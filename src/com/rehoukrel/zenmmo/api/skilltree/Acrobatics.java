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

public class Acrobatics extends SkillTree {
    public Acrobatics() {
        super("Acrobatics", XMaterial.LEATHER_BOOTS.parseMaterial(), Arrays.asList("&fGive you outstanding movement", "&fand useful tricks for survival"));
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
