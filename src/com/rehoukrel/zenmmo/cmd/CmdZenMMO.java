package com.rehoukrel.zenmmo.cmd;

import com.rehoukrel.zenmmo.ZenMMO;
import com.rehoukrel.zenmmo.api.PlayerData;
import com.rehoukrel.zenmmo.api.Skill;
import com.rehoukrel.zenmmo.api.SkillTree;
import com.rehoukrel.zenmmo.event.BasicEvent;
import com.rehoukrel.zenmmo.menu.MainMenu;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CmdZenMMO implements CommandExecutor, TabCompleter {

    private ZenMMO plugin = ZenMMO.getPlugin(ZenMMO.class);


    @SuppressWarnings("deprecation")
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
                if (strings.length >= 2){
                    if (strings[0].equalsIgnoreCase("other")){
                        if (strings[1].length() > 0 && strings.length == 2){
                            OfflinePlayer op = Bukkit.getOfflinePlayer(strings[1]);
                            PlayerData pd = new PlayerData(op);
                            pd.openMenu(p);
                            pd.getMainMenu().setViewer(p);
                            return true;
                        }
                        if (strings.length >= 4){
                            if (p.hasPermission("zenmmo.command.admin")) {
                                OfflinePlayer op = Bukkit.getOfflinePlayer(strings[1]);
                                if (op.hasPlayedBefore()) {
                                    PlayerData popd = new PlayerData(op);
                                    if (strings[2].equalsIgnoreCase("skillpoint")) {
                                        try {
                                            int sp = Integer.parseInt(strings[5]);
                                            SkillTree tree = SkillTree.skillTree.get(strings[3]);
                                            if (popd.getSkillTree().containsKey(tree.getName())) {
                                                if (strings[4].equalsIgnoreCase("set")) {
                                                    tree.setSkillPoint(sp);
                                                } else if (strings[4].equalsIgnoreCase("add")) {
                                                    tree.setSkillPoint(tree.getSkillPoint() + sp);
                                                }
                                            }
                                        } catch (Exception ignored) {
                                        }
                                    }
                                    if (strings[2].equalsIgnoreCase("skilltree")) {
                                        SkillTree tree = SkillTree.skillTree.get(strings[3]);
                                        popd.chooseSkillTree(tree);
                                        return true;
                                    }
                                }
                            }
                        }
                    }
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

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("zenmmo") && commandSender instanceof Player){
            List<String> list = new ArrayList<>();
            if (strings.length == 1){
                list.add("stats");
                list.add("other");
                if (commandSender.hasPermission("zenmmo.command.admin")){
                    list.add("admin");
                }
                return list;
            }else if (strings.length == 2){
                if (strings[0].equalsIgnoreCase("stats")) {
                    PlayerData pd = BasicEvent.get((Player) commandSender);
                    list.addAll(pd.getSkillTree().keySet());
                    return list;
                }

            }
            return null;
        }
        return null;
    }
}
