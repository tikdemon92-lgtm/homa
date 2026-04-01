package ru.homa.dinamit.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import ru.homa.dinamit.HomaDinamitPlugin;
import ru.homa.dinamit.gui.DinamitGUI;
import ru.homa.dinamit.types.DinamitType;

/**
 * Слушатель кликов по GUI меню динамита.
 * Обрабатывает выбор игрока и выдаёт соответствующий вид динамита.
 */
public class DinamitGUIListener implements Listener {

    private final HomaDinamitPlugin plugin;

    public DinamitGUIListener(HomaDinamitPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        String title = event.getView().getTitle();
        String configTitle = plugin.getConfig().getString("gui.title");

        if (!DinamitGUI.isDinamitGUI(title, configTitle)) return;

        event.setCancelled(true);

        ItemStack clicked = event.getCurrentItem();
        if (clicked == null) return;

        DinamitType type = DinamitType.fromItem(clicked);
        if (type == null) return;

        // Проверка прав
        if (!player.hasPermission(type.getPermission())) {
            player.sendMessage(colorize(
                    plugin.getConfig().getString("messages.prefix", "") +
                    plugin.getConfig().getString("messages.no-permission",
                            "&cУ вас недостаточно прав для этого действия.")
            ));
            return;
        }

        // Выдаём динамит игроку
        ItemStack dinamit = type.createItem();
        player.getInventory().addItem(dinamit);
        player.closeInventory();

        String msg = plugin.getConfig()
                .getString("messages.received", "&aВы получили &6%amount%x %name%&a!")
                .replace("%amount%", "1")
                .replace("%name%", type.getDisplayName());
        player.sendMessage(colorize(
                plugin.getConfig().getString("messages.prefix", "") + msg));
    }

    private String colorize(String text) {
        return text.replace("&", "§");
    }
}
