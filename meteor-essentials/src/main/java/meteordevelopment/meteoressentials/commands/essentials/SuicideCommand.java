package meteordevelopment.meteoressentials.commands.essentials;

import meteordevelopment.meteoressentials.commands.MeteorCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginBase;

public class SuicideCommand extends MeteorCommand {
    public SuicideCommand(PluginBase plugin) {
        super(plugin, "suicide", "Kills you.", "/suicide");
    }

    @Override
    protected boolean onCommand(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player player)) return false;

        player.setHealth(0);
        return true;
    }
}
