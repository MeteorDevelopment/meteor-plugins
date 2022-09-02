package meteordevelopment.meteoressentials.listeners;

import meteordevelopment.meteoressentials.Utils;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;

public class Listeners {
    public static void register(JavaPlugin plugin, String packageName) {
        for (Class<? extends Listener> klass : Utils.getReflections(plugin, packageName).getSubTypesOf(Listener.class)) {
            try {
                Listener listener = klass.getDeclaredConstructor().newInstance();
                Bukkit.getPluginManager().registerEvents(listener, plugin);
            }
            catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}
