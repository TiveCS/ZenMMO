package com.rehoukrel.zenmmo.api.skilltree;

import com.rehoukrel.zenmmo.api.SkillTree;
import com.rehoukrel.zenmmo.api.skill.resource.Mining;
import com.rehoukrel.zenmmo.api.skill.resource.Woodcutting;
import com.rehoukrel.zenmmo.utils.XMaterial;

import java.util.ArrayList;
import java.util.Arrays;

public class Resource extends SkillTree {
    public Resource() {
        super("Resource", XMaterial.IRON_SHOVEL.parseMaterial(), new ArrayList<>(
                Arrays.asList("&fTree to collect natural" , "&fresource on non-aquatic biome")));

        addSkill(new Woodcutting(), new Mining());
        loadIcon();
    }
}
