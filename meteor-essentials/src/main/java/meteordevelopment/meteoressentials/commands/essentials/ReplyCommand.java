package meteordevelopment.meteoressentials.commands.essentials;

import meteordevelopment.meteoressentials.Config;
import meteordevelopment.meteoressentials.chat.PrivateMsgs;
import meteordevelopment.meteoressentials.commands.MeteorCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginBase;

public class ReplyCommand extends MeteorCommand {
    private final StringBuilder sb = new StringBuilder();

    public ReplyCommand(PluginBase plugin) {
        super(plugin, "r", "Replies to the last message.", "/r <message>");
    }

    @Override
    protected boolean onCommand(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player player)) return false;

        for (int i = 0; i < args.length; i++) {
            if (i > 0) sb.append(" ");
            sb.append(args[i]);
        }

        if (!PrivateMsgs.reply(player, sb.toString())) {
            player.sendMessage(Config.PREFIX + ChatColor.RED + "Nobody sent you a message or is offline");
        }
        sb.setLength(0);

        return true;
    }
}
