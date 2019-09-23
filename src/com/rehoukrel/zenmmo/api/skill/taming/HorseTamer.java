package com.rehoukrel.zenmmo.api.skill.taming;

import com.rehoukrel.zenmmo.api.Skill;
import com.rehoukrel.zenmmo.utils.XMaterial;
import org.bukkit.ChatColor;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class HorseTamer extends Skill {

    /*Arrays.asList("&7- Exp bonus for [calc(%horsetamer_attribute_exp-bonus%+%level%/2)]",
            "&7- Hit to store horse into saddle [Level %horsetamer_attribute_call%]")*/

    public HorseTamer() {
        super("HorseTamer", XMaterial.HORSE_SPAWN_EGG.parseItem(),
                Arrays.asList("&7- Exp bonus for [calc(%horsetamer_attribute_exp-bonus%+%level%/2)]"), 15);

        addAttribute("exp-bonus", 0); // exp bonus in amount
        //addAttribute("call", 10); // Allow player store horse in saddle (in level)

        loadPlaceholderAttribute();
        loadDefaultIconTemplate();
    }

    public void storeHorse(Player tamer, Horse horse){
        if (horse.getInventory().getSaddle() == null) {
            ItemStack off = tamer.getInventory().getItemInOffHand();
            ItemMeta meta = off.getItemMeta();
            List<String> lore = new ArrayList<>();
            String horseLore = ChatColor.translateAlternateColorCodes('&', "&7Horse ID: ");
            if (meta.hasLore()) {
                lore = meta.getLore();
            }
            boolean has = false;
            String id = UUID.randomUUID().toString();
            for (String s : lore) {
                if (s.startsWith(horseLore)) {
                    has = true;
                    break;
                }
            }
            if (!has) {

            }
        }
    }

}
