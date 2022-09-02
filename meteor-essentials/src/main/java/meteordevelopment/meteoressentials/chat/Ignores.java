package meteordevelopment.meteoressentials.chat;

import meteordevelopment.meteoressentials.MeteorEssentials;
import meteordevelopment.nbt.NBT;
import meteordevelopment.nbt.NbtFormatException;
import meteordevelopment.nbt.tags.CompoundTag;
import meteordevelopment.nbt.tags.ListTag;
import meteordevelopment.nbt.tags.StringTag;
import meteordevelopment.nbt.tags.TagId;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;

public class Ignores {
    private static final List<UUID> EMPTY = new ArrayList<>();
    private static final Map<UUID, List<UUID>> IGNORES = new HashMap<>();

    public static void load() {
        IGNORES.clear();

        File file = new File(MeteorEssentials.INSTANCE.getDataFolder(), "ignores.nbt");

        if (file.exists()) {
            try {
                fromTag(NBT.read(file, true).tag);
            } catch (NbtFormatException e) {
                e.printStackTrace();
            }
        }
    }

    public static void save() {
        File file = new File(MeteorEssentials.INSTANCE.getDataFolder(), "ignores.nbt");
        NBT.write(toTag(), file, true);
    }

    public static boolean hasReceiverIgnored(Player sender, Player receiver) {
        List<UUID> list = IGNORES.get(receiver.getUniqueId());
        if (list == null) return false;
        return list.contains(sender.getUniqueId());
    }

    // Returns true if added to ignore list
    public static boolean toggleIgnore(Player player, UUID toIgnore) {
        List<UUID> list = IGNORES.computeIfAbsent(player.getUniqueId(), k -> new ArrayList<>());
        if (!list.remove(toIgnore)) {
            list.add(toIgnore);
            return true;
        }
        return false;
    }

    public static List<UUID> getIgnores(Player player) {
        return IGNORES.getOrDefault(player.getUniqueId(), EMPTY);
    }

    private static CompoundTag toTag() {
        CompoundTag tag = new CompoundTag();

        for (UUID player : IGNORES.keySet()) {
            ListTag<StringTag> list = new ListTag<>(TagId.String);

            for (UUID ignored : IGNORES.get(player)) {
                list.add(new StringTag(ignored.toString()));
            }

            tag.put(player.toString(), list);
        }

        return tag;
    }

    private static void fromTag(CompoundTag tag) {
        for (String key : tag.keySet()) {
            UUID player = UUID.fromString(key);
            List<UUID> list = new ArrayList<>(tag.keySet().size());

            for (StringTag t : tag.getList(key, StringTag.class)) {
                list.add(UUID.fromString(t.value));
            }

            IGNORES.put(player, list);
        }
    }
}
