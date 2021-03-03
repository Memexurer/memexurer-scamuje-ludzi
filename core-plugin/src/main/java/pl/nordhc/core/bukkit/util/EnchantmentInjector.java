package pl.nordhc.core.bukkit.util;

import java.lang.reflect.Field;
import org.bukkit.enchantments.Enchantment;

public final class EnchantmentInjector {

  private EnchantmentInjector() {
  }

  public static Enchantment getEnchantment(String name, InjectableEnchantment enchantmentPromise) {
    Enchantment enchantment = Enchantment.getByName(name);
    if(enchantment != null) {
      return enchantment;
    }

    return registerEnchantment(enchantmentPromise);
  }

  private static Enchantment registerEnchantment(InjectableEnchantment injectableEnchantment) {
    try {
      Field f = Enchantment.class.getDeclaredField("acceptingNew");
      f.setAccessible(true);
      f.set(null, true);

      Enchantment enchantment = injectableEnchantment.getEnchantment(findAvailableEnchantmentId());
      Enchantment.registerEnchantment(enchantment);

      return enchantment;
    } catch (ReflectiveOperationException exception) {
      throw new RuntimeException(exception);
    }
  }

  private static int findAvailableEnchantmentId() {
    int largest = 0;
    for (Enchantment enchantment : Enchantment.values()) {
      if (enchantment.getId() > largest) {
        largest = enchantment.getId();
      }
    }

    return largest + 1;
  }

  public interface InjectableEnchantment {

    Enchantment getEnchantment(int id);
  }
}