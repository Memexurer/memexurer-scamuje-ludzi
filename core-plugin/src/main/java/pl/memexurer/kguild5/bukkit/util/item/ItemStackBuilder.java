package pl.memexurer.kguild5.bukkit.util.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemStackBuilder extends ItemBuilder<ItemStackBuilder> {
    public ItemStackBuilder(Material material) {
        super(material);
    }

    public ItemStackBuilder(ItemStack item) {
        super(item);
    }

    @Override
    protected ItemStackBuilder getThis() {
        return this;
    }
}
