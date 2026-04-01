package ru.homa.dinamit.listeners;

import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.inventory.ItemStack;
import ru.homa.dinamit.HomaDinamitPlugin;
import ru.homa.dinamit.managers.CooldownManager;
import ru.homa.dinamit.managers.ExplosionManager;
import ru.homa.dinamit.types.DinamitType;

/**
 * Слушатель событий, связанных с установкой и взрывом блоков ТНТ (динамита).
 */
public class DinamitBlockListener implements Listener {

    private final HomaDinamitPlugin plugin;
    private final CooldownManager cooldownManager;
    private final ExplosionManager explosionManager;

    public DinamitBlockListener(HomaDinamitPlugin plugin,
                                CooldownManager cooldownManager,
                                ExplosionManager explosionManager) {
        this.plugin = plugin;
        this.cooldownManager = cooldownManager;
        this.explosionManager = explosionManager;
    }

    /**
     * Перехватывает установку блока ТНТ, чтобы определить,
     * является ли он особым видом динамита HomaDinamit.
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItemInHand();

        DinamitType type = DinamitType.fromItem(item);
        if (type == null) return;

        // Проверка разрешений
        if (plugin.getConfig().getBoolean("explosions.check-permissions", true)
                && !player.hasPermission(type.getPermission())) {
            event.setCancelled(true);
            player.sendMessage(formatMessage(
                    plugin.getConfig().getString("messages.prefix", ""),
                    plugin.getConfig().getString("messages.no-permission", "&cНет прав.")
            ));
            return;
        }

        // Проверка кулдауна
        int cooldown = plugin.getConfig().getInt("cooldowns." + type.getId(), 0);
        if (cooldownManager.isOnCooldown(player, type, cooldown)) {
            event.setCancelled(true);
            long remaining = cooldownManager.getRemainingSeconds(player, type, cooldown);
            String msg = plugin.getConfig()
                    .getString("messages.cooldown", "&eПодождите ещё &c%seconds% &eсек.")
                    .replace("%seconds%", String.valueOf(remaining));
            player.sendMessage(formatMessage(
                    plugin.getConfig().getString("messages.prefix", ""), msg));
            return;
        }

        // Регистрируем кулдаун
        if (cooldown > 0) {
            cooldownManager.setCooldown(player, type);
        }

        // Помечаем блок для последующей обработки взрыва
        Block block = event.getBlockPlaced();
        plugin.markBlock(block.getLocation(), type);
    }

    /**
     * Форматирует сообщение: добавляет префикс и применяет цветовые коды.
     */
    private String formatMessage(String prefix, String message) {
        return colorize(prefix + message);
    }

    private String colorize(String text) {
        return text.replace("&", "§");
    }
}
