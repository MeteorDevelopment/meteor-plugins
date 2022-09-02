package meteordevelopment.meteoressentials;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Utils {
    public static double clamp(double value, double min, double max) {
        if (value < min) return min;
        return Math.min(value, max);
    }

    public static boolean isDigits(String str) {
        if (str.isEmpty()) return false;

        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    public static void setName(ItemStack itemStack, Component name) {
        ItemMeta pageItemMeta = itemStack.getItemMeta();
        pageItemMeta.displayName(name);
        itemStack.setItemMeta(pageItemMeta);
    }

    public static void fillPanes(Inventory gui) {
        for (int i = 0; i < 9; i++) {
            ItemStack itemStack = gui.getItem(i);
            if (itemStack != null && itemStack.getType() != Material.AIR) continue;

            itemStack = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
            Utils.setName(itemStack, Component.text(""));
            gui.setItem(i, itemStack);
        }
    }

    public static Reflections getReflections(JavaPlugin plugin, String packageName) {
        ConfigurationBuilder builder = new ConfigurationBuilder();

        try {
            Method getClassLoader = getMethod(plugin.getClass(), "getClassLoader");
            getClassLoader.setAccessible(true);
            builder.setUrls(ClasspathHelper.forPackage(packageName, (ClassLoader) getClassLoader.invoke(plugin)));
            getClassLoader.setAccessible(false);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        builder.filterInputsBy(new FilterBuilder().includePackage(packageName));
        builder.addScanners(Scanners.SubTypes);

        return new Reflections(builder);
    }

    public static Method getMethod(Class<?> klass, String name) {
        while (true) {
            try {
                return klass.getDeclaredMethod(name);
            } catch (NoSuchMethodException ignored) {}

            klass = klass.getSuperclass();
            if (klass == null) return null;
        }
    }

    public static String toString(Component component) {
        return PlainTextComponentSerializer.plainText().serialize(component);
    }
}
