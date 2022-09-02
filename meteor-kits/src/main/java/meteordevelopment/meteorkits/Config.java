package meteordevelopment.meteorkits;

import meteordevelopment.meteoressentials.Perm;
import meteordevelopment.meteoressentials.Perms;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Config {
    public static Map<String, KitLimits> LIMITS = new HashMap<>();

    public static Perm CAN_INTERACT_WITH_ALL_KITS;

    public static void load() {
        MeteorKits.INSTANCE.saveDefaultConfig();
        FileConfiguration config = MeteorKits.INSTANCE.getConfig();

        // Limits
        LIMITS.clear();
        ConfigurationSection limits = config.getConfigurationSection("limits");

        if (limits != null) {
            for (String key : limits.getKeys(false)) {
                ConfigurationSection raw = limits.getConfigurationSection(key);

                if (raw != null) {
                    String perm = raw.getString("perm", "__default__");
                    int maxCount = raw.getInt("maxCount");
                    boolean canHavePublic = raw.getBoolean("canHavePublic");

                    LIMITS.put(perm, new KitLimits(maxCount, canHavePublic));
                }
            }
        }

        // Other
        CAN_INTERACT_WITH_ALL_KITS = Perms.get(config.getString("canInteractWithAllKits", "admin"));
    }

    public static String getPrefix() {
        return meteordevelopment.meteoressentials.Config.PREFIX;
    }

    public static KitLimits getLimits(HumanEntity player) {
        int maxCount = 0;
        boolean canHavePublic = false;

        for (String perm : LIMITS.keySet()) {
            if (!perm.equals("__default__") && !player.hasPermission(perm)) continue;
            KitLimits limits = LIMITS.get(perm);

            if (limits.maxCount() > maxCount) maxCount = limits.maxCount();
            if (limits.canHavePublic()) canHavePublic = true;
        }

        return new KitLimits(maxCount, canHavePublic);
    }

    public static KitLimits getLimits(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        return player != null ? getLimits(player) : LIMITS.get("__default__");
    }
}
