package meteordevelopment.meteorkits.commands;

import meteordevelopment.meteoressentials.commands.MeteorCommand;
import meteordevelopment.meteorkits.Config;
import meteordevelopment.meteorkits.Kit;
import meteordevelopment.meteorkits.Kits;
import meteordevelopment.meteorkits.KitsGui;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginBase;

public class KitCommand extends MeteorCommand {
    public KitCommand(PluginBase plugin) {
        super(plugin, "kit", "Opens kit gui or equips named kit.", "/kit <name>");
    }

    @Override
    protected boolean onCommand(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player player)) return false;

        if (args.length == 1) {
            Kit kit = Kits.get(args[0]);

            if (kit == null) player.sendMessage(Config.getPrefix() + "Kit with name " + ChatColor.GRAY + "'" + args[0] + "' " + ChatColor.WHITE + "doesn't exist");
            else {
                if (kit.isPublic || Config.CAN_INTERACT_WITH_ALL_KITS.has(player) || kit.author.equals(player.getUniqueId())) kit.apply(player);
                else player.sendMessage(Config.getPrefix() + "You don't own that kit");
            }

            return true;
        }

        player.openInventory(KitsGui.guiMain(player));
        return true;
    }
}
