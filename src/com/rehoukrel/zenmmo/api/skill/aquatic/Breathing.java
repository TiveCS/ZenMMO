package com.rehoukrel.zenmmo.api.skill.aquatic;

import com.rehoukrel.zenmmo.api.Skill;
import com.rehoukrel.zenmmo.utils.XMaterial;

import java.util.Arrays;

public class Breathing extends Skill {
    public Breathing() {
        super("Breathing", XMaterial.LEATHER_HELMET.parseMaterial(),
                Arrays.asList("&7- Chance to restore oxygen while drowning [calc(%breathing_attribute_oxygen%*%level%)%]"), 10);

        addAttribute("oxygen", 8);

        loadPlaceholderAttribute();
        loadDefaultIconTemplate();
    }
}
