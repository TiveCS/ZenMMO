package com.rehoukrel.zenmmo.event;

import com.rehoukrel.zenmmo.api.PlayerData;
import com.rehoukrel.zenmmo.api.Skill;
import com.rehoukrel.zenmmo.api.SkillTree;
import com.rehoukrel.zenmmo.utils.menu.UneditableMenu;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;

public class BasicEvent implements Listener{

    public static List<Player> cooldown = new ArrayList<>();


    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        cooldown.remove(event.getPlayer());
    }

    @EventHandler
    public void onPlayerCraft(CraftItemEvent event){
        if (event.isCancelled() || cooldown.contains(event.getWhoClicked())){
            return;
        }
        if (event.getWhoClicked() instanceof Player) {
            PlayerData playerData = new PlayerData((OfflinePlayer) event.getWhoClicked());
            for (SkillTree tree : playerData.getSkillTree().values()){
                tree.onPlayerCraft(event);
            }
        }
    }

    @EventHandler
    public void onPlayerBreed(EntityBreedEvent event){
        if (event.getBreeder() instanceof Player) {
            if (event.isCancelled() || cooldown.contains(event.getBreeder())) {
                return;
            }
            PlayerData pd = new PlayerData((OfflinePlayer) event.getBreeder());
            for (SkillTree tree : pd.getSkillTree().values()){
                tree.onPlayerBreed(event);
            }
        }
    }

    @EventHandler
    public void onPlayerEnchant(EnchantItemEvent event){
        if (event.isCancelled() || cooldown.contains(event.getEnchanter())){
            return;
        }
        PlayerData playerData = new PlayerData(event.getEnchanter());
        for (SkillTree tree : playerData.getSkillTree().values()){
            tree.onPlayerEnchant(event);
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event){
        Projectile proj = event.getEntity();
        if (proj.getShooter() instanceof Player){
            if (!cooldown.contains(proj.getShooter())){
                PlayerData pd = new PlayerData((OfflinePlayer) proj.getShooter());
                for (SkillTree tree : pd.getSkillTree().values()){
                    tree.onProjectileHit(event);
                }
            }
        }
        if (event.getHitEntity() instanceof Player){
            if (!cooldown.contains(event.getHitEntity())){
                PlayerData pd = new PlayerData((OfflinePlayer) event.getHitEntity());
                for (SkillTree tree : pd.getSkillTree().values()){
                    tree.onProjectileHit(event);
                }
            }
        }
    }

    @EventHandler
    public void onEntityDropItem(EntityDropItemEvent event){
        if (event.isCancelled() || cooldown.contains(event.getEntity())){
            return;
        }
        if (event.getEntity() instanceof Player) {
            PlayerData playerData = new PlayerData((OfflinePlayer) event.getEntity());
            for (SkillTree tree : playerData.getSkillTree().values()){
                tree.onEntityDropItem(event);
            }
        }
    }

    @EventHandler
    public void onPlayerFish(PlayerFishEvent event){
        if (event.isCancelled() || cooldown.contains(event.getPlayer())){
            return;
        }
        PlayerData playerData = new PlayerData(event.getPlayer());
        for (SkillTree tree : playerData.getSkillTree().values()){
            tree.onPlayerFish(event);
        }
    }

    @EventHandler
    public void onTame(EntityTameEvent event){
        if (event.getOwner() instanceof Player) {
            if (event.isCancelled() || cooldown.contains((Player) event.getOwner())){
                return;
            }
            Player tamer = (Player) event.getOwner();
            PlayerData playerData = new PlayerData(tamer);
            for (SkillTree tree : playerData.getSkillTree().values()){
                tree.onPlayerTame(event);
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        if (event.isCancelled() || cooldown.contains(event.getPlayer())){
            return;
        }
        Player breaker = event.getPlayer();
        PlayerData playerData = new PlayerData(breaker);
        for (SkillTree tree : playerData.getSkillTree().values()){
            tree.onBlockBreak(event);
        }
    }

    @EventHandler
    public void onEntityBowShoot(EntityShootBowEvent event){
        if (event.getEntity() instanceof Player){
            if (event.isCancelled() || cooldown.contains((Player) event.getEntity())){
                return;
            }
            try {
                PlayerData playerData = new PlayerData((OfflinePlayer) event.getEntity());
                for (SkillTree tree : playerData.getSkillTree().values()) {
                    tree.onEntityBowShoot(event);
                }
            }catch(Exception e){}
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event){
        PlayerData pda, pdv;
        Projectile proj;
        if (event.isCancelled() || event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK)){
            return;
        }
        if (event.getDamager() instanceof Projectile){
            proj = (Projectile) event.getDamager();
            if (proj.getShooter() instanceof Player){
                if (!cooldown.contains((Player) proj.getShooter())){
                    pda = new PlayerData((OfflinePlayer) proj.getShooter());
                    for (SkillTree tree : pda.getSkillTree().values()){
                        tree.onEntityDamage(event);
                    }
                }
            }
        }
        if (event.getDamager() instanceof Player){
            if (!cooldown.contains((Player) event.getDamager())){
                pda = new PlayerData((OfflinePlayer) event.getDamager());
                for (SkillTree tree : pda.getSkillTree().values()){
                    tree.onEntityDamage(event);
                }
            }
        }

        if (event.getEntity() instanceof Player){
            if (!cooldown.contains((Player) event.getEntity())){
                pdv = new PlayerData((OfflinePlayer) event.getEntity());
                for (SkillTree tree : pdv.getSkillTree().values()){
                    tree.onEntityDamage(event);
                }
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event){
        if (event.isCancelled()){
            return;
        }
        if (event.getEntity() instanceof Player){
            if (!cooldown.contains(event.getEntity())){
                PlayerData pd = new PlayerData((OfflinePlayer) event.getEntity());
                for (SkillTree tree : pd.getSkillTree().values()){
                    tree.onDamage(event);
                }
            }
        }
    }

    @EventHandler
    public void onEntityTarget(EntityTargetLivingEntityEvent event){
        if (event.isCancelled()){
            return;
        }
        if (event.getTarget() instanceof Player){
            if (!cooldown.contains(event.getTarget())){
                PlayerData playerData = new PlayerData((OfflinePlayer) event.getTarget());
                for (SkillTree st : playerData.getSkillTree().values()){
                    st.onEntityTarget(event);
                }
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
