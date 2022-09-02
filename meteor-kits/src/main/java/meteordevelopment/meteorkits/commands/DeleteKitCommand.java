package meteordevelopment.meteorkits.commands;

import meteordevelopment.meteoressentials.commands.MeteorCommand;
import meteordevelopment.meteorkits.Config;
import meteordevelopment.meteorkits.Kit;
import meteordevelopment.meteorkits.Kits;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginBase;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DeleteKitCommand extends MeteorCommand {
    private static final List<String> LIST = new ArrayList<>();

    public DeleteKitCommand(PluginBase plugin) {
        super(plugin, "deletekit", "Deletes a kit.", "/deletekit <name>");
    }

    @Override
    protected boolean onCommand(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player player)) return false;
        if (args.length != 1) return false;

        boolean deleted;
        if (Config.CAN_INTERACT_WITH_ALL_KITS.has(player)) deleted = Kits.remove(args[0]);
        else deleted = Kits.remove(player, args[0]);

        if (deleted) player.sendMessage(Config.getPrefix() + "Kit with name " + ChatColor.GRAY + "'" + args[0] + "' " + ChatColor.WHITE + "has been deleted");
        else player.sendMessage(Config.getPrefix() + "Kit with name " + ChatColor.GRAY + "'" + args[0] + "' " + ChatColor.WHITE + "doesn't exist");

        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, String[] args, Location location) throws IllegalArgumentException {
        LIST.clear();
        if (sender instanceof Player) {
            for (Kit kit : Kits.getKits((Player) sender)) LIST.add(kit.name);
        }
        return LIST;
    }
}
