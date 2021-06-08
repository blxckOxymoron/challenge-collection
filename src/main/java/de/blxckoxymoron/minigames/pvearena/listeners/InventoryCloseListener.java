package de.blxckoxymoron.minigames.pvearena.listeners;

import de.blxckoxymoron.minigames.pvearena.Arena;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryCloseListener implements Listener {

    @EventHandler
    public static void onInventoryClose(InventoryCloseEvent e) {
        if (e.getView().getTitle().startsWith(Arena.INVENTORY_PREFIX)) {
            Arena.updateInventories();
        }
    }
}
