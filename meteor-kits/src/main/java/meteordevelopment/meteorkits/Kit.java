package meteordevelopment.meteorkits;

import meteordevelopment.nbt.NbtWriter;
import meteordevelopment.nbt.tags.CompoundTag;
import meteordevelopment.nbt.tags.TagId;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Set;
import java.util.UUID;

public class Kit {
    private static final Set<Material> BANNED_ITEMS = Set.of(
            Material.ELYTRA,
            Material.SHIELD,

            Material.REDSTONE_BLOCK,
            Material.PISTON,
            Material.STICKY_PISTON,

            Material.TNT
    );

    public String name;
    public UUID author;
    public boolean isPublic;
    public ItemStack[] items;

    public Kit(String name, Player player) {
        this.name = name;
        this.author = player.getUniqueId();
        this.items = new ItemStack[42];

        for (int i = 0; i < items.length; i++) {
            ItemStack itemStack = i == 41 ? player.getInventory().getItemInOffHand() : player.getInventory().getContents()[i];

            if (isBanned(itemStack)) {
                if (i == 41) player.getInventory().setItemInOffHand(new ItemStack(Material.AIR));
                else player.getInventory().setItem(i, new ItemStack(Material.AIR));
            }
            else {
                items[i] = new ItemStack(itemStack);
            }
        }
    }

    public Kit(CompoundTag tag) {
        fromTag(tag);
    }

    private boolean isBanned(ItemStack itemStack) {
        if (itemStack == null) return true;

        // Banned item list
        if (BANNED_ITEMS.contains(itemStack.getType())) return true;

        // Stack size
        if (itemStack.getMaxStackSize() != -1 && itemStack.getAmount() > itemStack.getMaxStackSize()) return true;

        // Enchantment level
        for (Enchantment enchantment : itemStack.getEnchantments().keySet()) {
            if (itemStack.getEnchantments().get(enchantment) > enchantment.getMaxLevel()) return true;
        }

        return false;
    }

    public void apply(HumanEntity player) {
        player.getInventory().clear();

        for (int i = 0; i < items.length; i++) {
            if (items[i] == null) continue;

            if (i == 41) player.getInventory().setItemInOffHand(new ItemStack(items[i]));
            else player.getInventory().setItem(i, new ItemStack(items[i]));
        }
    }

    // Serialization

    public void toTag(NbtWriter nbt) {
        nbt.writeCompoundStart();

        nbt.writeString("name", name);
        nbt.writeString("author", author.toString());
        nbt.writeBool("isPublic", isPublic);

        int itemCount = 0;
        for (ItemStack itemStack : items) {
            if (itemStack != null) itemCount++;
        }

        nbt.writeList("items", TagId.Compound, itemCount);
        for (int i = 0; i < items.length; i++) {
            ItemStack itemStack = items[i];
            if (itemStack == null) continue;

            nbt.writeCompoundStart();
            nbt.writeInt("slot", i);
            ItemStackSerializer.toNbt(nbt, itemStack);

            nbt.writeCompoundEnd();
        }

        nbt.writeCompoundEnd();
    }

    public void fromTag(CompoundTag tag) {
        name = tag.getString("name");
        author = UUID.fromString(tag.getString("author"));
        isPublic = tag.getBool("isPublic");

        items = new ItemStack[42];
        for (CompoundTag t : tag.getList("items", CompoundTag.class)) {
            items[t.getInt("slot")] = ItemStackSerializer.fromNbt(t);
        }
    }
}
