package com.rehoukrel.zenmmo.api.skill.acrobatics;

import com.rehoukrel.zenmmo.api.Skill;
import com.rehoukrel.zenmmo.utils.XMaterial;

import java.util.Arrays;

public class Rolling extends Skill {
    public Rolling() {
        super("Rolling", XMaterial.SLIME_BALL.parseMaterial(),
                Arrays.asList("&7- Ignore fall damage while sneak [calc(%rolling_attribute_ignore-fall%*%level%)%]",
                        "&7- Resistance calc(%rolling_attribute_defense-amplifier%+1) for calc(%rolling_attribute_defense-duration%*%level%)s [calc(%rolling_attribute_defense%*%level%)%]"), 15);

        addAttribute("ignore-fall", 3);
        addAttribute("defense", 5); // Chance
        addAttribute("defense-duration", 2); // In seconds
        addAttribute("defense-amplifier", 0); // Amplifier start with 0

        loadPlaceholderAttribute();
        loadDefaultIconTemplate();
    }
}
