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

        Perm perm = PERMS.get(fullName);
        if (perm == null) {
            perm = new Perm(new Permission(fullName));

            Bukkit.getPluginManager().addPermission(perm.permission());
            PERMS.put(fullName, perm);
        }

        return perm;
    }
}
