package com.rehoukrel.zenmmo.api.skilltree;

import com.rehoukrel.zenmmo.api.SkillTree;
import com.rehoukrel.zenmmo.api.skill.combat.Archery;
import com.rehoukrel.zenmmo.api.skill.combat.MartialArt;
import com.rehoukrel.zenmmo.api.skill.combat.Sword;
import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.event.player.PlayerFishEvent;

import java.util.Arrays;

public class Combat extends SkillTree {
    public Combat() {
        super("Combat", Material.IRON_SWORD, Arrays.asList("&fExtends your battle mechanic", "&fand effect while in combat!"));

        addSkill(new Sword(), new Archery(), new MartialArt());
        loadIcon();
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
