package com.rehoukrel.zenmmo.api;

import com.rehoukrel.zenmmo.ZenMMO;
import com.rehoukrel.zenmmo.utils.ConfigManager;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlayerData {

    private ZenMMO plugin = ZenMMO.getPlugin(ZenMMO.class);
    private File folder = new File(plugin.getDataFolder(), "PlayerData");

    private OfflinePlayer player;
    private File file;
    private ConfigManager configManager;
    private HashMap<String, SkillTree> skillTree = new HashMap<>();
    private HashMap<String, Object> rawData = new HashMap<>();
    private int maxSkillTree = 0;

    public PlayerData(OfflinePlayer player){
        if (!player.hasPlayedBefore()){
            return;
        }
        this.player = player;
        if (!getFolder().exists()){
            getFolder().mkdir();
        }
        this.file = new File(folder, player.getUniqueId().toString() + ".yml");
        this.configManager = new ConfigManager(plugin, getFile());

        initializeDefaultData();
        loadData();
    }

    // Action

    public void chooseSkillTree(SkillTree tree){
        List<String> st = new ArrayList<>();
        if (getConfigManager().contains("player-data.skill-tree")){
            st = getConfigManager().getConfig().getStringList("player-data.skill-tree");
            if (!(st.size() < getMaxSkillTree())){
                if (getPlayer().isOnline()){
                    getPlayer().getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[ You has reached maximum skill tree ]"));
                }
                return;
            }
            if (st.contains(tree.getName())){
                if (getPlayer().isOnline()){
                    getPlayer().getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[ You already choose this skill tree ]"));
                }
                return;
            }
            st.add(tree.getName());
        }else{
            if (st.size() < getMaxSkillTree()) {
                st.add(tree.getName());
            }
        }
        getConfigManager().inputAndSave("player-data.skill-tree", st);
        initializeSkillTreeData(tree);
        if (getPlayer().isOnline()){
            getPlayer().getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[ You have choosen skill tree " + tree.getName() + " ]"));
        }
    }

    // Startup

    public void loadData(){
        getRawData().clear();
        for (String path : plugin.getConfig().getConfigurationSection("player-data").getKeys(false)){
            String pt = "player-data." + path;
            getRawData().put(path, getConfigManager().getConfig().get(pt));
        }
        getSkillTree().clear();
        for (String path : getConfigManager().getConfig().getStringList("player-data.skill-tree")){
            getSkillTree().put(path, SkillTree.skillTree.get(path));
        }

        maxSkillTree = Integer.parseInt(getRawData().get("max-skill-tree").toString());
        initializeSkillTreeData();
    }

    public void initializeSkillTreeData(SkillTree tree){
        for (Skill sk : tree.getSkills().values()){
            String path = "skill-tree." + tree.getName();
            getConfigManager().init(path + ".level", 1);
            getConfigManager().init(path + ".exp", 0);
            getConfigManager().init(path + ".skill." + sk.getName(), 1);
        }
        getConfigManager().saveConfig();
    }

    public void initializeSkillTreeData(){
        for (String s : getSkillTree().keySet()){
            SkillTree tree = getSkillTree().get(s);
            for (Skill sk : tree.getSkills().values()){
                String path = "skill-tree." + tree.getName();
                getConfigManager().init(path + ".level", 1);
                getConfigManager().init(path + ".exp", 0);
                getConfigManager().init(path + ".skill." + sk.getName(), 1);
            }
        }
        getConfigManager().saveConfig();
    }

    public void initializeDefaultData(){
        for (String path : plugin.getConfig().getConfigurationSection("player-data").getKeys(false)){
            String pt = "player-data." + path;
            getConfigManager().init(pt, plugin.getConfig().get(pt));
        }
        getConfigManager().saveConfig();
    }

    // Getter


    public HashMap<String, Object> getRawData() {
        return rawData;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public OfflinePlayer getPlayer() {
        return player;
    }

    public int getMaxSkillTree() {
        return maxSkillTree;
    }

    public HashMap<String, SkillTree> getSkillTree() {
        return skillTree;
    }

    public File getFolder() {
        return folder;
    }

    public File getFile() {
        return file;
    }
}