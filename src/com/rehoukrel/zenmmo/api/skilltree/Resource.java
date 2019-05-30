package com.rehoukrel.zenmmo.api.skilltree;

import com.rehoukrel.zenmmo.ZenMMO;
import com.rehoukrel.zenmmo.api.Skill;
import com.rehoukrel.zenmmo.api.SkillTree;
import com.rehoukrel.zenmmo.api.skill.resource.Mining;
import com.rehoukrel.zenmmo.api.skill.resource.Woodcutting;
import com.rehoukrel.zenmmo.utils.DataConverter;
import com.rehoukrel.zenmmo.utils.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class Resource extends SkillTree {

    ZenMMO plugin = ZenMMO.getPlugin(ZenMMO.class);
    List<Player> cooldowns = new ArrayList<>();

    List<Material> LOG =
            Arrays.asList(XMaterial.OAK_LOG.parseMaterial(), XMaterial.BIRCH_LOG.parseMaterial(), XMaterial.ACACIA_LOG.parseMaterial(),
                    XMaterial.JUNGLE_LOG.parseMaterial(), XMaterial.SPRUCE_LOG.parseMaterial(), XMaterial.DARK_OAK_LOG.parseMaterial());
    List<Material> ORE =
            Arrays.asList(XMaterial.COAL_ORE.parseMaterial(), XMaterial.IRON_ORE.parseMaterial(), XMaterial.GOLD_ORE.parseMaterial(),
                    XMaterial.EMERALD_ORE.parseMaterial(), XMaterial.DIAMOND_ORE.parseMaterial(), XMaterial.LAPIS_ORE.parseMaterial(),
                    XMaterial.REDSTONE_ORE.parseMaterial(), XMaterial.NETHER_QUARTZ_ORE.parseMaterial());

    HashMap<XMaterial, String> LOG_CHECKER = new HashMap<XMaterial, String>(){{
        put(XMaterial.OAK_LOG, "OAK");
        put(XMaterial.BIRCH_LOG, "BIRCH");
        put(XMaterial.SPRUCE_LOG, "SPRUCE");
        put(XMaterial.JUNGLE_LOG, "JUNGLE");
        put(XMaterial.ACACIA_LOG, "ACACIA");
        put(XMaterial.DARK_OAK_LOG, "DARK_OAK");
    }};


    public Resource() {
        super("Resource", XMaterial.IRON_SHOVEL.parseMaterial(), new ArrayList<>(
                Arrays.asList("&fTree to collect natural" , "&fresource on non-aquatic biome")));

        addSkill(new Woodcutting(), new Mining());
        loadIcon();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        if (LOG.contains(block.getType()) || ORE.contains(block.getType())){

            ItemStack penaltyBlock = null, productBlock = null;
            String s = "";
            XMaterial xm = XMaterial.requestXMaterial(block.getType().name(), block.getData());
            s = LOG_CHECKER.get(xm);
            try {
                switch(s){
                    case "DARK_OAK":
                        penaltyBlock = new ItemStack(XMaterial.DARK_OAK_PLANKS.parseMaterial(), 2);
                        productBlock = new ItemStack(XMaterial.DARK_OAK_LOG.parseMaterial());
                        break;
                    case "ACACIA":
                        penaltyBlock = new ItemStack(XMaterial.ACACIA_PLANKS.parseMaterial(), 2);
                        productBlock = new ItemStack(XMaterial.ACACIA_LOG.parseMaterial());
                        break;
                    case "OAK":
                        penaltyBlock = new ItemStack(XMaterial.OAK_PLANKS.parseMaterial(), 2);
                        productBlock = new ItemStack(XMaterial.OAK_LOG.parseMaterial());
                        break;
                    case "BIRCH":
                        penaltyBlock = new ItemStack(XMaterial.BIRCH_PLANKS.parseMaterial(), 2);
                        productBlock = new ItemStack(XMaterial.BIRCH_LOG.parseMaterial());
                        break;
                    case "SPRUCE":
                        penaltyBlock = new ItemStack(XMaterial.SPRUCE_PLANKS.parseMaterial(), 2);
                        productBlock = new ItemStack(XMaterial.SPRUCE_LOG.parseMaterial());
                        break;
                    case "JUNGLE":
                        penaltyBlock = new ItemStack(XMaterial.JUNGLE_PLANKS.parseMaterial(), 2);
                        productBlock = new ItemStack(XMaterial.JUNGLE_LOG.parseMaterial());
                        break;
                }

            }catch(Exception e){}

            if (LOG.contains(block.getType())) {
                Skill woodcutting = this.getSkills().get("Woodcutting");
                int level = woodcutting.getLevel();
                double basePenalty = 30; // in Percent
                double penalty = woodcutting.getAttribute().get("Penalty") * level, product = woodcutting.getAttribute().get("Product") * level;
                if (DataConverter.chance((basePenalty - penalty < 0) ? 0 : basePenalty - penalty)) {
                    if (penaltyBlock != null) {
                        block.getWorld().dropItem(block.getLocation(), penaltyBlock);
                    }
                } else if (DataConverter.chance(product) && !cooldowns.contains(event.getPlayer())) {
                    block.getWorld().dropItemNaturally(block.getLocation(), productBlock);
                    cooldowns.add(event.getPlayer());
                    Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, new Runnable() {
                        @Override
                        public void run() {
                            cooldowns.remove(event.getPlayer());
                        }
                    }, 60l);
                }
            }else if (ORE.contains(block.getType())){
                Skill mining = this.getSkills().get("Mining");
                int level = mining.getLevel();
                double doubleDrop = mining.getAttribute().get("double-drop"), penalty = mining.getAttribute().get("penalty");
                double basePenalty = (35 - penalty < 0 ? 0 : 35 - penalty); // in Percent
                if (DataConverter.chance(basePenalty)){
                    block.setType(Material.AIR);
                    block.getWorld().dropItem(block.getLocation(), new ItemStack(Material.COBBLESTONE));
                }else if (DataConverter.chance(doubleDrop)){
                    block.setType(Material.AIR);
                    block.getWorld().dropItem(block.getLocation(), new ItemStack(block.getType()));
                }

            }

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
