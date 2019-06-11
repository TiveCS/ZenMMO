package com.rehoukrel.zenmmo.api.skill.taming;

import com.rehoukrel.zenmmo.api.Skill;
import com.rehoukrel.zenmmo.utils.XMaterial;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class HorseTamer extends Skill {
    public HorseTamer() {
        super("HorseTamer", XMaterial.HORSE_SPAWN_EGG.parseItem(),
                Arrays.asList("&7- Exp bonus for [calc(%horsetamer_attribute_exp-boost%*%level%)]",
                        "&7- Allow to store horse into saddle [Level %horsetamer_attribute_call%]"), 15);

        addAttribute("exp-boost", 20); // boost in percent
        addAttribute("call", 10); // Allow player store horse in saddle (in level)

        loadDefaultIconTemplate();
    }

    public ItemStack storeHorse(Player tamer, Horse horse){
        ItemStack saddle = tamer.getInventory().getItemInMainHand();
        ItemMeta meta = saddle.getItemMeta();
        if (!saddle.getType().equals(XMaterial.SADDLE.parseMaterial())){
            return null;
        }
        return saddle;
    }

}
