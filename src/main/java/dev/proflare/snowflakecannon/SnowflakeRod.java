package dev.proflare.snowflakecannon;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class SnowflakeRod {

    public static final NamespacedKey KEY = new NamespacedKey("snowflakecannon", "snowflake_rod");

    public static ItemStack create() {
        ItemStack item = new ItemStack(Material.FISHING_ROD);
        ItemMeta meta = item.getItemMeta();

        meta.displayName(
            Component.text("Fishing Rod")
                .color(NamedTextColor.WHITE)
                .decoration(TextDecoration.ITALIC, false)
        );

        meta.lore(List.of());

        meta.getPersistentDataContainer().set(KEY, PersistentDataType.BYTE, (byte) 1);

        item.setItemMeta(meta);
        return item;
    }

    public static boolean isSnowflakeRod(ItemStack item) {
        if (item == null || item.getType() != Material.FISHING_ROD) return false;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;
        return meta.getPersistentDataContainer().has(KEY, PersistentDataType.BYTE);
    }
}
