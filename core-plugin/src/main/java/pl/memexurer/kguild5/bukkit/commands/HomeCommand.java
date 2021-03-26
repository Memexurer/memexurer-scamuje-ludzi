package pl.memexurer.kguild5.bukkit.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Dependency;
import co.aikar.commands.annotation.Description;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import pl.memexurer.kguild5.bukkit.gui.ClickableInventory;
import pl.memexurer.kguild5.bukkit.util.ChatHelper;
import pl.memexurer.kguild5.bukkit.util.item.ItemStackBuilder;
import pl.memexurer.kguild5.bukkit.system.data.user.UserHandler;

@CommandAlias("home")
@Description("{@@cmd.home.description}")
public class HomeCommand extends BaseCommand {

  @Dependency
  private UserHandler handler;

  @Default
  public void onCommand(CommandSender sender) {
    Player player;
    new HomeInventory(sender.hasPermission("core.homevip"), handler.getPlayer((player = (Player) sender)).getHomes())
        .openInventory(player);
  }

  public static class HomeInventory implements ClickableInventory {

    private final boolean hasVip;
    private final Map<String, Location> locationMap;

    public HomeInventory(boolean hasVip, Map<String, Location> locationMap) {
      this.hasVip = hasVip;
      this.locationMap = locationMap;
    }

    @Override
    public Inventory getInventory() {
      Inventory inventory = Bukkit.createInventory(this, 9, "Domki");

      int slot = 0;
      for (Map.Entry<String, Location> locationEntry : locationMap.entrySet()) {
        inventory.setItem(slot++,
            new ItemStackBuilder(Material.PAPER)
                .withItemName("&7Domek &g" + locationEntry.getKey())
                .addLore(hasVip, "&7(kliknij aby przeteleportowac)")
                .addLore("&8\u00BB &7Lokalizacja domku: ")
                .addLore("&gX: " + locationEntry.getValue().getBlockX())
                .addLore("&gY: " + locationEntry.getValue().getBlockY())
                .addLore("&gZ: " + locationEntry.getValue().getBlockZ())
                .buildItem());
      }

      return inventory;
    }

    @Override
    public void handleClick(InventoryClickEvent e) {
      e.setCancelled(true);

      Location location = findMapEntry(locationMap, e.getRawSlot());
      if(location == null) return;

      if(hasVip) {
        e.getWhoClicked().teleport(location);
        e.getWhoClicked().sendMessage(ChatHelper.fixColor("&gPrzeteleportowano!"));
      } else {
        e.getWhoClicked().sendMessage(ChatHelper.fixColor("&gX: " + location.getBlockX() + " g"));
      }

      e.getWhoClicked().closeInventory();;
    }

    private <V> V findMapEntry(Map<?, V> map, int index) {
      for (Map.Entry<?, V> entry : map.entrySet()) {
        if (index-- == 0) {
          return entry.getValue();
        }
      }
      return null;
    }
  }

}
