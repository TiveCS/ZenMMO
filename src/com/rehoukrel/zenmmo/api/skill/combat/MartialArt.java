package com.rehoukrel.zenmmo.api.skill.combat;

import com.rehoukrel.zenmmo.api.Skill;
import com.rehoukrel.zenmmo.utils.XMaterial;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.Arrays;

public class MartialArt extends Skill {
    public MartialArt() {
        super("Martial Arts", XMaterial.LEATHER_CHESTPLATE.parseMaterial(),
                Arrays.asList("&7- Increase hand strength [calc(%martial arts_attribute_strength%*%level%)%]"
                ,"&7- Chance to Chop [calc(%martial arts_attribute_chop%*%level%)%]"
                ,"&7- Chance to Iron Punch [calc(%martial arts_attribute_iron-punch%*%level%)%]"
                ,"&7- Chance to Bone break [calc(%martial arts_attribute_bonebreak%*%level%)%]"), 20);

        addAttribute("strength", 10); // Increase in percent
        addAttribute("chop", 2.5); // Chance
        addAttribute("iron-punch", 2.5); // Chance
        addAttribute("bonebreak", 1); // Chance

        loadPlaceholderAttribute();
        loadDefaultIconTemplate();
    }

    public void chop(LivingEntity attacker, LivingEntity victim){
        victim.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 60, 1), true);
        victim.getWorld().playSound(victim.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1,2);
        victim.getWorld().spawnParticle(Particle.CRIT, victim.getLocation(), 5);
    }

    public void ironPunch(LivingEntity attacker, LivingEntity victim){
        Vector dir = attacker.getEyeLocation().getDirection();
        victim.setVelocity(dir);
        victim.getWorld().playSound(victim.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 1,1);
        victim.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, victim.getLocation(), 1);
    }
}
