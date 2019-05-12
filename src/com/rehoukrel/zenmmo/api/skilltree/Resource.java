package com.rehoukrel.zenmmo.api.skilltree;

import com.rehoukrel.zenmmo.api.SkillTree;
import com.rehoukrel.zenmmo.api.skill.resource.Mining;
import com.rehoukrel.zenmmo.api.skill.resource.Woodcutting;
import com.rehoukrel.zenmmo.utils.XMaterial;
import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.event.player.PlayerFishEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Resource extends SkillTree {

    List<Material> LOG =
            Arrays.asList(XMaterial.OAK_LOG.parseMaterial(), XMaterial.BIRCH_LOG.parseMaterial(), XMaterial.ACACIA_LOG.parseMaterial(),
                    XMaterial.JUNGLE_LOG.parseMaterial(), XMaterial.SPRUCE_LOG.parseMaterial(), XMaterial.DARK_OAK_LOG.parseMaterial());

    public Resource() {
        super("Resource", XMaterial.IRON_SHOVEL.parseMaterial(), new ArrayList<>(
                Arrays.asList("&fTree to collect natural" , "&fresource on non-aquatic biome")));

        addSkill(new Woodcutting(), new Mining());
        loadIcon();
    }

    @Override
    public void onBlockBreak(BlockBreakEvent event) {
        if (LOG.contains(event.getBlock().getType())){
            addExp();
            showProgress(event.getPlayer());
        }
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
