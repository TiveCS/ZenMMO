package com.rehoukrel.zenmmo.api.skilltree;

import com.rehoukrel.zenmmo.api.SkillTree;
import com.rehoukrel.zenmmo.utils.XMaterial;
import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.event.player.PlayerFishEvent;

import java.util.Arrays;
import java.util.List;

public class Aquatic extends SkillTree {
    public Aquatic() {
        super("Aquatic", XMaterial.FISHING_ROD.parseMaterial(), Arrays.asList("&fMostly for aquatic activity", "&fand also for oceanic things"));
    }

    @Override
    public void onBlockBreak(BlockBreakEvent event) {

    }

    @Override
    public void onEntityBowShoot(EntityShootBowEvent event) {

    }

    @Override
    public void onEntityDamage(EntityDamageByEntityEvent event) {

    }

    @Override
    public void onPlayerFish(PlayerFishEvent event) {

    }

    @Override
    public void onTame(EntityTameEvent event) {

    }
}
