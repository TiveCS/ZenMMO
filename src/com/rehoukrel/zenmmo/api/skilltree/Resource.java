package com.rehoukrel.zenmmo.api.skilltree;

import com.rehoukrel.zenmmo.ZenMMO;
import com.rehoukrel.zenmmo.api.Skill;
import com.rehoukrel.zenmmo.api.SkillTree;
import com.rehoukrel.zenmmo.api.skill.resource.Mining;
import com.rehoukrel.zenmmo.api.skill.resource.Woodcutting;
import com.rehoukrel.zenmmo.event.OnShears;
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
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class Resource extends SkillTree {

    ZenMMO plugin = ZenMMO.getPlugin(ZenMMO.class);
    List<Player> cooldowns = new ArrayList<>();

    List<Material> AXE = Arrays.asList(
            XMaterial.WOODEN_AXE.parseMaterial(), XMaterial.STONE_AXE.parseMaterial(), XMaterial.GOLDEN_AXE.parseMaterial(),
            XMaterial.IRON_AXE.parseMaterial(), XMaterial.DIAMOND_AXE.parseMaterial());
    List<Material> PICKAXE = Arrays.asList(
            XMaterial.WOODEN_PICKAXE.parseMaterial(), XMaterial.STONE_PICKAXE.parseMaterial(), XMaterial.GOLDEN_PICKAXE.parseMaterial(),
            XMaterial.IRON_PICKAXE.parseMaterial(), XMaterial.DIAMOND_PICKAXE.parseMaterial());
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

    HashMap<XMaterial, String> ORE_CHECKER = new HashMap<XMaterial, String>(){{
        put(XMaterial.COAL_ORE, "COAL");
        put(XMaterial.IRON_ORE, "IRON");
        put(XMaterial.GOLD_ORE, "GOLD");
        put(XMaterial.REDSTONE_ORE, "REDSTONE");
        put(XMaterial.DIAMOND_ORE, "DIAMOND");
        put(XMaterial.EMERALD_ORE, "EMERALD");
        put(XMaterial.LAPIS_ORE, "LAPIS");
        put(XMaterial.NETHER_QUARTZ_ORE, "QUARTZ");
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
        Player p = event.getPlayer();
        ItemStack hand = event.getPlayer().getInventory().getItemInMainHand();
        if (LOG.contains(block.getType()) || ORE.contains(block.getType())){
            ItemStack penaltyBlock = null, productBlock = null;
            XMaterial xm = XMaterial.isNewVersion() ? XMaterial.fromString(block.getType().name()) : XMaterial.requestXMaterial(block.getType().name(), block.getData());
            String s = "";
            if (LOG.contains(block.getType()) && AXE.contains(hand.getType())) {
                addExp();
                showProgress(event.getPlayer());
                Skill woodcutting = this.getSkills().get("Woodcutting");

                s = LOG_CHECKER.get(xm);
                try {
                    switch(s){
                        case "DARK_OAK":
                            penaltyBlock = XMaterial.DARK_OAK_PLANKS.parseItem();
                            productBlock = XMaterial.DARK_OAK_LOG.parseItem();
                            break;
                        case "ACACIA":
                            penaltyBlock = XMaterial.ACACIA_PLANKS.parseItem();
                            productBlock = XMaterial.ACACIA_LOG.parseItem();
                            break;
                        case "OAK":
                            penaltyBlock = XMaterial.OAK_PLANKS.parseItem();
                            productBlock = XMaterial.OAK_LOG.parseItem();
                            break;
                        case "BIRCH":
                            penaltyBlock = XMaterial.BIRCH_PLANKS.parseItem();
                            productBlock = XMaterial.BIRCH_LOG.parseItem();
                            break;
                        case "SPRUCE":
                            penaltyBlock = XMaterial.SPRUCE_PLANKS.parseItem();
                            productBlock = XMaterial.SPRUCE_LOG.parseItem();
                            break;
                        case "JUNGLE":
                            penaltyBlock = XMaterial.JUNGLE_PLANKS.parseItem();
                            productBlock = XMaterial.JUNGLE_LOG.parseItem();
                            break;
                    }

                }catch(Exception e){}

                penaltyBlock.setAmount(2);
                productBlock.setAmount(1);

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
            }else if (ORE.contains(block.getType()) && PICKAXE.contains(hand.getType())){

                s = ORE_CHECKER.get(xm);
                if (s.equals("EMERALD")){
                    productBlock = XMaterial.EMERALD.parseItem();
                }else if (s.equals("DIAMOND")){
                    productBlock = XMaterial.DIAMOND.parseItem();
                }else if (s.equals("QUARTZ")){
                    productBlock = XMaterial.QUARTZ.parseItem();
                }else if (s.equals("REDSTONE")) {
                    productBlock = XMaterial.REDSTONE.parseItem();
                }else if (s.equals("LAPIS")) {
                    productBlock = XMaterial.LAPIS_LAZULI.parseItem();
                }else {
                    for (XMaterial c : ORE_CHECKER.keySet()) {
                        if (ORE_CHECKER.get(c).equals(s)) {
                            productBlock = c.parseItem();
                            break;
                        }
                    }
                }

                addExp();
                showProgress(event.getPlayer());
                Skill mining = this.getSkills().get("Mining");
                int level = mining.getLevel();
                double doubleDrop = mining.getAttribute().get("double-drop")*level, penalty = mining.getAttribute().get("penalty")*level;
                double basePenalty = (35 - penalty < 0 ? 0 : 35 - penalty); // in Percent
                if (DataConverter.chance(basePenalty)){
                    block.setType(Material.AIR);
                    block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.COBBLESTONE));
                }else if (DataConverter.chance(doubleDrop)){
                    block.getWorld().dropItemNaturally(block.getLocation(), productBlock);
                }

            }
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
