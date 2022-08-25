package meteordevelopment.meteoressentials;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Config {
    public static String PREFIX;

    public static String JOIN_MESSAGE;
    public static String LEAVE_MESSAGE;

    public static int MIN_CRYSTAL_AGE;

    public static List<String> DISABLED_COMMANDS;

    public static Map<String, String> TELEPORT_COMMANDS;

    public static void load() {
        MeteorEssentials.INSTANCE.saveDefaultConfig();
        FileConfiguration config = MeteorEssentials.INSTANCE.getConfig();

        PREFIX = ChatColor.translateAlternateColorCodes('&', config.getString("prefix", ""));

        JOIN_MESSAGE = ChatColor.translateAlternateColorCodes('&', config.getString("join_message", ""));
        LEAVE_MESSAGE = ChatColor.translateAlternateColorCodes('&', config.getString("leave_message", ""));

        MIN_CRYSTAL_AGE = config.getInt("min_crystal_age", 0);

        DISABLED_COMMANDS = config.getStringList("disabled_commands");

        // Teleport commands
        TELEPORT_COMMANDS = new HashMap<>();
        ConfigurationSection section = config.getConfigurationSection("teleport_commands");

        if (section != null) {
            for (String key : section.getKeys(false)) {
                TELEPORT_COMMANDS.put(key, section.getString(key));
            }
        }
    }
}
