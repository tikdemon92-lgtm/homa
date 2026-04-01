package ru.homa.dinamit.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.homa.dinamit.HomaDinamitPlugin;
import ru.homa.dinamit.types.DinamitType;

import java.util.Arrays;

/**
 * Класс отвечает за создание и отображение GUI-меню плагина HomaDinamit.
 */
public class DinamitGUI {

    private static final String TITLE = "✦ Homa Динамит ✦";
    private static final int ROWS = 6;
    private static final int SIZE = ROWS * 9;

    private final HomaDinamitPlugin plugin;

    public DinamitGUI(HomaDinamitPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Открывает главное меню динамита для игрока.
     *
     * @param player игрок, которому открывается меню
     */
    public void open(Player player) {
        String title = plugin.getConfig().getString("gui.title", TITLE);
        int rows = plugin.getConfig().getInt("gui.rows", ROWS);
        Inventory inv = Bukkit.createInventory(null, rows * 9, title);

        fillBorders(inv, rows * 9);
        addDinamitItems(inv, player);
        addInfoItem(inv);

        player.openInventory(inv);
    }

    /**
     * Заполняет границы инвентаря декоративными стеклянными панелями.
     */
    private void fillBorders(Inventory inv, int size) {
        ItemStack filler = createFiller(Material.BLACK_STAINED_GLASS_PANE, "§0");
        ItemStack accent = createFiller(Material.RED_STAINED_GLASS_PANE, "§4");

        // Верхняя и нижняя строки
        for (int i = 0; i < 9; i++) {
            inv.setItem(i, accent);
            inv.setItem(size - 9 + i, accent);
        }
        // Боковые колонки
        for (int i = 1; i < (size / 9) - 1; i++) {
            inv.setItem(i * 9, filler);
            inv.setItem(i * 9 + 8, filler);
        }
        // Угловые акценты
        inv.setItem(0, accent);
        inv.setItem(8, accent);
        inv.setItem(size - 9, accent);
        inv.setItem(size - 1, accent);
    }

    /**
     * Добавляет предметы динамита в меню с учётом прав игрока.
     */
    private void addDinamitItems(Inventory inv, Player player) {
        // Строки для размещения предметов динамита: 1-я, 2-я, 3-я строки (слоты 10–16, 19–25, 28–34)
        int[] slots = {10, 11, 12, 13, 14, 15, 16, 19, 20};
        int slotIndex = 0;

        for (DinamitType type : DinamitType.values()) {
            if (slotIndex >= slots.length) break;
            if (!player.hasPermission(type.getPermission())) {
                inv.setItem(slots[slotIndex], type.createLockedGuiItem());
            } else {
                inv.setItem(slots[slotIndex], type.createGuiItem());
            }
            slotIndex++;
        }
    }

    /**
     * Добавляет информационный предмет в нижнюю часть меню.
     */
    private void addInfoItem(Inventory inv) {
        ItemStack info = new ItemStack(Material.BOOK);
        ItemMeta meta = info.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§6✦ §eИнформация §6✦");
            meta.setLore(Arrays.asList(
                    "§7Добро пожаловать в меню",
                    "§6HomaDinamit§7!",
                    "",
                    "§7Нажмите на любой вид динамита,",
                    "§7чтобы получить его в руки.",
                    "",
                    "§8▸ §fЗелёная рамка — §adоступно",
                    "§8▸ §fКрасная рамка — §cзаблокировано",
                    "",
                    "§8Версия: §71.0.0"
            ));
            info.setItemMeta(meta);
        }
        inv.setItem(40, info);
    }

    /**
     * Создаёт декоративный заполнитель (стеклянную панель) без текста.
     */
    private ItemStack createFiller(Material material, String name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            item.setItemMeta(meta);
        }
        return item;
    }

    /**
     * Возвращает заголовок GUI для проверки в слушателе событий.
     */
    public static String getGuiTitle(String configTitle) {
        return configTitle != null ? configTitle : TITLE;
    }

    /**
     * Проверяет, является ли инвентарь меню динамита по заголовку.
     */
    public static boolean isDinamitGUI(String inventoryTitle, String configTitle) {
        String expected = configTitle != null ? configTitle : TITLE;
        return expected.equals(inventoryTitle);
    }
}
