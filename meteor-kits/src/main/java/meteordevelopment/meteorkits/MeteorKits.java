package meteordevelopment.meteorkits;

import meteordevelopment.meteoressentials.commands.Commands;
import meteordevelopment.meteoressentials.listeners.Listeners;
import meteordevelopment.meteorkits.commands.CreateKitCommand;
import meteordevelopment.meteorkits.listeners.GuiListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class MeteorKits extends JavaPlugin {
    public static MeteorKits INSTANCE;

    private boolean initialized;

    @Override
    public void onEnable() {
        INSTANCE = this;
        initialized = false;

        Config.load();
        Kits.load();

        Listeners.register(this, GuiListener.class.getPackageName());
        Commands.register(this, CreateKitCommand.class.getPackageName());

        initialized = true;
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);

        if (initialized) {
            Kits.save();
        }
    }
}
