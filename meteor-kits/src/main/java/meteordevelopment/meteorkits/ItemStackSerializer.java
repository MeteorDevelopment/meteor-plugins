package meteordevelopment.meteorkits;

import meteordevelopment.nbt.NbtWriter;
import meteordevelopment.nbt.tags.CompoundTag;
import meteordevelopment.nbt.tags.ListTag;
import meteordevelopment.nbt.tags.StringTag;
import meteordevelopment.nbt.tags.TagId;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.BlockState;
import org.bukkit.block.ShulkerBox;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.List;
import java.util.Map;

public class ItemStackSerializer {
    public static void toNbt(NbtWriter nbt, ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();

        nbt.writeString("id", itemStack.getType().name());
        nbt.writeInt("count", itemStack.getAmount());

        // Enchantments
        Map<Enchantment, Integer> enchantments = itemStack.getEnchantments();
        if (enchantments.size() > 0) {
            nbt.writeList("enchantemets", TagId.Compound, enchantments.size());

            for (Enchantment en : enchantments.keySet()) {
                nbt.writeCompoundStart();

                tagToNbt(nbt, "id", en.getKey());
                nbt.writeInt("level", enchantments.get(en));

                nbt.writeCompoundEnd();
            }
        }

        // Tipped Arrows / Potions
        if (itemMeta instanceof PotionMeta meta) {
            PotionData data = meta.getBasePotionData();
            nbt.writeString("potion", data.getType().name());
            nbt.writeBool("extended", data.isExtended());
            nbt.writeBool("upgraded", data.isUpgraded());

            List<PotionEffect> effects = meta.getCustomEffects();
            if (effects.size() > 0) {
                nbt.writeList("effects", TagId.Compound, effects.size());

                for (PotionEffect potionEffect : effects) {
                    nbt.writeCompoundStart();

                    nbt.writeString("id", potionEffect.getType().getName());
                    nbt.writeInt("duration", potionEffect.getDuration());
                    nbt.writeInt("amplifier", potionEffect.getAmplifier());

                    nbt.writeCompoundEnd();
                }
            }
        }

        // Shulker Boxes
        if (itemMeta instanceof BlockStateMeta meta) {
            BlockState blockState = meta.getBlockState();

            if (blockState instanceof ShulkerBox state) {
                int itemCount = 0;
                for (ItemStack stack : state.getInventory()) {
                    if (stack != null && stack.getType() != Material.AIR) itemCount++;
                }

                nbt.writeList("items", TagId.Compound, itemCount);

                for (int i = 0; i < state.getInventory().getSize(); i++) {
                    ItemStack item = state.getInventory().getItem(i);
                    if (item == null || item.getType() == Material.AIR) continue;

                    nbt.writeCompoundStart();

                    nbt.writeInt("slot", i);
                    toNbt(nbt, item);

                    nbt.writeCompoundEnd();
                }
            }
        }
    }

    public static ItemStack fromNbt(CompoundTag tag) {
        Material type = Material.getMaterial(tag.getString("id"));
        if (type == null) return null;

        ItemStack itemStack = new ItemStack(type, tag.getInt("count"));

        // Enchantements
        ListTag<CompoundTag> e = tag.getList("enchantemets", CompoundTag.class);
        if (e != null) {
            for (CompoundTag t : e) {
                Enchantment en = Enchantment.getByKey(tagFromNbt(t.getList("id", StringTag.class)));
                if (en != null) itemStack.addUnsafeEnchantment(en, t.getInt("level"));
            }
        }

        // Tipped Arrows / Potions
        ItemMeta itemMeta = itemStack.getItemMeta();

        if (itemMeta instanceof PotionMeta meta) {
            try {
                PotionType ptype = PotionType.valueOf(tag.getString("potion"));
                meta.setBasePotionData(new PotionData(ptype, tag.getBool("extended"), tag.getBool("upgraded")));
            } catch (IllegalArgumentException ignored) {}

            ListTag<CompoundTag> p = tag.getList("effects", CompoundTag.class);
            if (p != null) {
                for (CompoundTag t : p) {
                    PotionEffectType petype = PotionEffectType.getByName(t.getString("id"));
                    if (petype != null) {
                        meta.addCustomEffect(new PotionEffect(petype, t.getInt("duration"), t.getInt("amplifier")), true);
                    }
                }
            }
        }

        // Shulker Boxes
        else if (itemMeta instanceof BlockStateMeta meta) {
            BlockState blockState = meta.getBlockState();

            if (blockState instanceof ShulkerBox state) {
                ListTag<CompoundTag> l = tag.getList("items", CompoundTag.class);

                for (CompoundTag t : l) {
                    state.getInventory().setItem(t.getInt("slot"), fromNbt(t));
                }

                meta.setBlockState(blockState);
            }
        }

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private static void tagToNbt(NbtWriter nbt, String name, NamespacedKey key) {
        nbt.writeList(name, TagId.String, 2);
        nbt.writeString(key.getNamespace());
        nbt.writeString(key.getKey());
    }

    private static NamespacedKey tagFromNbt(ListTag<StringTag> tag) {
        return new NamespacedKey(tag.get(0).value, tag.get(1).value);
    }
}
