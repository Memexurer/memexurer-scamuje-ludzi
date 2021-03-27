package pl.memexurer.kguild5.bukkit.system.data.user;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import org.bson.Document;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.memexurer.kguild5.bukkit.system.data.codec.CodecHelper;
import pl.memexurer.kguild5.bukkit.system.data.codec.Converter;

public class UserBackup {

  private final long backupTime;
  private final ItemStack[] armorContents;
  private final ItemStack[] inventoryContents;

  private UserBackup(long backupTime, ItemStack[] armorContents, ItemStack[] inventoryContents) {
    this.backupTime = backupTime;
    this.armorContents = armorContents;
    this.inventoryContents = inventoryContents;
  }

  public static UserBackup createBackup(Player player) {
    return new UserBackup(System.currentTimeMillis(), player.getInventory().getArmorContents(),
        player.getInventory().getContents());
  }

  public Date getBackupDate() {
    return new Date(backupTime);
  }

  public void restoreBackup(Player player) {
    player.getInventory().setContents(inventoryContents);
    player.getInventory().setArmorContents(armorContents);
  }

  public ItemStack[] getArmorContents() {
    return armorContents;
  }

  public ItemStack[] getInventoryContents() {
    return inventoryContents;
  }

  public static class BackupConverter implements Converter<UserBackup> {

    @Override
    public Document encode(UserBackup userBackup) {
      Document document = new Document();
      document.put("time", userBackup.backupTime);
      document.put("armor", Arrays.asList(userBackup.armorContents));
      document.put("inventory", Arrays.asList(userBackup.inventoryContents));
      return document;
    }

    @Override
    public UserBackup decode(Document document, CodecHelper helper) {
      return new UserBackup(
          document.getLong("time"),
          helper.reinterpretList(document.get("armor", Collections.emptyList()), ItemStack.class)
              .toArray(new ItemStack[0]),
          helper
              .reinterpretList(document.get("inventory", Collections.emptyList()), ItemStack.class)
              .toArray(new ItemStack[0])
      );
    }

    @Override
    public Class<UserBackup> getConvertedClass() {
      return UserBackup.class;
    }
  }
}
