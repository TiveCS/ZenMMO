package com.rehoukrel.zenmmo.api.skill.acrobatics;

import com.rehoukrel.zenmmo.api.Skill;
import com.rehoukrel.zenmmo.utils.XMaterial;

import java.util.Arrays;

public class Leap extends Skill {
    public Leap() {
        super("Leap", XMaterial.GOLDEN_BOOTS.parseMaterial(),
                Arrays.asList("&7- Sneak to Leap [Level %leap_attribute_leap%]",
                        "&7- Leap not give fall damage [Level %leap_attribute_no-fall-damage%]"), 15);

        addAttribute("leap", 1);
        addAttribute("no-fall-damage", 15);

        loadPlaceholderAttribute();
        loadDefaultIconTemplate();
    }
}
