package com.rehoukrel.zenmmo.api.skill.combat;

import com.rehoukrel.zenmmo.api.Skill;
import com.rehoukrel.zenmmo.utils.XMaterial;

import java.util.Arrays;

public class MartialArt extends Skill {
    public MartialArt() {
        super("Martial Arts", XMaterial.LEATHER_CHESTPLATE.parseMaterial(), Arrays.asList(""), 20);

        addAttribute("strength", 10); // Increase
        addAttribute("chop", 2.5); // Chance
        addAttribute("iron-punch", 2.5); // Chance
        addAttribute("bonebreak", 1); // Chance

        loadPlaceholderAttribute();
        loadDefaultIconTemplate();
    }
}
