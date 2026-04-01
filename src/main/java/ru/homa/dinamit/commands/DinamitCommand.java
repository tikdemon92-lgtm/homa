package ru.homa.dinamit.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.homa.dinamit.HomaDinamitPlugin;
import ru.homa.dinamit.gui.DinamitGUI;
import ru.homa.dinamit.types.DinamitType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Обработчик команды /dinamit.
 * Открывает GUI-меню или выдаёт динамит через подкоманды.
 */
public class DinamitCommand implements CommandExecutor, TabCompleter {

    private final HomaDinamitPlugin plugin;
    private final DinamitGUI gui;

    public DinamitCommand(HomaDinamitPlugin plugin, DinamitGUI gui) {
        this.plugin = plugin;
        this.gui = gui;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            // Открыть GUI меню
            if (!(sender instanceof Player player)) {
                sender.sendMessage(colorize(
                        plugin.getConfig().getString("messages.prefix", "") +
                        plugin.getConfig().getString("messages.player-only",
                                "&cЭта команда доступна только игрокам.")));
                return true;
            }
            if (!player.hasPermission("homadinamit.use")) {
                player.sendMessage(colorize(
                        plugin.getConfig().getString("messages.prefix", "") +
                        plugin.getConfig().getString("messages.no-permission",
                                "&cУ вас недостаточно прав.")));
                return true;
            }
            gui.open(player);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "give" -> handleGive(sender, args);
            case "reload" -> handleReload(sender);
            case "help" -> sendHelp(sender);
            default -> {
                sender.sendMessage(colorize(
                        plugin.getConfig().getString("messages.prefix", "") +
                        "&cНеизвестная подкоманда. Введите &f/dinamit help &cдля справки."));
            }
        }
        return true;
    }

    /**
     * Подкоманда /dinamit give <игрок> <тип> [количество].
     */
    private void handleGive(CommandSender sender, String[] args) {
        if (!sender.hasPermission("homadinamit.give")) {
            sender.sendMessage(colorize(prefix() +
                    plugin.getConfig().getString("messages.no-permission",
                            "&cУ вас недостаточно прав.")));
            return;
        }
        if (args.length < 3) {
            sender.sendMessage(colorize(prefix() +
                    plugin.getConfig().getString("messages.usage-give",
                            "&eИспользование: &f/dinamit give <игрок> <тип> [количество]")));
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(colorize(prefix() +
                    plugin.getConfig().getString("messages.player-not-found",
                            "&cИгрок &f%player% &cне найден.")
                    .replace("%player%", args[1])));
            return;
        }

        DinamitType type = DinamitType.fromId(args[2]);
        if (type == null) {
            sender.sendMessage(colorize(prefix() +
                    plugin.getConfig().getString("messages.invalid-type",
                            "&cНеизвестный тип динамита.")));
            return;
        }

        int amount = 1;
        if (args.length >= 4) {
            try {
                amount = Math.max(1, Math.min(64, Integer.parseInt(args[3])));
            } catch (NumberFormatException ignored) {}
        }

        ItemStack item = type.createItem(amount);
        target.getInventory().addItem(item);

        String receivedMsg = plugin.getConfig()
                .getString("messages.received", "&aВы получили &6%amount%x %name%&a!")
                .replace("%amount%", String.valueOf(amount))
                .replace("%name%", type.getDisplayName());
        target.sendMessage(colorize(prefix() + receivedMsg));

        String givenMsg = plugin.getConfig()
                .getString("messages.given-to", "&aВыдано &6%amount%x %name% &aигроку &f%player%&a.")
                .replace("%amount%", String.valueOf(amount))
                .replace("%name%", type.getDisplayName())
                .replace("%player%", target.getName());
        sender.sendMessage(colorize(prefix() + givenMsg));
    }

    /**
     * Подкоманда /dinamit reload — перезагрузка конфигурации.
     */
    private void handleReload(CommandSender sender) {
        if (!sender.hasPermission("homadinamit.admin")) {
            sender.sendMessage(colorize(prefix() +
                    plugin.getConfig().getString("messages.no-permission",
                            "&cУ вас недостаточно прав.")));
            return;
        }
        plugin.reloadConfig();
        sender.sendMessage(colorize(prefix() +
                plugin.getConfig().getString("messages.reload",
                        "&aКонфигурация успешно перезагружена.")));
    }

    /**
     * Отправляет справку по командам плагина.
     */
    private void sendHelp(CommandSender sender) {
        sender.sendMessage(colorize("&8&m                                        "));
        sender.sendMessage(colorize("  &6✦ &eHoma&6Динамит &7— Справка &6✦"));
        sender.sendMessage(colorize("&8&m                                        "));
        sender.sendMessage(colorize("  &f/dinamit &7— Открыть меню динамита"));
        sender.sendMessage(colorize("  &f/dinamit give &e<игрок> <тип> &7[кол.] &7— Выдать динамит"));
        sender.sendMessage(colorize("  &f/dinamit reload &7— Перезагрузить конфигурацию"));
        sender.sendMessage(colorize("  &f/dinamit help &7— Показать эту справку"));
        sender.sendMessage(colorize("&8&m                                        "));
        sender.sendMessage(colorize("  &7Типы динамита:"));
        for (DinamitType type : DinamitType.values()) {
            sender.sendMessage(colorize("  &8▸ &f" + type.getId() + " &7— " + type.getDisplayName()));
        }
        sender.sendMessage(colorize("&8&m                                        "));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return filterStartsWith(Arrays.asList("give", "reload", "help"), args[0]);
        }
        if (args.length == 2 && args[0].equalsIgnoreCase("give")) {
            return filterStartsWith(
                    Bukkit.getOnlinePlayers().stream()
                            .map(Player::getName)
                            .collect(Collectors.toList()),
                    args[1]
            );
        }
        if (args.length == 3 && args[0].equalsIgnoreCase("give")) {
            return filterStartsWith(
                    Arrays.stream(DinamitType.values())
                            .map(DinamitType::getId)
                            .collect(Collectors.toList()),
                    args[2]
            );
        }
        return Collections.emptyList();
    }

    private List<String> filterStartsWith(List<String> list, String prefix) {
        return list.stream()
                .filter(s -> s.toLowerCase().startsWith(prefix.toLowerCase()))
                .collect(Collectors.toList());
    }

    private String prefix() {
        return plugin.getConfig().getString("messages.prefix", "");
    }

    private String colorize(String text) {
        return text.replace("&", "§");
    }
}
