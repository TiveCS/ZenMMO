package com.rehoukrel.zenmmo.api.skilltree;

import com.rehoukrel.zenmmo.api.SkillTree;
import com.rehoukrel.zenmmo.api.skill.aquatic.Breathing;
import com.rehoukrel.zenmmo.api.skill.aquatic.Fishing;
import com.rehoukrel.zenmmo.api.skill.aquatic.Mobility;
import com.rehoukrel.zenmmo.api.skill.aquatic.WaterHunting;
import com.rehoukrel.zenmmo.utils.DataConverter;
import com.rehoukrel.zenmmo.utils.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Aquatic extends SkillTree {

    List<ItemStack> TREASURE = Arrays.asList(
            XMaterial.DIAMOND.parseItem(), XMaterial.DIAMOND_BLOCK.parseItem(), XMaterial.BOOK.parseItem(),
            XMaterial.EMERALD.parseItem(), XMaterial.LAPIS_LAZULI.parseItem(), XMaterial.WOODEN_AXE.parseItem(), XMaterial.LEAD.parseItem()
    );

    List<Player> cd = new ArrayList<>();
    List<Player> visCd = new ArrayList<>();

    public Aquatic() {
        super("Aquatic", XMaterial.FISHING_ROD.parseMaterial(), Arrays.asList("&fMostly for aquatic activity", "&fand also for oceanic things"));

        setUseIncrement(true);

        addSkill(new Breathing(), new Fishing(), new Mobility(), new WaterHunting());
        loadIcon();
    }

    @Override
    public void onEntityTarget(EntityTargetEvent event) {
        WaterHunting waterHunting = (WaterHunting) getSkills().get("WaterHunting");
        if (waterHunting.getLevel() > 0) {
            int hide = (int) Math.round(waterHunting.getAttribute().get("hide"));
            Player p = (Player) event.getTarget();
            if (p.getWorld().getBlockAt(p.getEyeLocation()).isLiquid()) {
                if (waterHunting.getLevel() >= hide) {
                    event.setTarget(null);
                }
            }
        }
    }

    @Override
    public void onPlayerSprintToggle(PlayerToggleSprintEvent event) {
        Player p = event.getPlayer();

        WaterHunting waterHunting = (WaterHunting) getSkills().get("WaterHunting");
        if (waterHunting.getLevel() > 0){
            final int vision = (int) Math.round(waterHunting.getAttribute().get("vision")*waterHunting.getLevel())*20;
            if (!visCd.contains(p) && (p.getWorld().getBlockAt(p.getLocation()).isLiquid())){
                visCd.add(p);
                p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, vision, 0), true);
                Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                    @Override
                    public void run() {
                        visCd.remove(p);
                    }
                }, vision + 60);
            }
        }
    }

    @Override
    public void onPlayerSneakToggle(PlayerToggleSneakEvent event) {
        Mobility mobility = (Mobility) getSkills().get("Mobility");
        int level = mobility.getLevel();
        if (level > 0 && !cd.contains(event.getPlayer()) && event.getPlayer().getWorld().getBlockAt(event.getPlayer().getLocation()).isLiquid()) {
            int efficiencyDuration = (((int) Math.round(mobility.getAttribute().get("efficiency-duration")))*20)*level,
                    efficiency = (int) Math.round(mobility.getAttribute().get("efficiency"))*level;
            event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, efficiencyDuration, efficiency), true);
            cd.add(event.getPlayer());
            Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    cd.remove(event.getPlayer());
                }
            }, efficiencyDuration + 40);
        }
    }

    @Override
    public void onDamage(EntityDamageEvent event) {
        if (event.getCause().equals(EntityDamageEvent.DamageCause.DROWNING)){
            Player p = (Player) event.getEntity();
            addExp(5);
            startCooldown(30);
            showProgress(p);
            Breathing breathing = (Breathing) getSkills().get("Breathing");
            if (DataConverter.chance(breathing.getAttribute().get("oxygen")*breathing.getLevel())) {
                int max = p.getMaximumAir(), curr = p.getRemainingAir();
                int result = curr + ((int) (Math.round(max / 3)));
                if (result > max) {
                    result = max;
                }
                p.setRemainingAir(result);
            }
        }
    }

    @Override
    public void onPlayerFish(PlayerFishEvent event) {
        if (event.getState().equals(PlayerFishEvent.State.CAUGHT_FISH)) {
            Fishing fishing = (Fishing) this.getSkills().get("Fishing");
            double expbonus = fishing.getAttribute().get("exp-bonus") * fishing.getLevel(), treasurer = fishing.getAttribute().get("treasurer") * fishing.getLevel();
            if (DataConverter.chance(treasurer)) {
                ItemStack i = TREASURE.get(new Random().nextInt(TREASURE.size() - 1));
                event.getHook().getLocation().getWorld().dropItemNaturally(event.getHook().getLocation(), i);
            }
            int exp = event.getExpToDrop(), bonus = (int) Math.round(exp+fishing.getLevel() * expbonus / 100);
            int result = exp + bonus;
            event.setExpToDrop(result);
            event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8(&a+&8) &b" + bonus + " &7xp"));
            addExp(new Random().nextInt(15));
            startCooldown(140);
            showProgress(event.getPlayer());
        }
    }

    @Override
    public void onEntityBowShoot(EntityShootBowEvent event) {
        if (event.getProjectile() instanceof Arrow){
            WaterHunting waterhunt = (WaterHunting) this.getSkills().get("WaterHunting");
            if (waterhunt.getLevel() >= waterhunt.getAttribute().get("water-arrow")) {
                Arrow proj = (Arrow) event.getProjectile();
                Player shooter = (Player) proj.getShooter();
                org.bukkit.util.Vector vel = proj.getVelocity().clone();
                vel = vel.multiply(3);
                org.bukkit.util.Vector finalVel = vel;
                if (shooter.getWorld().getBlockAt(shooter.getEyeLocation()).isLiquid()){
                    proj.setVelocity(finalVel);
                }
            }

        }
    }
}
