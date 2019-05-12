package com.rehoukrel.zenmmo.event;

import com.rehoukrel.zenmmo.api.PlayerData;
import com.rehoukrel.zenmmo.api.SkillTree;
import com.rehoukrel.zenmmo.utils.menu.UneditableMenu;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerFishEvent;

public class BasicEvent implements Listener {

    @EventHandler
    public void onPlayerFish(PlayerFishEvent event){
        PlayerData playerData = new PlayerData(event.getPlayer());
        for (SkillTree tree : playerData.getSkillTree().values()){
            tree.onPlayerFish(event);
        }
    }

    @EventHandler
    public void onTame(EntityTameEvent event){
        if (event.getOwner() instanceof Player) {
            Player tamer = (Player) event.getOwner();
            PlayerData playerData = new PlayerData(tamer);
            for (SkillTree tree : playerData.getSkillTree().values()){
                tree.onTame(event);
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        Player breaker = event.getPlayer();
        PlayerData playerData = new PlayerData(breaker);
        for (SkillTree tree : playerData.getSkillTree().values()){
            tree.onBlockBreak(event);
        }
    }

    @EventHandler
    public void onEntityBowShoot(EntityShootBowEvent event){
        if (event.getEntity() instanceof Player){
            PlayerData playerData = (PlayerData) event.getEntity();
            for (SkillTree tree : playerData.getSkillTree().values()){
                tree.onEntityBowShoot(event);
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event){
        PlayerData pda, pdv;
        if (event.getDamager() instanceof Player){
            pda = new PlayerData((OfflinePlayer) event.getDamager());
            for (SkillTree tree : pda.getSkillTree().values()){
                tree.onEntityDamage(event);
            }
        }

        if (event.getEntity() instanceof Player){
            pdv = new PlayerData((OfflinePlayer) event.getEntity());
            for (SkillTree tree : pdv.getSkillTree().values()){
                tree.onEntityDamage(event);
            }
        }
    }

    // Menu
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
