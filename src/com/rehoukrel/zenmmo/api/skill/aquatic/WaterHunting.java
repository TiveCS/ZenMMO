package com.rehoukrel.zenmmo.api.skill.aquatic;

import com.rehoukrel.zenmmo.api.Skill;
import com.rehoukrel.zenmmo.utils.XMaterial;

import java.util.Arrays;

public class WaterHunting extends Skill {
    public WaterHunting() {
        super("WaterHunting", XMaterial.BOW.parseMaterial(),
                Arrays.asList("&7- Arrow ignore water physics while sneak [Level %waterhunting_attribute_water-arrow%]",
                        "&7- Apply vision underwater [calc(%waterhunting_attribute_vision%*%level%) second(s)]",
                        "&7- Cannot be targeted by mobs while underwater [Level %waterhunting_attribute_hide%]"), 25);

        addAttribute("water-arrow", 10); // Value = require level to use this
        addAttribute("vision", 10); // Value = duration in seconds
        addAttribute("hide", 15); // Value = require level to use this

        loadPlaceholderAttribute();
        loadDefaultIconTemplate();
    }
}
