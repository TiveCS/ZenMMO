package com.rehoukrel.zenmmo.api.skill.taming;

import com.rehoukrel.zenmmo.api.Skill;
import com.rehoukrel.zenmmo.utils.XMaterial;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.List;

public class FriendlyAura extends Skill {
    public FriendlyAura() {
        super("FriendlyAura", XMaterial.OAK_SAPLING.parseMaterial(),
                Arrays.asList("&7- Can breed every Animal [Level %friendlyaura_attribute_tamer%",
                        "&7- Exp bonus after breed [calc(%friendlyaura_attribute_exp-boost%*%level%)]",
                        "&7- Restore hunger while around your animal [Level %friendlyaura_attribute_healthy%]"), 15);

        addAttribute("tamer", 1); // Always successful to breed or tame animal (value Required level)
        addAttribute("exp-boost", 5); // Percentage to increase breed or tame exp
        addAttribute("healthy", 15); // Restore hunger bar every given second while around your breeded animal (value = required level)
        addAttribute("healthy-interval", 50); // in seconds
        addAttribute("healthy-duration", 10); // in seconds
        addAttribute("healthy-restore", 1); // hunger bar point restore
    }
}
