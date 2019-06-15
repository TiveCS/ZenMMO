package com.rehoukrel.zenmmo.api.skill.taming;

import com.rehoukrel.zenmmo.api.Skill;
import com.rehoukrel.zenmmo.utils.ParticleManager;
import com.rehoukrel.zenmmo.utils.XMaterial;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;

public class FriendlyAura extends Skill {

    public FriendlyAura() {
        super("FriendlyAura", XMaterial.OAK_SAPLING.parseMaterial(),
                Arrays.asList("&7- Apply happy effect [Level %friendlyaura_attribute_happy%]",
                        "&7- Exp bonus after breed [calc(%friendlyaura_attribute_exp-boost%*%level%)]"), 15);

        addAttribute("happy", 1); // Apply happy effect (value Required level)
        addAttribute("happy-duration", 5); // In seconds
        addAttribute("happy-cooldown", 20); // In second
        addAttribute("happy-amplifier", 0); // Potion effect's amplifier (start with 0)
        addAttribute("exp-boost", 5); // Percentage to increase breed or tame exp

        loadPlaceholderAttribute();
        loadDefaultIconTemplate();
    }

    public void applyHappy(Player le,int duration, int amplifier){
        PotionEffect regen = new PotionEffect(PotionEffectType.REGENERATION, duration*20, amplifier)
                , absorption = new PotionEffect(PotionEffectType.ABSORPTION, duration*20, amplifier);

        le.addPotionEffect(regen);
        le.addPotionEffect(absorption);

        ParticleManager pm = new ParticleManager(le);
        pm.trails(Particle.VILLAGER_HAPPY, le, 1, duration*20, 10);
    }
}
