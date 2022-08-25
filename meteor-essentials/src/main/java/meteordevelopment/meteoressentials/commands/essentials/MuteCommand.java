package meteordevelopment.meteoressentials.commands.essentials;

import meteordevelopment.meteoressentials.Config;
import meteordevelopment.meteoressentials.Perms;
import meteordevelopment.meteoressentials.chat.Mutes;
import meteordevelopment.meteoressentials.commands.MeteorCommand;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginBase;

public class MuteCommand extends MeteorCommand {
    public MuteCommand(PluginBase plugin) {
        super(plugin, "mute", "Mutes a player.", "/mute <username>", Perms.get(plugin, "mute"));
    }

    @Override
    protected boolean onCommand(CommandSender sender, String label, String[] args) {
        if (args.length != 1) return false;

        OfflinePlayer toMute = Bukkit.getOfflinePlayer(args[0]);
        boolean toMuteOnline = toMute.getPlayer() != null;

        if (perm.has(sender) && toMuteOnline && perm.has(toMute.getPlayer())) {
            sender.sendMessage(Config.PREFIX + "You cannot mute another person who can also mute.");
            return true;
        }

        if (Mutes.addMute(toMute)) {
            Bukkit.broadcast(Component.text(Config.PREFIX + String.format("%s%s%s has been muted.", ChatColor.RED, toMute.getName(), ChatColor.GRAY)));
        }
        else {
            sender.sendMessage(Config.PREFIX + "That player is already muted.");
        }

        return true;
    }
}
