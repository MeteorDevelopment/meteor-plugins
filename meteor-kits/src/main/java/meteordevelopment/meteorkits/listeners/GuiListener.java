package meteordevelopment.meteorkits.listeners;

import meteordevelopment.meteoressentials.Utils;
import meteordevelopment.meteorkits.Config;
import meteordevelopment.meteorkits.Kit;
import meteordevelopment.meteorkits.Kits;
import meteordevelopment.meteorkits.KitsGui;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.function.Consumer;

public class GuiListener implements Listener {
    @EventHandler
    private void onInventoryClick(InventoryClickEvent event) {
        if (event.getCurrentItem() == null) return;

        Component name = event.getView().title();
        if (name.equals(KitsGui.GUI_TITLE)) onGui(event, this::onGuiMain);
        else if (name.equals(KitsGui.GUI_PRIVATE_KITS_TITLE) || name.equals(KitsGui.GUI_PUBLIC_KITS_TITLE)) onGui(event, null);
    }

    private void onGui(InventoryClickEvent event, Consumer<InventoryClickEvent> handler) {
        event.setCancelled(true);

        if (event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.DIAMOND_SWORD) {
            Kit kit = Kits.get(ChatColor.stripColor(Utils.toString(event.getCurrentItem().getItemMeta().displayName())));

            if (kit != null) {
                if (event.getClick() == ClickType.LEFT) {
                    kit.apply(event.getWhoClicked());
                    event.getWhoClicked().closeInventory();
                }
                else if (event.getClick() == ClickType.RIGHT && Config.getLimits(event.getWhoClicked()).canHavePublic() && kit.author.equals(event.getWhoClicked().getUniqueId())) {
                    if (kit.isPublic) Kits.removePublic(kit);
                    else {
                        Kits.addPublic(kit);

                        for (Kit k : Kits.getKits(event.getWhoClicked())) {
                            if (k.isPublic) {
                                k.isPublic = false;
                                Kits.removePublic(k);
                            }
                        }
                    }

                    kit.isPublic = !kit.isPublic;
                    event.getWhoClicked().openInventory(KitsGui.guiPrivateKits(event.getWhoClicked()));
                }
            }

            return;
        }

        if (handler != null) handler.accept(event);
    }

    private void onGuiMain(InventoryClickEvent event) {
        if (event.getCurrentItem() == null) return;
        String name = ChatColor.stripColor(Utils.toString(event.getCurrentItem().getItemMeta().displayName()));

        if (name.equals(Utils.toString(KitsGui.GUI_PRIVATE_KITS_TITLE))) {
            event.getWhoClicked().openInventory(KitsGui.guiPrivateKits(event.getWhoClicked()));
        }
        else if (name.equals(Utils.toString(KitsGui.GUI_PUBLIC_KITS_TITLE))) {
            event.getWhoClicked().openInventory(KitsGui.guiPublicKits(event.getWhoClicked()));
        }
    }
}
