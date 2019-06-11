package com.rehoukrel.zenmmo.api.skill.taming;

import com.rehoukrel.zenmmo.api.Skill;
import com.rehoukrel.zenmmo.utils.XMaterial;

import java.util.Arrays;

public class WolfMastery extends Skill {
    public WolfMastery() {
        super("WolfMastery", XMaterial.BONE.parseMaterial(),
                Arrays.asList("&7- Wolf Damage [+calc(%wolfmastery_attribute_damage%*%level%)]",
                        "&7- Wolf Max Health [+calc(%wolfmastery_attribute_max-health%*%level%)]"), 20);
        addAttribute("damage", 0.2); // Increase dmg of wolf by number (not percent)
        addAttribute("max-health", 0.5); // Increase max health of wolf by number
        addAttribute("locate", 10); // Locate far target by apply glowing (value = required level)
        addAttribute("locate-cooldown", 5); // Locate cooldown
        addAttribute("xp-heal", 18); // Heal wolf when player pickup exp (value = required level)
        addAttribute("xp-heal-amount", 0.5); // Heal amount
    }
}
