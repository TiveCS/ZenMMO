package com.rehoukrel.zenmmo.api.skilltree;

import com.rehoukrel.zenmmo.api.SkillTree;
import com.rehoukrel.zenmmo.api.skill.aquatic.Breathing;
import com.rehoukrel.zenmmo.api.skill.aquatic.Fishing;
import com.rehoukrel.zenmmo.api.skill.aquatic.Mobility;
import com.rehoukrel.zenmmo.api.skill.aquatic.WaterHunting;
import com.rehoukrel.zenmmo.utils.DataConverter;
import com.rehoukrel.zenmmo.utils.XMaterial;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Aquatic extends SkillTree {

    List<ItemStack> TREASURE = Arrays.asList(
            XMaterial.DIAMOND.parseItem(), XMaterial.DIAMOND_BLOCK.parseItem(), XMaterial.BOOK.parseItem(),
            XMaterial.EMERALD.parseItem(), XMaterial.LAPIS_LAZULI.parseItem()
    );

    public Aquatic() {
        super("Aquatic", XMaterial.FISHING_ROD.parseMaterial(), Arrays.asList("&fMostly for aquatic activity", "&fand also for oceanic things"));

        setUseIncrement(true);

        addSkill(new Breathing(), new Fishing(), new Mobility(), new WaterHunting());
        loadIcon();
    }

    @Override
    public void onPlayerFish(PlayerFishEvent event) {
        if (event.getState().equals(PlayerFishEvent.State.CAUGHT_FISH)) {
            Fishing fishing = (Fishing) this.getSkills().get("Fishing");
            double expbonus = fishing.getAttribute().get("exp-bonus") * fishing.getLevel(), treasurer = fishing.getAttribute().get("treasurer") * fishing.getLevel();
            if (DataConverter.chance(treasurer)) {
                ItemStack i = TREASURE.get(new Random().nextInt(TREASURE.size() - 1));
                event.getHook().getLocation().getWorld().dropItemNaturally(event.getHook().getLocation(), i);
            }
            int exp = event.getExpToDrop();
            event.setExpToDrop((int) Math.round(exp + exp * expbonus / 100));
            addExp(new Random().nextInt(15));
            showProgress(event.getPlayer());
        }
    }

}
