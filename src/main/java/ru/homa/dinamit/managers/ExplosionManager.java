package ru.homa.dinamit.managers;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import ru.homa.dinamit.HomaDinamitPlugin;
import ru.homa.dinamit.types.DinamitType;

import java.util.Random;

/**
 * Менеджер взрывов динамита.
 * Обрабатывает специальные эффекты различных типов динамита при взрыве.
 */
public class ExplosionManager {

    private final HomaDinamitPlugin plugin;
    private final Random random = new Random();

    public ExplosionManager(HomaDinamitPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Обрабатывает взрыв динамита заданного типа в указанном месте.
     *
     * @param type     тип динамита
     * @param location место взрыва
     * @param source   игрок-инициатор (может быть null)
     */
    public void handleExplosion(DinamitType type, Location location, Player source) {
        boolean blockDamage = plugin.getConfig().getBoolean("explosions.block-damage", true);
        boolean setFireGlobal = plugin.getConfig().getBoolean("explosions.set-fire", true);
        World world = location.getWorld();
        if (world == null) return;

        switch (type) {
            case OBYCHNYI -> world.createExplosion(location, type.getExplosionPower(), false, blockDamage);
            case MOSHCHNYI -> world.createExplosion(location, type.getExplosionPower(), false, blockDamage);
            case OGNENYI -> {
                boolean setFire = type.isSetFire() && setFireGlobal;
                world.createExplosion(location, type.getExplosionPower(), setFire, blockDamage);
            }
            case LEDYANOI -> handleLedyanoi(location, blockDamage, world);
            case MOLNIEVYI -> handleMolnievyi(location, world);
            case KLASTERNYI -> handleKlasternyi(location, blockDamage, world);
            case PODVODNYI -> handlePodvodnyi(location, blockDamage, world);
            case YADERNYI -> handleYadernyi(location, blockDamage, world);
        }
    }

    /**
     * Ледяной взрыв: создаёт взрыв и замораживает воду в области.
     */
    private void handleLedyanoi(Location center, boolean blockDamage, World world) {
        world.createExplosion(center, 5.0f, false, blockDamage);
        int radius = 5;
        for (int x = -radius; x <= radius; x++) {
            for (int y = -2; y <= 2; y++) {
                for (int z = -radius; z <= radius; z++) {
                    if (x * x + z * z > radius * radius) continue;
                    Location loc = center.clone().add(x, y, z);
                    Material mat = loc.getBlock().getType();
                    if (mat == Material.WATER) {
                        loc.getBlock().setType(Material.ICE);
                    } else if (mat == Material.LAVA) {
                        loc.getBlock().setType(Material.OBSIDIAN);
                    }
                }
            }
        }
    }

    /**
     * Молниевый взрыв: вызывает молнии вокруг места взрыва.
     */
    private void handleMolnievyi(Location center, World world) {
        world.createExplosion(center, 4.0f, false, true);
        int lightningCount = 8 + random.nextInt(5);
        int radius = 8;
        for (int i = 0; i < lightningCount; i++) {
            double angle = (2 * Math.PI * i) / lightningCount;
            double distance = 2 + random.nextDouble() * (radius - 2);
            Location strike = center.clone().add(
                    Math.cos(angle) * distance,
                    0,
                    Math.sin(angle) * distance
            );
            plugin.getServer().getScheduler().runTaskLater(plugin, () ->
                    world.strikeLightning(strike), i * 3L);
        }
    }

    /**
     * Кластерный взрыв: создаёт несколько меньших взрывов по случайным направлениям.
     */
    private void handleKlasternyi(Location center, boolean blockDamage, World world) {
        world.createExplosion(center, 3.0f, false, blockDamage);
        int clusters = 5;
        for (int i = 0; i < clusters; i++) {
            final int delay = (i + 1) * 5;
            Location cluster = center.clone().add(
                    (random.nextDouble() - 0.5) * 10,
                    0,
                    (random.nextDouble() - 0.5) * 10
            );
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                cluster.setY(cluster.getWorld().getHighestBlockYAt(cluster));
                world.createExplosion(cluster, 3.5f, false, blockDamage);
            }, delay);
        }
    }

    /**
     * Подводный взрыв: работает в любой среде без ослабления.
     */
    private void handlePodvodnyi(Location center, boolean blockDamage, World world) {
        // Создаём взрыв без учёта воды с двойной силой для компенсации
        world.createExplosion(center.getX(), center.getY(), center.getZ(),
                DinamitType.PODVODNYI.getExplosionPower(), false, blockDamage);
    }

    /**
     * Ядерный взрыв: огромная волна разрушения с несколькими взрывами.
     */
    private void handleYadernyi(Location center, boolean blockDamage, World world) {
        // Основной взрыв
        world.createExplosion(center, 15.0f, false, blockDamage);
        // Дополнительные волны
        int waves = 3;
        for (int wave = 1; wave <= waves; wave++) {
            final int w = wave;
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                int waveRadius = w * 4;
                for (int i = 0; i < 6; i++) {
                    double angle = (2 * Math.PI * i) / 6;
                    Location wLoc = center.clone().add(
                            Math.cos(angle) * waveRadius,
                            0,
                            Math.sin(angle) * waveRadius
                    );
                    world.createExplosion(wLoc, 6.0f, false, blockDamage);
                }
            }, wave * 10L);
        }
    }

}
