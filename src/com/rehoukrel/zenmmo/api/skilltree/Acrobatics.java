package com.rehoukrel.zenmmo.api.skilltree;

import com.rehoukrel.zenmmo.api.SkillTree;
import com.rehoukrel.zenmmo.utils.XMaterial;

import java.util.Arrays;

public class Acrobatics extends SkillTree {
    public Acrobatics() {
        super("Acrobatics", XMaterial.LEATHER_BOOTS.parseMaterial(), Arrays.asList("&fGive you outstanding movement", "&fand useful tricks for survival"));
    }

}
