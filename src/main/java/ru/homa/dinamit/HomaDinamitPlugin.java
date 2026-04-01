package ru.homa.dinamit;

import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import ru.homa.dinamit.commands.DinamitCommand;
import ru.homa.dinamit.gui.DinamitGUI;
import ru.homa.dinamit.listeners.DinamitBlockListener;
import ru.homa.dinamit.listeners.DinamitExplosionListener;
import ru.homa.dinamit.listeners.DinamitGUIListener;
import ru.homa.dinamit.managers.CooldownManager;
import ru.homa.dinamit.managers.ExplosionManager;
import ru.homa.dinamit.types.DinamitType;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Главный класс плагина HomaDinamit.
 *
 * <p>HomaDinamit — плагин для серверов Bukkit/Spigot/Paper, добавляющий
 * разнообразные виды динамита с красивым GUI-меню и полностью русским интерфейсом.</p>
 */
public class HomaDinamitPlugin extends JavaPlugin {

    /** Карта помеченных блоков ТНТ → тип динамита (для обработки специальных взрывов). */
    private final Map<String, DinamitType> markedBlocks = new HashMap<>();

    private CooldownManager cooldownManager;
    private ExplosionManager explosionManager;
    private DinamitGUI gui;

    @Override
    public void onEnable() {
        // Сохраняем конфигурацию по умолчанию, если её нет
        saveDefaultConfig();

        // Инициализируем менеджеры
        cooldownManager = new CooldownManager();
        explosionManager = new ExplosionManager(this);
        gui = new DinamitGUI(this);

        // Регистрируем команды
        DinamitCommand commandExecutor = new DinamitCommand(this, gui);
        Objects.requireNonNull(getCommand("dinamit"), "Команда 'dinamit' не найдена в plugin.yml!")
                .setExecutor(commandExecutor);
        Objects.requireNonNull(getCommand("dinamit"))
                .setTabCompleter(commandExecutor);

        // Регистрируем слушателей событий
        getServer().getPluginManager().registerEvents(new DinamitGUIListener(this), this);
        getServer().getPluginManager().registerEvents(
                new DinamitBlockListener(this, cooldownManager, explosionManager), this);
        getServer().getPluginManager().registerEvents(
                new DinamitExplosionListener(this, explosionManager), this);

        // Приветственное сообщение в консоли
        printBanner();
    }

    @Override
    public void onDisable() {
        markedBlocks.clear();
        getLogger().info("HomaDinamit отключён. До свидания!");
    }

    // ─── Работа с помеченными блоками ──────────────────────────────────────────

    /**
     * Помечает местоположение блока как определённый тип динамита.
     *
     * @param location местоположение блока ТНТ
     * @param type     тип динамита HomaDinamit
     */
    public void markBlock(Location location, DinamitType type) {
        markedBlocks.put(locationKey(location), type);
    }

    /**
     * Возвращает тип динамита для данного местоположения или {@code null}, если не помечено.
     *
     * @param location местоположение для проверки
     * @return тип динамита или {@code null}
     */
    public DinamitType getMarkedType(Location location) {
        return markedBlocks.get(locationKey(location));
    }

    /**
     * Снимает метку с местоположения блока.
     *
     * @param location местоположение для снятия метки
     */
    public void unmarkBlock(Location location) {
        markedBlocks.remove(locationKey(location));
    }

    /**
     * Генерирует строковый ключ для местоположения блока.
     */
    private String locationKey(Location loc) {
        return (loc.getWorld() != null ? loc.getWorld().getName() : "world") +
                ":" + loc.getBlockX() + ":" + loc.getBlockY() + ":" + loc.getBlockZ();
    }

    // ─── Вспомогательные методы ─────────────────────────────────────────────────

    public CooldownManager getCooldownManager() { return cooldownManager; }
    public ExplosionManager getExplosionManager() { return explosionManager; }
    public DinamitGUI getGui() { return gui; }

    /**
     * Выводит приветственный баннер плагина в консоль.
     */
    private void printBanner() {
        String version = getDescription().getVersion();
        int count = DinamitType.values().length;
        getLogger().info("╔══════════════════════════════════════╗");
        getLogger().info("║     HomaDinamit v" + version + " запущен!       ║");
        getLogger().info("║  Платформа: Bukkit / Spigot / Paper  ║");
        getLogger().info("║  Динамита загружено: " + count + " видов           ║");
        getLogger().info("╚══════════════════════════════════════╝");
    }
}
