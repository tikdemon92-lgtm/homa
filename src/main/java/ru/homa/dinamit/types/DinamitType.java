package ru.homa.dinamit.types;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

/**
 * Перечисление всех видов динамита, доступных в плагине HomaDinamit.
 */
public enum DinamitType {

    OBYCHNYI(
            "obychnyi",
            "§6✦ §eОбычный Динамит §6✦",
            Arrays.asList(
                    "§7Классический динамит с умеренной",
                    "§7силой взрыва. Отлично подходит",
                    "§7для начинающих подрывников.",
                    "",
                    "§8▸ §fМощность: §c★★☆☆☆",
                    "§8▸ §fРадиус: §a3 блока",
                    "§8▸ §fОгонь: §cНет",
                    "",
                    "§eНажмите, чтобы взять!"
            ),
            Material.TNT,
            3.0f,
            false,
            "homadinamit.type.obychnyi",
            1,
            1001
    ),

    MOSHCHNYI(
            "moshchnyi",
            "§4✦ §cМощный Динамит §4✦",
            Arrays.asList(
                    "§7Усиленный динамит с повышенной",
                    "§7разрушительной силой. Разнесёт",
                    "§7целый участок за один взрыв.",
                    "",
                    "§8▸ §fМощность: §c★★★★☆",
                    "§8▸ §fРадиус: §a6 блоков",
                    "§8▸ §fОгонь: §cНет",
                    "",
                    "§eНажмите, чтобы взять!"
            ),
            Material.TNT,
            6.0f,
            false,
            "homadinamit.type.moshchnyi",
            2,
            1002
    ),

    OGNENYI(
            "ognenyi",
            "§6✦ §cОгненный Динамит §6✦",
            Arrays.asList(
                    "§7Динамит, пропитанный огнём.",
                    "§7При взрыве поджигает всё вокруг,",
                    "§7превращая местность в пепелище.",
                    "",
                    "§8▸ §fМощность: §c★★★☆☆",
                    "§8▸ §fРадиус: §a4 блока",
                    "§8▸ §fОгонь: §6Да §c🔥",
                    "",
                    "§eНажмите, чтобы взять!"
            ),
            Material.FIRE_CHARGE,
            4.0f,
            true,
            "homadinamit.type.ognenyi",
            3,
            1003
    ),

    LEDYANOI(
            "ledyanoi",
            "§b✦ §3Ледяной Динамит §b✦",
            Arrays.asList(
                    "§7Морозный динамит, замораживающий",
                    "§7всё в радиусе взрыва. Превращает",
                    "§7воду и лаву в лёд и обсидиан.",
                    "",
                    "§8▸ §fМощность: §c★★☆☆☆",
                    "§8▸ §fРадиус: §a5 блоков",
                    "§8▸ §fЭффект: §bЗаморозка ❄",
                    "",
                    "§eНажмите, чтобы взять!"
            ),
            Material.BLUE_ICE,
            5.0f,
            false,
            "homadinamit.type.ledyanoi",
            4,
            1004
    ),

    MOLNIEVYI(
            "molnievyi",
            "§e✦ §6Молниевый Динамит §e✦",
            Arrays.asList(
                    "§7Динамит, заряженный молниями.",
                    "§7При взрыве вызывает серию",
                    "§7ударов молнии в округе.",
                    "",
                    "§8▸ §fМощность: §c★★★☆☆",
                    "§8▸ §fРадиус: §a8 блоков",
                    "§8▸ §fЭффект: §eМолния ⚡",
                    "",
                    "§eНажмите, чтобы взять!"
            ),
            Material.LIGHTNING_ROD,
            4.0f,
            false,
            "homadinamit.type.molnievyi",
            5,
            1005
    ),

    KLASTERNYI(
            "klasternyi",
            "§5✦ §dКластерный Динамит §5✦",
            Arrays.asList(
                    "§7Динамит, распадающийся на",
                    "§7несколько меньших зарядов при",
                    "§7взрыве. Накрывает большую площадь.",
                    "",
                    "§8▸ §fМощность: §c★★★★☆",
                    "§8▸ §fРадиус: §a10 блоков",
                    "§8▸ §fЭффект: §dМножественный взрыв 💥",
                    "",
                    "§eНажмите, чтобы взять!"
            ),
            Material.FIREWORK_ROCKET,
            3.0f,
            false,
            "homadinamit.type.klasternyi",
            6,
            1006
    ),

