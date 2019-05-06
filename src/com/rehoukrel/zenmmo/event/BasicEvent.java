package com.rehoukrel.zenmmo.event;

import com.rehoukrel.zenmmo.utils.menu.UneditableMenu;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class BasicEvent implements Listener {

    @EventHandler
    public void onMenuClick(InventoryClickEvent event){
        if (UneditableMenu.menus.containsKey(event.getWhoClicked())){
            UneditableMenu menu = UneditableMenu.menus.get(event.getWhoClicked());
            if (event.getClickedInventory().equals(menu.getMenu())){
                event.setCancelled(true);
                menu.actionClick(event);
            }
        }
    }

}
