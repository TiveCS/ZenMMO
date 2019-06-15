package com.rehoukrel.zenmmo.api.skilltree;

import com.rehoukrel.zenmmo.api.SkillTree;
import com.rehoukrel.zenmmo.api.skill.taming.FriendlyAura;
import com.rehoukrel.zenmmo.api.skill.taming.HorseTamer;
import com.rehoukrel.zenmmo.api.skill.taming.WolfMastery;
import com.rehoukrel.zenmmo.utils.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class Taming extends SkillTree {

    List<Player> hcd = new ArrayList<>(), healcooldown = new ArrayList<>();

    public Taming() {
        super("Taming", XMaterial.LEAD.parseMaterial(), Arrays.asList("&fExtends your taming ability", "&fand get more speciality"));

        addSkill(new WolfMastery(), new FriendlyAura(), new HorseTamer());
        loadIcon();
    }

    @Override
    public void onPlayerExpChange(PlayerExpChangeEvent event) {
        Player p = event.getPlayer();
        WolfMastery wolfMastery = (WolfMastery) getSkills().get("WolfMastery");
        if (wolfMastery.getLevel() > 0){
            int level = wolfMastery.getLevel(), xpHeal = (int) Math.round(wolfMastery.getAttribute().get("xp-heal"));
            if (level >= xpHeal && !healcooldown.contains(p)){
                event.setAmount(0);
                double healAmount = wolfMastery.getAttribute().get("xp-heal-amount");
                long cooldown = Math.round(wolfMastery.getAttribute().get("xp-heal-cooldown")*20);
                boolean remove = false;
                Entity[] entities = p.getWorld().getChunkAt(p.getLocation()).getEntities();
                List<Wolf> wolves = new ArrayList<>();
                for (Entity e : entities){
                    if (e instanceof Wolf){
                        Wolf w = (Wolf) e;
                        if (w.getOwner().equals(p) && w != null && w.getHealth() < w.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()){wolves.add(w);}
                    }
                }
                for (Wolf wolf : wolves){
                    double ch = wolf.getHealth() + healAmount;
                    if (ch > wolf.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()){
                        ch = 0;
                    }
                    wolf.setHealth(ch);
                }
                Bukkit.getScheduler().runTaskLater(plugin, () -> healcooldown.remove(p), cooldown);
            }
        }
    }

    @Override
    public void onPlayerBreed(EntityBreedEvent event) {
        Player p = (Player) event.getBreeder();
        FriendlyAura friendlyAura = (FriendlyAura) getSkills().get("FriendlyAura");
        if (friendlyAura.getLevel() > 0){
            int level = friendlyAura.getLevel();
            if (level >= (int) Math.round(friendlyAura.getAttribute().get("happy"))){
                double xpBonus = friendlyAura.getAttribute().get("exp-boost")*level;
                int xp = event.getExperience();
                xp += (int) Math.round(xp*xpBonus/100);
                event.setExperience(xp);
                if (!hcd.contains(p)){
                    hcd.add(p);
                    int cd = (int) Math.round(friendlyAura.getAttribute().get("happy-cooldown")),
                            dur = (int) Math.round(friendlyAura.getAttribute().get("happy-duration")),
                            am = (int) Math.round(friendlyAura.getAttribute().get("happy-amplifier"));
                    friendlyAura.applyHappy(p, dur, am);
                    Bukkit.getScheduler().runTaskLater(plugin, () -> hcd.remove(p), (cd + dur) * 20);
                }
            }
        }
    }

    @Override
    public void onPlayerTame(EntityTameEvent event) {
        Player tamer = (Player) event.getOwner();
        LivingEntity animal = event.getEntity();

        HorseTamer horseTamer = (HorseTamer) getSkills().get("HorseTamer");
        WolfMastery wolfMastery = (WolfMastery) getSkills().get("WolfMastery");

        if (horseTamer.getLevel() > 0 && animal.getType().equals(EntityType.HORSE)){
            int level = horseTamer.getLevel(), xpBonus = (int) Math.round(horseTamer.getAttribute().get("exp-bonus"));
            ExperienceOrb experienceOrb = (ExperienceOrb) tamer.getWorld().spawnEntity(tamer.getLocation(), EntityType.EXPERIENCE_ORB);
            experienceOrb.setExperience(xpBonus);
        }
        if (wolfMastery.getLevel() > 0 && animal.getType().equals(EntityType.WOLF)){
            int level = wolfMastery.getLevel();
            double maxHealth = wolfMastery.getAttribute().get("max-health")*level, damage = wolfMastery.getAttribute().get("damage")*level;
            double health = animal.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue(), dmg = animal.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).getBaseValue();
            health += maxHealth; dmg += damage;
            animal.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(health);
            animal.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(dmg);
        }
    }

}
