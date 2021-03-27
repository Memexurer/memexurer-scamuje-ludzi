package pl.memexurer.kguild5.bukkit.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Dependency;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.IntStream;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import pl.memexurer.kguild5.bukkit.gui.ClickableInventory;
import pl.memexurer.kguild5.bukkit.system.data.user.UserBackup;
import pl.memexurer.kguild5.bukkit.system.data.user.UserDataModel;
import pl.memexurer.kguild5.bukkit.system.data.user.UserHandler;
import pl.memexurer.kguild5.bukkit.util.item.ItemStackBuilder;

@CommandAlias("backup")
@CommandPermission("core.backup")
@Description("{@@cmd.backup.description}")
public class BackupCommand extends BaseCommand {

  private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  @Dependency
  private UserHandler handler;

  @Subcommand("restore")
  @Description("{@@cmd.backup.restore.description}")
  public void restoreBackup(CommandSender sender, Player argumentPlayer) {
    Player viewer = (Player) sender;

    UserDataModel dataModel;
    if (argumentPlayer == null || (dataModel = handler.getPlayer(argumentPlayer)) == null) {
      sender.sendMessage("Nie znaleziono gracza!");
      return;
    }

    viewer.openInventory(
        new BackupInventory(dataModel.getBackupList(), argumentPlayer).getInventory());
  }

  @Subcommand("create")
  @Description("{@@cmd.backup.create.description}")
  public void createBackup(CommandSender sender, Player argumentPlayer) {

    UserDataModel dataModel;
    if (argumentPlayer == null || (dataModel = handler.getPlayer(argumentPlayer)) == null) {
      sender.sendMessage("Nie znaleziono gracza!");
      return;
    }

    dataModel.createBackup(argumentPlayer);
    sender.sendMessage("Backup zostal utworzony!");
  }

  public static class BackupInventory implements ClickableInventory {

    private final List<UserBackup> backupList;
    private final Player player;

    public BackupInventory(List<UserBackup> backupList, Player player) {
      this.backupList = backupList;
      this.player = player;
    }

    @Override
    public Inventory getInventory() {
      Inventory inventory = Bukkit.createInventory(this, 9, "Backup gracza " + player.getName());

      int i = 0;
      for (UserBackup backup : backupList) {
        inventory.setItem(i++,
            new ItemStackBuilder(Material.PAPER)
                .withItemName("Backup z " + DATE_FORMAT.format(backup.getBackupDate()))
                .withLore("LPM - przywroc backup", "PPM - pokaz zawartosc")
                .buildItem()
        );
      }
      return inventory;
    }

    @Override
    public void handleClick(InventoryClickEvent e) {
      e.setCancelled(true);

      if (e.getRawSlot() >= backupList.size()) {
        return;
      }

      UserBackup currentBackup = backupList.get(e.getRawSlot());
      if (e.isLeftClick()) {
        new BackupConfirmInventory(player, currentBackup, this).openInventory(e.getWhoClicked());
      } else if (e.isRightClick()) {
        new BackupContentViewInventory(currentBackup, this).openInventory(e.getWhoClicked());
      }
    }
  }

  public static class BackupContentViewInventory implements ClickableInventory {

    private final UserBackup playerBackup;
    private final ClickableInventory previousMenu;

    public BackupContentViewInventory(UserBackup playerBackup, ClickableInventory previousMenu) {
      this.playerBackup = playerBackup;
      this.previousMenu = previousMenu;
    }

    @Override
    public Inventory getInventory() {
      Inventory inventory = Bukkit.createInventory(this, 45, "Zawartosc backupu");

      IntStream.range(0, 36)
          .forEach(i -> inventory.setItem(35 - i, playerBackup.getInventoryContents()[i]));
      IntStream.range(0, 4)
          .forEach(i -> inventory.setItem(39 - i, playerBackup.getArmorContents()[i]));

      ItemStack blackPane = new ItemStackBuilder(Material.STAINED_GLASS_PANE)
          .withColor(DyeColor.BLACK)
          .withItemName("chuj").buildItem();
      IntStream.range(41, 45).forEach(i -> inventory.setItem(i, blackPane));

      inventory
          .setItem(40, new ItemStackBuilder(Material.BARRIER).withItemName("Powrót").buildItem());

      return inventory;
    }

    @Override
    public void handleClick(InventoryClickEvent e) {
      e.setCancelled(true);

      if (e.getRawSlot() == 40) {
        previousMenu.openInventory(e.getWhoClicked());
      }
    }
  }

  public static class BackupConfirmInventory implements ClickableInventory {

    private final Player player;
    private final UserBackup backup;
    private final ClickableInventory previousMenu;

    public BackupConfirmInventory(Player player, UserBackup backup,
        ClickableInventory previousMenu) {
      this.player = player;
      this.backup = backup;
      this.previousMenu = previousMenu;
    }

    @Override
    public Inventory getInventory() {
      Inventory inventory = Bukkit
          .createInventory(this, InventoryType.HOPPER, "Backup gracza " + player.getName());

      inventory.setItem(0, new ItemStackBuilder(Material.STAINED_GLASS_PANE)
          .withColor(DyeColor.LIME)
          .withItemName("Przywroc backup")
          .buildItem());

      inventory.setItem(4, new ItemStackBuilder(Material.STAINED_GLASS_PANE)
          .withColor(DyeColor.RED)
          .withItemName("Anuluj (powrot do poprzedniego menu)")
          .buildItem());

      return inventory;
    }

    @Override
    public void handleClick(InventoryClickEvent e) {
      e.setCancelled(true);

      if (e.getRawSlot() == 0) {
        backup.restoreBackup(player);
        e.getWhoClicked().sendMessage("Backup zostal przywrocony!");
        e.getWhoClicked().closeInventory();
      } else if (e.getRawSlot() == 4) {
        previousMenu.openInventory(e.getWhoClicked());
      }
    }
  }
}
