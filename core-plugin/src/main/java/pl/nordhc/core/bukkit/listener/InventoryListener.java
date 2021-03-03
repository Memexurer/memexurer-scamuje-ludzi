package pl.nordhc.core.bukkit.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import pl.nordhc.core.bukkit.gui.ClickableInventory;

public class InventoryListener implements Listener {
  @EventHandler
  public void onInventory(InventoryCloseEvent e) {
    if (e.getInventory().getHolder() instanceof ClickableInventory) {
      ((ClickableInventory) e.getInventory().getHolder()).handleClose(e);
    }
  }

  @EventHandler
  public void onInventory(InventoryClickEvent e) {
    if (e.getClickedInventory() != null && e.getClickedInventory()
        .getHolder() instanceof ClickableInventory) {
      ((ClickableInventory) e.getClickedInventory().getHolder()).handleClick(e);
    }
  }
}
