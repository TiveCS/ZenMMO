package com.rehoukrel.zenmmo.api.skill.combat;

import com.rehoukrel.zenmmo.api.Skill;
import com.rehoukrel.zenmmo.utils.XMaterial;

import java.util.Arrays;

public class Sword extends Skill {
    public Sword() {
        super("Sword", XMaterial.IRON_SWORD.parseMaterial(), Arrays.asList(""), 20);

        addAttribute("bleed", 1); // Chance
        addAttribute("thrust", 0.25); // Chance

        loadPlaceholderAttribute();
        loadDefaultIconTemplate();
    }
}
