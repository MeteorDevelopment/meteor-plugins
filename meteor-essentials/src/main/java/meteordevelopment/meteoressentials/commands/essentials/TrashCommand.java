package meteordevelopment.meteoressentials.commands.essentials;

import meteordevelopment.meteoressentials.commands.MeteorCommand;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginBase;

public class TrashCommand extends MeteorCommand {
    public TrashCommand(PluginBase plugin) {
        super(plugin, "trash", "Opens a trash can.", "/trash");
    }

    @Override
    protected boolean onCommand(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player player)) return false;

        player.openInventory(Bukkit.createInventory(player, 9 * 4, Component.text("Trash Can")));
        return true;
    }
}
