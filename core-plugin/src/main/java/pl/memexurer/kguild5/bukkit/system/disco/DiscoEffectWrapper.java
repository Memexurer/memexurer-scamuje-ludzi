package pl.memexurer.kguild5.bukkit.system.disco;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import pl.memexurer.kguild5.bukkit.system.disco.effects.DiscoEffect;
import pl.memexurer.kguild5.bukkit.system.disco.effects.GrayscaleDiscoEffect;
import pl.memexurer.kguild5.bukkit.system.disco.effects.RainbowDiscoEffect;
import pl.memexurer.kguild5.bukkit.system.disco.effects.UltraDiscoEffect;

public enum DiscoEffectWrapper {
    GRAYSCALE(new GrayscaleDiscoEffect()),
    RAINBOW(new RainbowDiscoEffect()),
    ULTRA(new UltraDiscoEffect());

    private final DiscoEffect effect;
    private final ItemStack head;
    private final ItemStack chestplate;
    private final ItemStack leggins;
    private final ItemStack boots;
    private Color color;

    DiscoEffectWrapper(DiscoEffect effect) {
        this.effect = effect;

        this.head = new ItemStack(Material.LEATHER_HELMET);
        this.chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
        this.leggins = new ItemStack(Material.LEATHER_LEGGINGS);
        this.boots = new ItemStack(Material.LEATHER_BOOTS);
    }

    public Color getColor() {
        return color;
    }

    public void update() {
        this.color = effect.getColor();
        LeatherArmorMeta meta = (LeatherArmorMeta) head.getItemMeta();
        meta.setColor(effect.getColor());
        head.setItemMeta(meta);

        meta = (LeatherArmorMeta) chestplate.getItemMeta();
        meta.setColor(effect.getColor());
        chestplate.setItemMeta(meta);

        meta = (LeatherArmorMeta) leggins.getItemMeta();
        meta.setColor(effect.getColor());
        leggins.setItemMeta(meta);

        meta = (LeatherArmorMeta) boots.getItemMeta();
        meta.setColor(effect.getColor());
        boots.setItemMeta(meta);
    }

    public ItemStack getHead() {
        return head;
    }

    public ItemStack getChestplate() {
        return chestplate;
    }

    public ItemStack getLeggins() {
        return leggins;
    }

    public ItemStack getBoots() {
        return boots;
    }
}
