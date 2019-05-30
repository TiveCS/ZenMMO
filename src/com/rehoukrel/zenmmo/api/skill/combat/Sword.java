package com.rehoukrel.zenmmo.api.skill.combat;

import com.rehoukrel.zenmmo.api.Skill;
import com.rehoukrel.zenmmo.utils.XMaterial;

import java.util.Arrays;

public class Sword extends Skill {
    public Sword() {
        super("Sword", XMaterial.IRON_SWORD.parseMaterial(),
                Arrays.asList("&7- Chance to apply Bleed [calc(%sword_attribute_bleed%*%level%)%]"
                ,"&7- Chance to Thrust [calc(%sword_attribute_thrust%*%level%)%]"), 20);

        addAttribute("bleed", 1); // Chance
        addAttribute("thrust", 0.25); // Chance

        loadPlaceholderAttribute();
        loadDefaultIconTemplate();
    }
}
