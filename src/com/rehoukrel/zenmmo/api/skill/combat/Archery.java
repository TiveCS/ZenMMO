package com.rehoukrel.zenmmo.api.skill.combat;

import com.rehoukrel.zenmmo.api.Skill;
import com.rehoukrel.zenmmo.utils.XMaterial;

import java.util.Arrays;

public class Archery extends Skill {
    public Archery() {
        super("Archery", XMaterial.BOW.parseMaterial(), Arrays.asList(""), 20);

        addAttribute("power", 5); // Increase
        addAttribute("headache", 2); // Chance

        loadPlaceholderAttribute();
        loadDefaultIconTemplate();
    }
}
