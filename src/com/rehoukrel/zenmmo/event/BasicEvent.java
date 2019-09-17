package com.rehoukrel.zenmmo.event;

import com.rehoukrel.zenmmo.api.PlayerData;
import com.rehoukrel.zenmmo.api.SkillTree;
import com.rehoukrel.zenmmo.utils.menu.UneditableMenu;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BasicEvent implements Listener{

    public static List<Player> cooldown = new ArrayList<>();
    public static HashMap<Player, PlayerData> pds = new HashMap<>();

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        cooldown.remove(event.getPlayer());
        pds.remove(event.getPlayer());
    }

    public static PlayerData get(Player p){
        PlayerData pd = null;
        if (pds.containsKey(p)) {
            pd = pds.get(p);
            return pd;
        }else{
            pd = new PlayerData(p);
            return pds.get(p);
        }
    }

    @EventHandler
    public void onPlayerCraft(CraftItemEvent event){
        if (event.isCancelled() || cooldown.contains(event.getWhoClicked())){
            return;
        }
        if (event.getWhoClicked() instanceof Player) {
            PlayerData playerData = get((Player) event.getWhoClicked());
            if (playerData == null){return;}
            for (SkillTree tree : playerData.getSkillTree().values()){
                tree.onPlayerCraft(event);
            }
        }
    }

    @EventHandler
    public void onPlayerSprint(PlayerToggleSprintEvent event){
        if (event.isCancelled() || cooldown.contains(event.getPlayer())){
            return;
        }
        PlayerData pd = get(event.getPlayer());
        if (pd == null){return;}
        for (SkillTree s : pd.getSkillTree().values()){
            s.onPlayerSprintToggle(event);
        }
    }

    @EventHandler
    public void onPlayerSneak(PlayerToggleSneakEvent event){
        if (event.isCancelled() || cooldown.contains(event.getPlayer())){
            return;
        }
        PlayerData pd = get(event.getPlayer());
        if (pd == null){return;}
        for (SkillTree s : pd.getSkillTree().values()){
            s.onPlayerSneakToggle(event);
        }
    }

    @EventHandler
    public void onPlayerBreed(EntityBreedEvent event){
        if (event.getBreeder() instanceof Player) {
            if (event.isCancelled() || cooldown.contains(event.getBreeder())) {
                return;
            }
            PlayerData pd = get((Player) event.getBreeder());

            if (pd == null){return;}
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
        PlayerData pd = get(event.getEnchanter());
        if (pd == null){return;}
        for (SkillTree tree : pd.getSkillTree().values()){
            tree.onPlayerEnchant(event);
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event){
        Projectile proj = event.getEntity();
        if (proj.getShooter() instanceof Player){
            if (!cooldown.contains(proj.getShooter())){
                PlayerData pd = get((Player) proj.getShooter());
                if (pd != null) {
                    for (SkillTree tree : pd.getSkillTree().values()) {
                        tree.onProjectileHit(event);
                    }
                }
            }
        }
        if (event.getHitEntity() instanceof Player){
            if (!cooldown.contains(event.getHitEntity())){
                PlayerData pd = get((Player) event.getHitEntity());
                if (pd != null) {
                    for (SkillTree tree : pd.getSkillTree().values()) {
                        tree.onProjectileHit(event);
                    }
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
            PlayerData playerData = get((Player) event.getEntity());
            if (playerData == null){return;}
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
        PlayerData playerData = get(event.getPlayer());
        if (playerData == null){return;}
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
            PlayerData playerData = get(tamer);
            if (playerData == null){return;}
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
        PlayerData playerData = get(breaker);
        if (playerData == null){return;}
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
                PlayerData playerData = get((Player) event.getEntity());
                if (playerData == null){return;}
                for (SkillTree tree : playerData.getSkillTree().values()) {
                    tree.onEntityBowShoot(event);
                }
            }catch(Exception ignored){}
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
                    pda = get((Player) proj.getShooter());
                    if (pda != null) {
                        for (SkillTree tree : pda.getSkillTree().values()) {
                            tree.onEntityDamage(event);
                        }
                    }
                }
            }
        }
        if (event.getDamager() instanceof Player && event.getDamager().getType().equals(EntityType.PLAYER)){
            if (!cooldown.contains((Player) event.getDamager())){
                pda = get((Player) event.getDamager());
                if (pda != null) {
                    for (SkillTree tree : pda.getSkillTree().values()) {
                        tree.onEntityDamage(event);
                    }
                }
            }
        }

        if (event.getEntity() instanceof Player && event.getEntityType().equals(EntityType.PLAYER)){
            if (!cooldown.contains((Player) event.getEntity())){
                pdv = get((Player) event.getEntity());
                if (pdv != null) {
                    for (SkillTree tree : pdv.getSkillTree().values()) {
                        tree.onEntityDamage(event);
                    }
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
                PlayerData pd = get((Player) event.getEntity());
                if (pd == null){return;}
                for (SkillTree tree : pd.getSkillTree().values()){
                    tree.onDamage(event);
                }
            }
        }
    }

    @EventHandler
    public void onEntityHungerChange(FoodLevelChangeEvent event){
        if (event.isCancelled()){
            return;
        }
        if (event.getEntity() instanceof Player){
            if (!cooldown.contains(event.getEntity())){
                PlayerData pd = get((Player) event.getEntity());
                if (pd == null){return;}
                for (SkillTree tree : pd.getSkillTree().values()){
                    tree.onEntityHungerChange(event);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerExpChange(PlayerExpChangeEvent event){
        if (!cooldown.contains(event.getPlayer())) {
            PlayerData pd = get(event.getPlayer());
            if (pd == null){return;}
            for (SkillTree tree : pd.getSkillTree().values()) {
                tree.onPlayerExpChange(event);
            }
        }
    }

    @EventHandler
    public void onEntityPotionEffectChange(EntityPotionEffectEvent event){
        if (event.isCancelled()){
            return;
        }
        if (event.getEntity() instanceof Player){
            if (!cooldown.contains(event.getEntity())){
                PlayerData pd = get((Player) event.getEntity());
                if (pd == null){return;}
                for (SkillTree tree : pd.getSkillTree().values()){
                    tree.onEntityPotionChange(event);
                }
            }
        }
    }

    @EventHandler
    public void onEntityTarget(EntityTargetEvent event){
        if (event.isCancelled()){
            return;
        }

        if (event.getTarget() instanceof Player){
            if (!cooldown.contains(event.getTarget())){
                PlayerData playerData = get((Player) event.getTarget());
                if (playerData == null){return;}
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
                //PlayerData pd = get((Player) event.getWhoClicked());
                //pd.loadData();
            }
        }
    }

    @EventHandler
    public void onMenuClose(InventoryCloseEvent event){
        if (UneditableMenu.menus.containsKey(event.getPlayer())){
            UneditableMenu menu = UneditableMenu.menus.get(event.getPlayer());
            /*if (event.getInventory().equals(menu.getMenu())){
                PlayerData pd = get((Player) event.getPlayer());
                pd.loadData();
            }*/
        }
    }

}
