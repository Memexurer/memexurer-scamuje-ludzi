package pl.memexurer.kguild5.bukkit.system.data.codec.converter;

import org.bson.Document;
import org.bukkit.inventory.ItemStack;
import pl.memexurer.kguild5.bukkit.system.data.codec.CodecHelper;
import pl.memexurer.kguild5.bukkit.system.data.codec.Converter;

public class ItemConverter implements Converter<ItemStack> {

  @Override
  public Document encode(ItemStack itemStack) {
    return new Document(itemStack.serialize());
  }

  @Override
  public ItemStack decode(Document document, CodecHelper helper) {
    return ItemStack.deserialize(document);
  }

  @Override
  public Class<ItemStack> getConvertedClass() {
    return ItemStack.class;
  }
}
