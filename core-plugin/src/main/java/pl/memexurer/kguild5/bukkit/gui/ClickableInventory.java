package pl.memexurer.kguild5.bukkit.gui;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryHolder;

public interface ClickableInventory extends InventoryHolder {

    //todo add click handler listeners u know
    default void handleClick(InventoryClickEvent e) {

    }

    default void handleClose(InventoryCloseEvent e) {

    }

    default void openInventory(HumanEntity entity) {
        entity.openInventory(this.getInventory());
    }
}
