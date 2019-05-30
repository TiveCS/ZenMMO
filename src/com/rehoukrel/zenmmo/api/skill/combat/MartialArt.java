package com.rehoukrel.zenmmo.api.skill.combat;

import com.rehoukrel.zenmmo.api.Skill;
import com.rehoukrel.zenmmo.utils.XMaterial;

import java.util.Arrays;

public class MartialArt extends Skill {
    public MartialArt() {
        super("Martial Arts", XMaterial.LEATHER_CHESTPLATE.parseMaterial(),
                Arrays.asList("&7- Increase hand strength [calc(%martial arts_attribute_strength%*%level%)%]"
                ,"&7- Chance to Chop [calc(%martial arts_attribute_chop%*%level%)%]"
                ,"&7- Chance to Iron Punch [calc(%martial arts_attribute_iron-punch%*%level%)%]"
                ,"&7- Chance to Bone break [calc(%martial arts_attribute_bonebreak%*%level%)%]"), 20);

        addAttribute("strength", 10); // Increase in percent
        addAttribute("chop", 2.5); // Chance
        addAttribute("iron-punch", 2.5); // Chance
        addAttribute("bonebreak", 1); // Chance

        loadPlaceholderAttribute();
        loadDefaultIconTemplate();
    }
}
