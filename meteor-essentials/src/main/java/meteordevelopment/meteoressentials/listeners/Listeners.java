package meteordevelopment.meteoressentials.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginBase;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;

public class Listeners {
    public static void register(PluginBase plugin, String packageName) {
        for (Class<? extends Listener> klass : new Reflections(packageName).getSubTypesOf(Listener.class)) {
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
