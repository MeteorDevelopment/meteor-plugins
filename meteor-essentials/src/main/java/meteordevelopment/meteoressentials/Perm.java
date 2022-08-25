package meteordevelopment.meteoressentials;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

public record Perm(Permission permission) {
    public boolean has(Player player) {
        return player.hasPermission(permission);
    }

    public boolean has(CommandSender sender) {
        return sender.hasPermission(permission);
    }
}
