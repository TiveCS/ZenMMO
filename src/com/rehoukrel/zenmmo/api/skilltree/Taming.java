package com.rehoukrel.zenmmo.api.skilltree;

import com.rehoukrel.zenmmo.api.SkillTree;
import com.rehoukrel.zenmmo.utils.XMaterial;

import java.util.Arrays;

public class Taming extends SkillTree {
    public Taming() {
        super("Taming", XMaterial.LEAD.parseMaterial(), Arrays.asList("&fExtends your taming ability", "&fand get more speciality"));
    }
}
