package meteordevelopment.meteoressentials.chat;

import meteordevelopment.meteoressentials.MeteorEssentials;
import meteordevelopment.nbt.NBT;
import meteordevelopment.nbt.NbtFormatException;
import meteordevelopment.nbt.tags.CompoundTag;
import meteordevelopment.nbt.tags.ListTag;
import meteordevelopment.nbt.tags.StringTag;
import meteordevelopment.nbt.tags.TagId;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Mutes {
    private static final List<UUID> MUTES = new ArrayList<>();

    public static void load() {
        MUTES.clear();

        File file = new File(MeteorEssentials.INSTANCE.getDataFolder(), "mutes.nbt");

        if (file.exists()) {
            try {
                fromTag(NBT.read(file, true).tag);
            } catch (NbtFormatException e) {
                e.printStackTrace();
            }
        }
    }

    public static void save() {
        File file = new File(MeteorEssentials.INSTANCE.getDataFolder(), "mutes.nbt");
        NBT.write(toTag(), file, true);
    }

    public static boolean addMute(OfflinePlayer player) {
        if (MUTES.contains(player.getUniqueId())) return false;

        MUTES.add(player.getUniqueId());
        save();
        return true;
    }

    public static boolean removeMute(Player player) {
        if (!MUTES.contains(player.getUniqueId())) return false;

        MUTES.remove(player.getUniqueId());
        save();
        return true;
    }

    public static boolean isMuted(Player player) {
        return MUTES.contains(player.getUniqueId());
    }

    private static CompoundTag toTag() {
        CompoundTag tag = new CompoundTag();

        ListTag<StringTag> list = new ListTag<>(TagId.String);

        for (UUID player : MUTES) list.add(new StringTag(player.toString()));

        tag.put("mutes", list);

        return tag;
    }

    private static void fromTag(CompoundTag tag) {
        for (StringTag t : tag.getList("mutes", StringTag.class)) {
            MUTES.add(UUID.fromString(t.value));
        }
    }
}
