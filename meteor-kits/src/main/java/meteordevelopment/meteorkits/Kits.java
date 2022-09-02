package meteordevelopment.meteorkits;

import meteordevelopment.nbt.NBT;
import meteordevelopment.nbt.NbtFormatException;
import meteordevelopment.nbt.NbtWriter;
import meteordevelopment.nbt.tags.CompoundTag;
import meteordevelopment.nbt.tags.TagId;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.text.Collator;
import java.util.*;

public class Kits {
    private static final List<Kit> EMPTY_LIST = new ArrayList<>(0);

    private static File file;

    private static final Map<String, Kit> kits = new HashMap<>();
    private static final Map<UUID, List<Kit>> playerKits = new HashMap<>();
    private static final List<Kit> publicKits = new ArrayList<>();

    public static long modifiedTimestamp = 0;

    public static void load() {
        file = new File(MeteorKits.INSTANCE.getDataFolder(), "kits.nbt");

        if (file.exists()) {
            try {
                long timestamp = System.nanoTime();

                fromTag(NBT.read(file, true).tag);

                double msDelta = (System.nanoTime() - timestamp) / 1000000.0;
                MeteorKits.INSTANCE.getLogger().info(String.format("Loaded %s kits in %.3f milliseconds", kits.size(), msDelta));
            } catch (NbtFormatException e) {
                e.printStackTrace();
            }
        }
    }

    public static void save() {
        try {
            long timestamp = System.nanoTime();

            NbtWriter nbt = new NbtWriter("", file, true);
            toTag(nbt);

            nbt.close();
            modifiedTimestamp = 0;

            double msDelta = (System.nanoTime() - timestamp) / 1000000.0;
            MeteorKits.INSTANCE.getLogger().info(String.format("Saved %s kits in %.3f milliseconds%n", kits.size(), msDelta));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void changed() {
        if (modifiedTimestamp == 0) modifiedTimestamp = System.currentTimeMillis();
    }

    private static void add(Kit kit, boolean save) {
        synchronized (kits) {
            kits.put(kit.name, kit);
            playerKits.computeIfAbsent(kit.author, uuid -> new ArrayList<>(1)).add(kit);

            if (kit.isPublic) {
                publicKits.add(kit);
                publicKits.sort((o1, o2) -> Collator.getInstance().compare(o1.name, o2.name));
            }

            if (save) changed();
        }
    }

    public static void add(Kit kit) {
        add(kit, true);
    }

    public static Kit get(String name) {
        return kits.get(name);
    }

    public static List<Kit> getKits(UUID player) {
        List<Kit> kits = playerKits.get(player);
        return kits != null ? kits : EMPTY_LIST;
    }

    public static List<Kit> getKits(HumanEntity player) {
        return getKits(player.getUniqueId());
    }

    public static List<Kit> getPublicKits() {
        return publicKits;
    }

    public static boolean remove(String name) {
        synchronized (kits) {
            Kit kit = kits.remove(name);
            if (kit != null) {
                playerKits.get(kit.author).remove(kit);
                publicKits.remove(kit);
                publicKits.sort((o1, o2) -> Collator.getInstance().compare(o1.name, o2.name));

                changed();
                return true;
            }
        }

        return false;
    }

    public static boolean remove(Player player, String name) {
        synchronized (kits) {
            List<Kit> kits = playerKits.get(player.getUniqueId());
            if (kits != null) {
                for (Kit kit : kits) {
                    if (kit.name.equals(name)) {
                        kits.remove(kit);
                        Kits.kits.remove(name);
                        publicKits.remove(kit);
                        publicKits.sort((o1, o2) -> Collator.getInstance().compare(o1.name, o2.name));

                        changed();
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public static void addPublic(Kit kit) {
        publicKits.add(kit);
        publicKits.sort((o1, o2) -> Collator.getInstance().compare(o1.name, o2.name));

        changed();
    }

    public static void removePublic(Kit kit) {
        if (publicKits.remove(kit)) {
            publicKits.sort((o1, o2) -> Collator.getInstance().compare(o1.name, o2.name));
            changed();
        }
    }

    // Serialization

    public static void toTag(NbtWriter nbt) {
        synchronized (kits) {
            nbt.writeList("kits", TagId.Compound, kits.size());
            for (Kit kit : kits.values()) kit.toTag(nbt);
        }
    }

    public static void fromTag(CompoundTag tag) {
        synchronized (kits) {
            kits.clear();
            playerKits.clear();
            publicKits.clear();

            for (CompoundTag t : tag.getList("kits", CompoundTag.class)) {
                add(new Kit(t), false);
            }
        }
    }
}
