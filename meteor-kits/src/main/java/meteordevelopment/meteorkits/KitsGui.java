package meteordevelopment.meteorkits;

import meteordevelopment.meteoressentials.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class KitsGui {
    public static final Component GUI_TITLE = Component.text("Kits");
    public static final Component GUI_PRIVATE_KITS_TITLE = Component.text("Private Kits");
    public static final Component GUI_PUBLIC_KITS_TITLE = Component.text("Public Kits");

    public static Inventory guiMain(HumanEntity player) {
        Inventory gui = Bukkit.createInventory(player, 9, GUI_TITLE);

        ItemStack privateKits = new ItemStack(Material.IRON_SWORD);
        Utils.setName(privateKits, GUI_PRIVATE_KITS_TITLE);
        gui.setItem(3, privateKits);

        ItemStack publicKits = new ItemStack(Material.GOLDEN_SWORD);
        Utils.setName(publicKits, GUI_PUBLIC_KITS_TITLE.color(NamedTextColor.GOLD));
        gui.setItem(5, publicKits);

        Utils.fillPanes(gui);
        return gui;
    }

    public static Inventory guiPrivateKits(UUID player, HumanEntity invHolder) {
        Inventory gui = Bukkit.createInventory(invHolder, 9, GUI_PRIVATE_KITS_TITLE);
        List<Kit> kits = Kits.getKits(player);

        for (int i = 0; i < 9; i++) {
            Kit kit = null;
            if (i < kits.size()) kit = kits.get(i);

            if (kit != null) {
                gui.setItem(i, newKitItemStack(player, kit));
            } else {
                int max = Config.getLimits(player).maxCount();

                ItemStack itemStack = new ItemStack(i < max ? Material.LIGHT_GRAY_STAINED_GLASS_PANE : Material.GRAY_STAINED_GLASS_PANE);
                if (i < max) Utils.setName(itemStack, Component.text("Empty kit slot", NamedTextColor.GRAY));
                else Utils.setName(itemStack, Component.text(kits.size() + " / " + max, NamedTextColor.GRAY));
                gui.setItem(i, itemStack);
            }
        }

        return gui;
    }

    public static Inventory guiPrivateKits(HumanEntity player) {
        return guiPrivateKits(player.getUniqueId(), player);
    }

    public static Inventory guiPublicKits(HumanEntity player) {
        Inventory gui = Bukkit.createInventory(player, 9 * 6, GUI_PUBLIC_KITS_TITLE);

        for (int i = 0; i < Math.max(Kits.getPublicKits().size(), 9 * 6); i++) {
            if (i >= 9 * 6) break;

            if (i < Kits.getPublicKits().size()) {
                gui.setItem(i, newKitItemStack(player, Kits.getPublicKits().get(i)));
            } else {
                ItemStack itemStack = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
                Utils.setName(itemStack, Component.text("No kit", NamedTextColor.GRAY));
                gui.setItem(i, itemStack);
            }
        }

        return gui;
    }

    private static ItemStack newKitItemStack(UUID player, Kit kit) {
        ItemStack itemStack = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta meta = itemStack.getItemMeta();

        KitStats.count(kit);

        meta.displayName(Component.text(ChatColor.AQUA + kit.name));
        List<Component> lore = new ArrayList<>(11);
        lore.add(Component.text(""));
        lore.add(Component.text(ChatColor.WHITE + "Totems: " + ChatColor.GRAY + KitStats.totems));
        lore.add(Component.text(ChatColor.WHITE + "EGaps: " + ChatColor.GRAY + KitStats.egaps));
        lore.add(Component.text(ChatColor.WHITE + "Xp Bottles: " + ChatColor.GRAY + KitStats.xpBottles));
        lore.add(Component.text(ChatColor.WHITE + "Obsidian: " + ChatColor.GRAY + KitStats.obsidian));
        lore.add(Component.text(ChatColor.WHITE + "Crystals: " + ChatColor.GRAY + KitStats.crystals));
        lore.add(Component.text(ChatColor.WHITE + "Beds: " + ChatColor.GRAY + KitStats.beds));
        lore.add(Component.text(""));
        lore.add(Component.text(ChatColor.GRAY + Bukkit.getOfflinePlayer(kit.author).getName()));
        KitLimits limits = Config.getLimits(player);
        if (limits.canHavePublic() || kit.isPublic) {
            lore.add(Component.text(""));
            if (kit.isPublic) lore.add(Component.text(ChatColor.GREEN + "Public"));
            else if (limits.canHavePublic() && kit.author.equals(player)) lore.add(Component.text(ChatColor.GRAY + "Right click to set as public."));
        }
        meta.lore(lore);

        itemStack.setItemMeta(meta);
        return itemStack;
    }

    private static ItemStack newKitItemStack(HumanEntity player, Kit kit) {
        return newKitItemStack(player.getUniqueId(), kit);
    }
}
