package com.rehoukrel.zenmmo.cmd;

import com.rehoukrel.zenmmo.ZenMMO;
import com.rehoukrel.zenmmo.api.PlayerData;
import com.rehoukrel.zenmmo.api.SkillTree;
import com.rehoukrel.zenmmo.menu.MainMenu;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdZenMMO implements CommandExecutor {

    private ZenMMO plugin = ZenMMO.getPlugin(ZenMMO.class);


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (command.getName().equals("zenmmo")){
            if (commandSender instanceof Player){
                Player p = (Player) commandSender;
                if (strings.length == 0){
                    PlayerData pd = new PlayerData(p);
                    pd.openMenu();
                    return true;
                }
                if (strings.length == 2){
                    if (strings[0].equalsIgnoreCase("stats")){
                        PlayerData pd = new PlayerData(p);
                        if (pd.getSkillTree().containsKey(strings[1])){
                            SkillTree tree = pd.getSkillTree().get(strings[1]);
                            p.sendMessage("Level " + tree.getLevel());
                            p.sendMessage("Exp " + tree.getExp());
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }
}
