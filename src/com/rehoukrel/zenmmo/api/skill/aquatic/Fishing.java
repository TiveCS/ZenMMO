package com.rehoukrel.zenmmo.api.skill.aquatic;

import com.rehoukrel.zenmmo.api.Skill;
import com.rehoukrel.zenmmo.utils.XMaterial;

import java.util.Arrays;

public class Fishing extends Skill {
    public Fishing() {
        super("Fishing", XMaterial.COD.parseMaterial(),
                Arrays.asList("&7- Increase exp receive [calc(%fishing_attribute_exp-bonus%*%level%)%]"), 20);

        addAttribute("exp-bonus", 10); // Bonus exp in percent

        loadPlaceholderAttribute();
        loadDefaultIconTemplate();
    }
}
