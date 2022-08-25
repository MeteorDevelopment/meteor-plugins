package meteordevelopment.meteoressentials.commands.essentials;

import meteordevelopment.meteoressentials.Config;
import meteordevelopment.meteoressentials.chat.Ignores;
import meteordevelopment.meteoressentials.commands.MeteorCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginBase;

import java.util.UUID;

public class IgnoreCommand extends MeteorCommand {
    private final StringBuilder sb = new StringBuilder();

    public IgnoreCommand(PluginBase plugin) {
        super(plugin, "ignore", "Hides player messages.", "/ignore <username>");
    }

    @Override
    protected boolean onCommand(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player player)) return false;

        if (args.length == 0) {
            sender.sendMessage(Config.PREFIX + "To add/remove people do " + ChatColor.WHITE + " /ignore <name>");

            sb.append(Config.PREFIX).append("Currently ignoring: ").append(ChatColor.WHITE);
            int i = 0;
            for (UUID ignored : Ignores.getIgnores(player)) {
                if (i > 0) sb.append(", ");
                sb.append(Bukkit.getOfflinePlayer(ignored).getName());
                i++;
            }

            player.sendMessage(sb.toString());
            sb.setLength(0);
        }
        else if (args.length == 1) {
            OfflinePlayer toIgnore = Bukkit.getOfflinePlayer(args[0]);

            if (toIgnore.hasPlayedBefore()) {
                boolean added = Ignores.toggleIgnore(player, toIgnore.getUniqueId());

                if (added) player.sendMessage(Config.PREFIX + ChatColor.GRAY + "Added " + ChatColor.WHITE + toIgnore.getName() + ChatColor.GRAY + " to your ignore list");
                else player.sendMessage(Config.PREFIX + ChatColor.GRAY + "Removed " + ChatColor.WHITE + toIgnore.getName() + ChatColor.GRAY + " from your ignore list");
            }
            else {
                player.sendMessage(Config.PREFIX + ChatColor.WHITE + args[0] + ChatColor.GRAY + " never played here before");
            }
        }

        return true;
    }
}
