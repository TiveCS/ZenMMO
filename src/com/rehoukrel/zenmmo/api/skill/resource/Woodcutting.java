package com.rehoukrel.zenmmo.api.skill.resource;

import com.rehoukrel.zenmmo.api.Skill;
import com.rehoukrel.zenmmo.utils.XMaterial;

import java.util.ArrayList;
import java.util.Arrays;

public class Woodcutting extends Skill {
    public Woodcutting() {
        super("Woodcutting", XMaterial.OAK_LOG.parseMaterial(),
                new ArrayList<>(Arrays.asList("&7- Increase resource produced [calc(%woodcutting_attribute_product%*%level%)]",
                        "&7- Decrease resource penalty [calc(%woodcutting_attribute_penalty%*%level%)]")),
                30);

        //---------- ATTRIBUTE --------------

        addAttribute("Product", 1);
        addAttribute("Penalty", 1);

        //-----------------------------------
        loadPlaceholderAttribute();
        loadDefaultIconTemplate();
    }
}
