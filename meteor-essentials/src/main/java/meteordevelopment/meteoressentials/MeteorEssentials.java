package meteordevelopment.meteoressentials;

import meteordevelopment.meteoressentials.chat.Ignores;
import meteordevelopment.meteoressentials.chat.Mutes;
import meteordevelopment.meteoressentials.commands.Commands;
import meteordevelopment.meteoressentials.listeners.Listeners;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class MeteorEssentials extends JavaPlugin {
    public static MeteorEssentials INSTANCE;

    private boolean initialized;

    @Override
    public void onEnable() {
        INSTANCE = this;
        initialized = false;

        Config.load();
        Mutes.load();
        Ignores.load();

        Listeners.register(this, Listeners.class.getPackageName());
        Commands.COMMANDS.clear();
        Commands.register(this, Commands.class.getPackageName());
        TeleportCommands.register();

        initialized = true;
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);

        if (initialized) {
            Ignores.save();
            Mutes.save();
        }
    }
}
