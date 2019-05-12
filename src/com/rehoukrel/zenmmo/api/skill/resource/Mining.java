package com.rehoukrel.zenmmo.api.skill.resource;

import com.rehoukrel.zenmmo.api.Skill;
import com.rehoukrel.zenmmo.utils.XMaterial;

import java.util.ArrayList;
import java.util.Arrays;

public class Mining extends Skill {
    public Mining() {
        super("Mining", XMaterial.IRON_PICKAXE.parseMaterial(),
                new ArrayList<>(Arrays.asList("&7- Double drop chance [%mining_attribute_double-drop%]",
                        "&7- Decrease penalty drop chance [%mining_attribute_penalty%]")), 20);

        addAttribute("double-drop", 2);
        addAttribute("penalty", 1.25);

        //-----------------------------------
        loadPlaceholderAttribute();
        loadDefaultIconTemplate();
    }
}
