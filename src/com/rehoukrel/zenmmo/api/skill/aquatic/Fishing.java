package com.rehoukrel.zenmmo.api.skill.aquatic;

import com.rehoukrel.zenmmo.api.Skill;
import com.rehoukrel.zenmmo.utils.XMaterial;

import java.util.Arrays;

public class Fishing extends Skill {
    public Fishing() {
        super("Fishing", XMaterial.COD.parseMaterial(),
                Arrays.asList("&7- Increase exp receive [calc(%fishing_attribute_exp-bonus%*%level%)%]",
                        "&7- Chance to get treasure [calc(%fishing_attribute_treasurer%*%level%)%]"), 20);

        addAttribute("exp-bonus", 7.5); // Bonus exp in percent
        addAttribute("treasurer", 0.2);

        loadPlaceholderAttribute();
        loadDefaultIconTemplate();
    }
}
