package com.rehoukrel.zenmmo.api.skill.acrobatics;

import com.rehoukrel.zenmmo.api.Skill;
import com.rehoukrel.zenmmo.utils.XMaterial;

import java.util.Arrays;

public class Agility extends Skill {
    public Agility() {
        super("Agility", XMaterial.LEATHER_BOOTS.parseMaterial(),
                Arrays.asList("&7- Speed calc(%agility_attribute_speed%+1) when sprint [calc(%agility_attribute_speed-duration%*%level%) Second(s)]",
                        "&7- Chance to remove Slow potion [calc(%agility_attribute_slow-resistance%*%level%)%]"), 10);

        addAttribute("speed", 1); // Amplifier
        addAttribute("speed-duration", 2); // in second
        addAttribute("slow-resistance", 5); // chance

        loadPlaceholderAttribute();
        loadDefaultIconTemplate();
    }
}
