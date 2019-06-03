package com.rehoukrel.zenmmo.api.skill.aquatic;

import com.rehoukrel.zenmmo.api.Skill;
import com.rehoukrel.zenmmo.utils.XMaterial;

import java.util.Arrays;

public class Mobility extends Skill {
    public Mobility() {
        super("Mobility", XMaterial.LEATHER_BOOTS.parseMaterial(),
                Arrays.asList("&7- Bonus movement speed when sprint on water [calc(%mobility_attribute_movement%*%level%)%]",
                        "&7- Haste calc(%mobility_attribute_efficiency%+1) when sneak underwater [calc(%mobility_attribute_efficiency-duration%*%level%) second(s)]"), 25);

        addAttribute("movement", 2); // Bonus movement speed on water in percent
        addAttribute("efficiency-duration", 8); // Duration
        addAttribute("efficiency", 1); // Apply fast digging potion level/amplifier (start with 0)

        loadPlaceholderAttribute();
        loadDefaultIconTemplate();
    }
}
