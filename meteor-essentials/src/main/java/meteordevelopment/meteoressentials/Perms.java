package meteordevelopment.meteoressentials;

import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginBase;

import java.util.HashMap;
import java.util.Map;

public class Perms {
    private static final Map<String, Perm> PERMS = new HashMap<>();

    public static Perm get(PluginBase plugin, String name) {
        String fullName = plugin.getName() + "." + name;
        return get(name);
    }

    public static Perm get(String name) {
        Perm perm = PERMS.get(name);
        if (perm == null) {
            perm = new Perm(new Permission(name));

            Bukkit.getPluginManager().addPermission(perm.permission());
            PERMS.put(name, perm);
        }

        return perm;
    }
}
