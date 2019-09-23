package com.rehoukrel.zenmmo.api.skilltree;

import com.rehoukrel.zenmmo.api.SkillTree;
import com.rehoukrel.zenmmo.api.skill.combat.Archery;
import com.rehoukrel.zenmmo.api.skill.combat.MartialArt;
import com.rehoukrel.zenmmo.api.skill.combat.Sword;
import com.rehoukrel.zenmmo.utils.DataConverter;
import com.rehoukrel.zenmmo.utils.ParticleManager;
import com.rehoukrel.zenmmo.utils.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Combat extends SkillTree {

    List<EntityType> uncount = Arrays.asList(EntityType.ARMOR_STAND, EntityType.MINECART, EntityType.MINECART_CHEST,
            EntityType.MINECART_COMMAND, EntityType.MINECART_FURNACE, EntityType.MINECART_HOPPER, EntityType.MINECART_MOB_SPAWNER
            , EntityType.MINECART_TNT, EntityType.PAINTING, EntityType.ITEM_FRAME);

    List<Material> SWORD = Arrays.asList(
            XMaterial.WOODEN_SWORD.parseMaterial(), XMaterial.STONE_SWORD.parseMaterial(), XMaterial.GOLDEN_SWORD.parseMaterial(),
            XMaterial.IRON_SWORD.parseMaterial(), XMaterial.DIAMOND_SWORD.parseMaterial());

    List<Material> AXE = Arrays.asList(
            XMaterial.WOODEN_AXE.parseMaterial(), XMaterial.STONE_AXE.parseMaterial(), XMaterial.GOLDEN_AXE.parseMaterial(),
            XMaterial.IRON_AXE.parseMaterial(), XMaterial.DIAMOND_AXE.parseMaterial());

    public Combat() {
        super("Combat", Material.IRON_SWORD, Arrays.asList("&fExtends your battle mechanic", "&fand effect while in combat!"));

        setUseIncrement(true);
        setSkillPointIncrement(1);

        addSkill(new Sword(), new Archery(), new MartialArt());
        loadIcon();
    }

    @Override
    public void onEntityBowShoot(EntityShootBowEvent event) {
        if (event.isCancelled()){
            return;
        }
        Archery archery = (Archery) this.getSkills().get("Archery");
        if (archery.getLevel() > 0) {
            Projectile proj = (Projectile) event.getProjectile();
            ParticleManager pm = new ParticleManager(plugin, proj);
            double headcahe = archery.getAttribute().get("headache")*archery.getLevel(), power = archery.getAttribute().get("power")*archery.getLevel();
            if (DataConverter.chance(headcahe)){
                proj.setMetadata("headache", new FixedMetadataValue(plugin, true));
            }
            proj.setMetadata("power", new FixedMetadataValue(plugin, power));
        }
    }

    @Override
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.isCancelled()){
            return;
        }

        if (event.getEntity() instanceof LivingEntity && (!uncount.contains(event.getEntity().getType()))) {
            LivingEntity victim = (LivingEntity) event.getEntity();
            ParticleManager pm = new ParticleManager(plugin, victim);

            if (victim.isInvulnerable()){return;}
            if (event.getDamager() instanceof Projectile){
                Archery archery = (Archery) this.getSkills().get("Archery");
                Projectile proj = (Projectile) event.getDamager();
                addExp(new Random().nextInt(4));
                startCooldown();
                showProgress((Player) proj.getShooter());
                double dmg = event.getDamage();
                if (proj.hasMetadata("power")) {
                    event.setDamage(dmg + (dmg * proj.getMetadata("power").get(0).asDouble() / 100));
                }
                if (proj.hasMetadata("headache")) {
                    if (proj.getMetadata("headache").get(0).asBoolean()) {
                        pm.dotParticle(Particle.EXPLOSION_LARGE, 1, 0, 0, 0);
                        event.setDamage(event.getDamage() + (dmg * (50 + archery.getLevel()) / 100));
                        victim.setVelocity(proj.getLocation().getDirection());
                        victim.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20, 4), true);
                        victim.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 100, 2), true);
                    }
                }
                return;
            }

            else if (event.getDamager().equals(getPlayerData().getPlayer())) {
                Player attacker = (Player) event.getDamager();
                ItemStack hand = attacker.getInventory().getItemInMainHand();
                int maxExp = 2;
                if (SWORD.contains(hand.getType())){
                    maxExp = 4;
                }else if (AXE.contains(hand.getType())){
                    maxExp = 5;
                }else if (hand.getType().equals(Material.AIR)){
                    maxExp = 10;
                }
                addExp(new Random().nextInt(maxExp) + 1);
                startCooldown();
                showProgress(attacker);

                MartialArt martialart = (MartialArt) this.getSkills().get("Martial Arts");
                if (hand.getType().equals(Material.AIR)) {
                    double bonusDmg = martialart.getAttribute().get("strength") * martialart.getLevel();
                    double bonebreak = martialart.getAttribute().get("bonebreak") * martialart.getLevel(),
                            chop = martialart.getAttribute().get("chop") * martialart.getLevel(),
                            ironpunch = martialart.getAttribute().get("iron-punch") * martialart.getLevel();
                    double dmg = event.getDamage();
                    event.setDamage(dmg + dmg * bonusDmg/100);

                    if (DataConverter.chance(bonebreak)) {
                        victim.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 120, 1));
                        Vector dir = attacker.getEyeLocation().getDirection();
                        int sw = new Random().nextInt(1);
                        if (sw == 1) {
                            if (DataConverter.chance(chop)) {
                                event.setDamage(event.getDamage() + dmg * (bonusDmg * ((martialart.getLevel() / martialart.getMaxLevel()) / 120)));
                                martialart.chop(attacker, victim);
                            } else if (DataConverter.chance(ironpunch)) {
                                event.setDamage(event.getDamage() + dmg * (bonusDmg * ((martialart.getLevel() / martialart.getMaxLevel()) / 120)));
                                martialart.ironPunch(attacker, victim);
                            }
                        } else if (sw == 0) {
                            if (DataConverter.chance(ironpunch)) {
                                event.setDamage(event.getDamage() + dmg * (bonusDmg * ((martialart.getLevel() / martialart.getMaxLevel()) / 120)));
                                martialart.ironPunch(attacker, victim);
                            } else if (DataConverter.chance(chop)) {
                                event.setDamage(event.getDamage() + dmg * (bonusDmg * ((martialart.getLevel() / martialart.getMaxLevel()) / 120)));
                                martialart.chop(attacker, victim);
                            }
                        }
                    } else {
                        int sw = new Random().nextInt(1);
                        if (sw == 1) {
                            if (DataConverter.chance(chop)) {
                                event.setDamage(event.getDamage() + dmg * (bonusDmg * ((martialart.getLevel() / martialart.getMaxLevel()) / 150)));
                                martialart.chop(attacker, victim);
                            } else if (DataConverter.chance(ironpunch)) {
                                event.setDamage(event.getDamage() + dmg * (bonusDmg * ((martialart.getLevel() / martialart.getMaxLevel()) / 150)));
                                martialart.ironPunch(attacker, victim);
                            }
                        } else if (sw == 0) {
                            if (DataConverter.chance(ironpunch)) {
                                event.setDamage(event.getDamage() + dmg * (bonusDmg * ((martialart.getLevel() / martialart.getMaxLevel()) / 150)));
                                martialart.ironPunch(attacker, victim);
                            } else if (DataConverter.chance(chop)) {
                                event.setDamage(event.getDamage() + dmg * (bonusDmg * ((martialart.getLevel() / martialart.getMaxLevel()) / 150)));
                                martialart.chop(attacker, victim);
                            }
                        }
                    }
                }

                Sword sword = (Sword) this.getSkills().get("Sword");
                if (SWORD.contains(hand.getType())){
                    double bleed = sword.getAttribute().get("bleed")*sword.getLevel(), thrust = sword.getAttribute().get("thrust")*sword.getLevel();
                    double rand = new Random().nextDouble(), dmg = event.getDamage();
                    event.setDamage(dmg + (dmg*(sword.getLevel()*rand/4)/4));
                    pm.setOffsetY(1);
                    if (DataConverter.chance(bleed)){
                        event.setDamage(event.getDamage() + (dmg*(25 + sword.getLevel())/100));
                        victim.getWorld().playSound(victim.getLocation(), Sound.ENTITY_PLAYER_HURT, 1, 0);
                        pm.trailsBlockCrack(Material.REDSTONE_BLOCK, victim, 10, 60, 20);
                        for (int i = 1; i <= 3; i++){
                            Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                                @Override
                                public void run() {
                                    if (!victim.isDead()) {
                                        victim.damage(event.getDamage() / 8, attacker);
                                        pm.dotParticle(Particle.REDSTONE, 10, 0.5, 255, 0,0);
                                    }
                                }
                            }, 20*i);
                        }
                    }
                    if (DataConverter.chance(thrust)){
                        event.setDamage(event.getDamage() + (dmg*(75 + sword.getLevel())/100));
                        victim.getWorld().playSound(victim.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 1, 2);
                        pm.dotParticle(Particle.CLOUD, 5, 0.2, 0,0,0);
                    }
                }
            }
        }
    }
}
