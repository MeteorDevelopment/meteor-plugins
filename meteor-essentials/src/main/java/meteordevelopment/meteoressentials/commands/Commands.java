package meteordevelopment.meteoressentials.commands;

import meteordevelopment.meteoressentials.Utils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginBase;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class Commands {
    public static final List<MeteorCommand> COMMANDS = new ArrayList<>();

    public static void register(JavaPlugin plugin, String packageName) {
        for (Class<? extends MeteorCommand> klass : Utils.getReflections(plugin, packageName).getSubTypesOf(MeteorCommand.class)) {
            try {
                MeteorCommand command = klass.getDeclaredConstructor(PluginBase.class).newInstance(plugin);
                COMMANDS.add(command);
                Bukkit.getCommandMap().register(plugin.getName(), command);
            }
            catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    public static void register(MeteorCommand command) {
        COMMANDS.add(command);
        Bukkit.getCommandMap().register(command.plugin.getName(), command);
    }
}
