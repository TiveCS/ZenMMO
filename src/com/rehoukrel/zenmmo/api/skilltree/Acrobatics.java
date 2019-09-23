package com.rehoukrel.zenmmo.api.skilltree;

import com.rehoukrel.zenmmo.utils.ParticleManager;
import com.rehoukrel.zenmmo.api.SkillTree;
import com.rehoukrel.zenmmo.api.skill.acrobatics.Agility;
import com.rehoukrel.zenmmo.api.skill.acrobatics.Backstep;
import com.rehoukrel.zenmmo.api.skill.acrobatics.Leap;
import com.rehoukrel.zenmmo.api.skill.acrobatics.Rolling;
import com.rehoukrel.zenmmo.utils.DataConverter;
import com.rehoukrel.zenmmo.utils.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Acrobatics extends SkillTree {

    public List<Player> cd_slow = new ArrayList<>(), cd_speed = new ArrayList<>(), cd_defense = new ArrayList<>(), cd_leap = new ArrayList<>();

    public Acrobatics() {
        super("Acrobatics", XMaterial.LEATHER_BOOTS.parseMaterial(), Arrays.asList("&fGive you outstanding movement", "&fand useful tricks for survival"));

        setUseIncrement(true);
        setSkillPointIncrement(1);

        addSkill(new Agility(), new Backstep(), new Rolling(), new Leap());
        loadIcon();
    }

    @Override
    public void onDamage(EntityDamageEvent event) {
        if (event.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
            Rolling rolling = (Rolling) getSkills().get("Rolling");
            Player p = (Player) event.getEntity();
            int level = rolling.getLevel();
            double ignoreFall = rolling.getAttribute().get("ignore-fall") * level, resistance = rolling.getAttribute().get("defense") * level;
            int resistanceAmplifier = (int) Math.round(rolling.getAttribute().get("defense-amplifier")),
                    resistanceDuration = (int) Math.round(rolling.getAttribute().get("defense-duration")) * level * 20;

            ParticleManager pm = new ParticleManager(plugin, p);
            if (DataConverter.chance(ignoreFall) && p.isSneaking()) {
                event.setDamage(0);
                addExp(10);
                startCooldown();
                showProgress(p);
            }
            if (!cd_defense.contains(p) && DataConverter.chance(resistance)) {
                cd_defense.add(p);
                p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, resistanceDuration, resistanceAmplifier), true);
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    cd_defense.remove(p);
                }, resistanceDuration + 120);
                addExp(20);
                startCooldown();
                showProgress(p);
            }
        }
    }

    @Override
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        Backstep backstep = (Backstep) getSkills().get("Backstep");
        LivingEntity victim = (LivingEntity) event.getEntity();
        if (backstep.getLevel() > 0 && victim.equals(getPlayerData().getPlayer().getPlayer())) {
            int level = backstep.getLevel();
            double dodge = backstep.getAttribute().get("dodge") * level;
            int dodgeProjectileLevel = (int) Math.round(backstep.getAttribute().get("dodge-projectile"));
            if (DataConverter.chance(dodge)) {
                if (event.getDamager() instanceof Projectile || event.getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE)) {
                    if (level >= dodgeProjectileLevel) {
                        backstep.doDodge((LivingEntity) event.getEntity());
                        event.setCancelled(true);
                    }
                } else {
                    backstep.doDodge((LivingEntity) event.getEntity());
                    event.setCancelled(true);
                }
                addExp(15);
                startCooldown();
                showProgress((Player) event.getEntity());
            }
        }
    }

    @Override
    public void onPlayerSneakToggle(PlayerToggleSneakEvent event){
        Player p = event.getPlayer();
        Leap leap = (Leap) getSkills().get("Leap");
        if (leap.getLevel() > 0){
            int level = leap.getLevel();
            int leapReq = (int) Math.round(leap.getAttribute().get("leap"));
            if (level >= leapReq && !cd_leap.contains(p) && p.isOnGround()){
                cd_leap.add(p);
                Vector vec = p.getEyeLocation().getDirection().clone().multiply(1.5);
                vec = vec.add(new Vector(0, 1, 0));
                p.setVelocity(vec);
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    cd_leap.remove(p);
                }, 100);
            }
        }
    }

    @Override
    public void onPlayerSprintToggle(PlayerToggleSprintEvent event) {
        Player p = event.getPlayer();
        Agility agility = (Agility) getSkills().get("Agility");
        if (agility.getLevel() > 0) {
            int level = agility.getLevel();
            int speed = ((int) Math.round(agility.getAttribute().get("speed")) + (level % agility.getMaxLevel())),
                    speedDuration = (int) Math.round(agility.getAttribute().get("speed-duration")) * level;
            double slowResistance = agility.getAttribute().get("slow-resistance") * level;

            if (p.hasPotionEffect(PotionEffectType.SLOW) && !cd_slow.contains(p)) {
                if (DataConverter.chance(slowResistance)) {
                    p.removePotionEffect(PotionEffectType.SLOW);
                    cd_slow.add(p);
                    addExp(new Random().nextInt(10));
                    startCooldown();
                    showProgress(p);
                    Bukkit.getScheduler().runTaskLater(plugin, () -> cd_slow.remove(p), 100);
                }
            }

            if (!cd_speed.contains(p)) {
                cd_speed.add(p);
                p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, speedDuration * 20, speed));
                addExp(new Random().nextInt(4));
                startCooldown();
                showProgress(p);
                p.getWorld().playSound(p.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1, 2);
                Bukkit.getScheduler().runTaskLater(plugin, () -> cd_speed.remove(p), (speedDuration * 20) + 120);
            }
        }
    }
}
