package meteordevelopment.meteorkits.commands;

import meteordevelopment.meteoressentials.commands.MeteorCommand;
import meteordevelopment.meteorkits.Config;
import meteordevelopment.meteorkits.Kit;
import meteordevelopment.meteorkits.KitLimits;
import meteordevelopment.meteorkits.Kits;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginBase;

public class CreateKitCommand extends MeteorCommand {
    public CreateKitCommand(PluginBase plugin) {
        super(plugin, "createkit", "Creates a new private kit.", "/createkit <name>");
    }

    @Override
    protected boolean onCommand(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player player)) return false;
        if (args.length != 1) return false;

        KitLimits limits = Config.getLimits(player);

        if (Kits.getKits(player).size() >= limits.maxCount()) {
            player.sendMessage(Config.getPrefix() + "You can only have " + limits.maxCount() + " kits");
            return true;
        }

        if (Kits.get(args[0]) != null) {
            player.sendMessage(Config.getPrefix() + "Kit with name " + ChatColor.GRAY + "'" + args[0] + "' " + ChatColor.WHITE + "already exists");
            return true;
        }

        Kits.add(new Kit(args[0], player));
        player.sendMessage(Config.getPrefix() + "Created kit with name " + ChatColor.GRAY + "'" + args[0] + "'");

        return true;
    }
}
