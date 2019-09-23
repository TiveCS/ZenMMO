package com.rehoukrel.zenmmo.api.skill.combat;

import com.rehoukrel.zenmmo.api.Skill;
import com.rehoukrel.zenmmo.utils.XMaterial;

import java.util.Arrays;

public class Archery extends Skill {
    public Archery() {
        super("Archery", XMaterial.BOW.parseMaterial(),
                Arrays.asList("&7- Increase shoot power [calc(%archery_attribute_power%*%level%)%]"
                ,"&7- Chance to apply Headache [calc(%archery_attribute_headache%*%level%)%]"), 20);

        addAttribute("power", 2.5); // Increase
        addAttribute("headache", 2); // Chance

        loadPlaceholderAttribute();
        loadDefaultIconTemplate();
    }
}
