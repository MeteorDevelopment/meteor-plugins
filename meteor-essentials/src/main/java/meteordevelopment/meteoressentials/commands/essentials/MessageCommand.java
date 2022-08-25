package meteordevelopment.meteoressentials.commands.essentials;

import meteordevelopment.meteoressentials.Config;
import meteordevelopment.meteoressentials.chat.PrivateMsgs;
import meteordevelopment.meteoressentials.commands.MeteorCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginBase;

public class MessageCommand extends MeteorCommand {
    private final StringBuilder sb = new StringBuilder();

    public MessageCommand(PluginBase plugin) {
        super(plugin, "msg", "Messages a person.", "/msg <name> <message>");
    }

    @Override
    protected boolean onCommand(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player player) || args.length < 2) return false;

        Player receiver = Bukkit.getPlayer(args[0]);
        if (receiver == null) {
            player.sendMessage(Config.PREFIX + ChatColor.WHITE + args[0] + ChatColor.GRAY + " is not online");
            return true;
        }

        for (int i = 1; i < args.length; i++) {
            if (i > 1) sb.append(" ");
            sb.append(args[i]);
        }

        PrivateMsgs.send(player, receiver, sb.toString());
        sb.setLength(0);

        return true;
    }
}
