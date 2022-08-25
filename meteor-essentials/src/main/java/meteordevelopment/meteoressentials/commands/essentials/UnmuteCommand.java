package meteordevelopment.meteoressentials.commands.essentials;

import meteordevelopment.meteoressentials.Config;
import meteordevelopment.meteoressentials.Perms;
import meteordevelopment.meteoressentials.chat.Mutes;
import meteordevelopment.meteoressentials.commands.MeteorCommand;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginBase;

public class UnmuteCommand extends MeteorCommand {
    public UnmuteCommand(PluginBase plugin) {
        super(plugin, "unmute", "Unmutes a player.", "/unmute <username>", Perms.get(plugin, "mute"));
    }

    @Override
    protected boolean onCommand(CommandSender sender, String label, String[] args) {
        Player toUnmute = Bukkit.getPlayer(args[0]);

        if (toUnmute == null) {
            if (sender instanceof Player) sender.sendMessage(Config.PREFIX + "Player is not online.");
            return true;
        }

        if (sender == toUnmute) {
            sender.sendMessage(Config.PREFIX + "You cannot unmute yourself.");
            return true;
        }

        if (Mutes.removeMute(toUnmute)) {
            Bukkit.broadcast(Component.text(Config.PREFIX + String.format("%s%s%s has been unmuted.", ChatColor.RED, toUnmute.getName(), ChatColor.GRAY)));
        }
        else {
            if (sender instanceof Player) sender.sendMessage(Config.PREFIX + "That player is not muted.");
        }

        return true;
    }
}
