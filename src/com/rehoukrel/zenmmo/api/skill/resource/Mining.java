package com.rehoukrel.zenmmo.api.skill.resource;

import com.rehoukrel.zenmmo.api.Skill;
import com.rehoukrel.zenmmo.utils.XMaterial;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Mining extends Skill {
    public Mining() {
        super("Mining", XMaterial.IRON_PICKAXE.parseMaterial(),
                new ArrayList<>(Arrays.asList("&7- Double drop chance", "&7- Decrease penalty drop chance")), 20);
    }
}