    PODVODNYI(
            "podvodnyi",
            "§3✦ §9Подводный Динамит §3✦",
            Arrays.asList(
                    "§7Специальный динамит для",
                    "§7подводных операций. Взрывается",
                    "§7без ущерба для горения под водой.",
                    "",
                    "§8▸ §fМощность: §c★★★☆☆",
                    "§8▸ §fРадиус: §a4 блока",
                    "§8▸ §fЭффект: §9Подводный взрыв 🌊",
                    "",
                    "§eНажмите, чтобы взять!"
            ),
            Material.PRISMARINE_CRYSTALS,
            4.0f,
            false,
            "homadinamit.type.podvodnyi",
            7,
            1007
    ),

    YADERNYI(
            "yadernyi",
            "§2✦ §aЯдерный Динамит §2✦",
            Arrays.asList(
                    "§7Чрезвычайно мощный динамит",
                    "§7с огромной силой разрушения.",
                    "§7§cИспользуйте с крайней осторожностью!",
                    "",
                    "§8▸ §fМощность: §c★★★★★",
                    "§8▸ §fРадиус: §a15 блоков",
                    "§8▸ §fЭффект: §aМегавзрыв ☢",
                    "",
                    "§eНажмите, чтобы взять!"
            ),
            Material.NETHER_STAR,
            15.0f,
            false,
            "homadinamit.type.yadernyi",
            8,
            1008
    );

    private final String id;
    private final String displayName;
    private final List<String> lore;
    private final Material material;
    private final float explosionPower;
    private final boolean setFire;
    private final String permission;
    private final int guiSlot;
    /** Значение Custom Model Data для иконки ресурс-пака (PAPER с CMDом). */
    private final int customModelData;

    DinamitType(String id, String displayName, List<String> lore, Material material,
                float explosionPower, boolean setFire, String permission, int guiSlot,
                int customModelData) {
        this.id = id;
        this.displayName = displayName;
        this.lore = lore;
        this.material = material;
        this.explosionPower = explosionPower;
        this.setFire = setFire;
        this.permission = permission;
        this.guiSlot = guiSlot;
        this.customModelData = customModelData;
    }

    public String getId() { return id; }
    public String getDisplayName() { return displayName; }
    public List<String> getLore() { return lore; }
    public Material getMaterial() { return material; }
    public float getExplosionPower() { return explosionPower; }
    public boolean isSetFire() { return setFire; }
    public String getPermission() { return permission; }
    public int getGuiSlot() { return guiSlot; }
    public int getCustomModelData() { return customModelData; }

    /**
     * Создаёт ItemStack для данного типа динамита с указанным количеством.
     * Это настоящий предмет, который кладётся в инвентарь игрока.
     */
    public ItemStack createItem(int amount) {
        ItemStack item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(displayName);
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        return item;
    }

    /**
     * Создаёт ItemStack с количеством 1 для этого типа динамита.
     */
    public ItemStack createItem() {
        return createItem(1);
    }

    /**
     * Создаёт отображаемый предмет для GUI меню.
     * Использует бумагу (Material.PAPER) с Custom Model Data, чтобы ресурс-пак
     * мог заменить текстуру на иконку из набора CyberTenfa.
     */
    public ItemStack createGuiItem() {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(displayName);
            meta.setLore(lore);
            meta.setCustomModelData(customModelData);
            item.setItemMeta(meta);
        }
        return item;
    }

    /**
     * Создаёт заблокированный предмет для GUI (нет прав).
     */
    public ItemStack createLockedGuiItem() {
        ItemStack item = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§c✖ §7" + stripColor(displayName));
            meta.setLore(Arrays.asList(
                    "§cДоступ закрыт",
                    "§7У вас нет разрешения",
                    "§7на использование этого",
                    "§7вида динамита.",
                    "",
                    "§8Требуется право: §c" + permission
            ));
            item.setItemMeta(meta);
        }
        return item;
    }

    /**
     * Находит тип динамита по его строковому идентификатору (без учёта регистра).
     */
    public static DinamitType fromId(String id) {
        if (id == null) return null;
        for (DinamitType type : values()) {
            if (type.id.equalsIgnoreCase(id)) return type;
        }
        return null;
    }

    /**
     * Находит тип динамита по ItemStack (сравнивает отображаемое имя).
     */
    public static DinamitType fromItem(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return null;
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) return null;
        String name = meta.getDisplayName();
        for (DinamitType type : values()) {
            if (type.displayName.equals(name)) return type;
        }
        return null;
    }

    private static String stripColor(String text) {
        return text.replaceAll("§[0-9a-fk-or]", "");
    }
}
