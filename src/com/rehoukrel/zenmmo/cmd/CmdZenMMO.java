package com.rehoukrel.zenmmo.cmd;

import com.rehoukrel.zenmmo.ZenMMO;
import com.rehoukrel.zenmmo.api.PlayerData;
import com.rehoukrel.zenmmo.menu.MainMenu;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdZenMMO implements CommandExecutor {

    private ZenMMO plugin = ZenMMO.getPlugin(ZenMMO.class);

    public void menu(Player player){
        MainMenu mm = new MainMenu(player, player);
        mm.open(player);
    }

    public void menuOther(OfflinePlayer target, Player viewer){
        MainMenu mm = new MainMenu(target, viewer);
        mm.open(viewer);
    }

    public void menuOther(PlayerData target, Player viewer){
        MainMenu mm = new MainMenu(target, viewer);
        mm.open(viewer);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (command.getName().equals("zenmmo")){
            if (commandSender instanceof Player){
                Player p = (Player) commandSender;
                PlayerData pd = new PlayerData(p);
                if (strings.length == 0){
                    menu(p);
                    return true;
                }
                if (strings.length == 1){
                    p.sendMessage(pd.getSkillTree().toString());
                    return true;
                }
            }
        }
        return false;
    }
}
