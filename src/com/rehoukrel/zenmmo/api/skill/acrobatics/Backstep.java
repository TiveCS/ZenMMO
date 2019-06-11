package com.rehoukrel.zenmmo.api.skill.acrobatics;

import com.rehoukrel.zenmmo.api.Skill;
import com.rehoukrel.zenmmo.utils.XMaterial;

import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import java.util.Arrays;

public class Backstep extends Skill {
    public Backstep() {
        super("Backstep", XMaterial.LEATHER.parseMaterial(),
                Arrays.asList("&7- Chance to dodge attack [calc(%backstep_attribute_dodge%*%level%)%]",
                        "&7- Dodge projectile attack [Level %backstep_attribute_dodge-projectile%]"), 15);

        addAttribute("dodge", 0.8);
        addAttribute("dodge-projectile", 5);

        loadPlaceholderAttribute();
        loadDefaultIconTemplate();
    }

    public void doDodge(LivingEntity entity){
        if (entity.isOnGround() && !entity.isDead()) {
            Vector dir = entity.getEyeLocation().getDirection().clone();
            entity.setVelocity(dir.multiply(-2));
        }
    }
}
