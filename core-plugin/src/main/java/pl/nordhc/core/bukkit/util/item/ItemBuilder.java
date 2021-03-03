package pl.nordhc.core.bukkit.util.item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.Validate;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import pl.nordhc.core.bukkit.util.ChatHelper;
import pl.nordhc.core.bukkit.util.EnchantmentInjector;

public abstract class ItemBuilder<T extends ItemBuilder<T>> {

  private static final Enchantment INVIS_ENCHANTMENT;

  static {
    INVIS_ENCHANTMENT = EnchantmentInjector.getEnchantment("Invisible", InvisibleEnchantment::new);
  }


  private final ItemMeta meta;
  private final ItemStack itemStack;

  public ItemBuilder(Material material) {
    this(new ItemStack(material, 1));
  }

  public ItemBuilder(ItemStack item) {
    this.itemStack = new ItemStack(item);
    this.meta = item.getItemMeta();
  }

  protected abstract T getThis();

  public T withItemName(String itemName) {
    meta.setDisplayName(ChatColor.GRAY + ChatHelper.fixColor(itemName));
    return getThis();
  }

  public T withLore(List<String> lore) {
    meta.setLore(lore.stream().map(ChatHelper::fixColor).collect(Collectors.toList()));
    return getThis();
  }

  public T withLore(String... lore) {
    return withLore(Arrays.asList(lore));
  }

  public T withAmount(int amount) {
    itemStack.setAmount(amount);
    return getThis();
  }

  public T withItemData(short data) {
    itemStack.setDurability(data);
    return getThis();
  }

  public T withColor(DyeColor color) {
    if (itemStack.getType() == Material.INK_SACK) {
      return withItemData(color.getDyeData());
    } else {
      return withItemData(color.getWoolData());
    }
  }

  public T withEnchantment(boolean enabled) {
    if (!enabled) {
      return getThis();
    }

    meta.addEnchant(INVIS_ENCHANTMENT, 1, false);
    return getThis();
  }

  public T withSkullOwner(String ownerName) {
    Validate.isTrue(meta instanceof SkullMeta, "Not an skull.");
    if (itemStack.getDurability() != 3) {
      itemStack.setDurability((short) 3);
    }

    ((SkullMeta) this.meta).setOwner(ownerName);
    return getThis();
  }

  public ItemStack buildItem() {
    itemStack.setItemMeta(meta);
    return itemStack;
  }

  public T addLore(String s) {
    List<String> lore = meta.getLore();
    if (lore == null) {
      meta.setLore((lore = new ArrayList<>()));
    }
    lore.add(s);

    return withLore(lore);
  }

  public T addLore(boolean b, String s) {
    if (b) {
      addLore(s);
    }
    return getThis();
  }

  public T addLore(List<String> lore) {
    List<String> lor = meta.getLore();
    if (lor == null) {
      meta.setLore(lor = new ArrayList<>(lore));
    } else {
      lor.addAll(lore);
    }

    return withLore(lor);
  }

  public static class InvisibleEnchantment extends Enchantment {

    public InvisibleEnchantment(int id) {
      super(id);
    }

    @Override
    public String getName() {
      return "Invisible";
    }

    @Override
    public int getMaxLevel() {
      return 0;
    }

    @Override
    public int getStartLevel() {
      return 0;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
      return null;
    }

    @Override
    public boolean conflictsWith(Enchantment enchantment) {
      return false;
    }

    @Override
    public boolean canEnchantItem(ItemStack itemStack) {
      return false;
    }
  }
}
