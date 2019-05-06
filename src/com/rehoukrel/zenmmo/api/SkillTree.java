package com.rehoukrel.zenmmo.api;

import com.rehoukrel.zenmmo.ZenMMO;
import com.rehoukrel.zenmmo.utils.ConfigManager;
import com.rehoukrel.zenmmo.utils.DataConverter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class SkillTree {

    private ZenMMO plugin = ZenMMO.getPlugin(ZenMMO.class);
    private File folder = new File(plugin.getDataFolder(), "SkillTree");
    public static HashMap<String, SkillTree> skillTree = new HashMap<>();

    private File file;
    private ConfigManager configManager;
    private String name;
    private ItemStack icon;
    private HashMap<String, Skill> skills = new HashMap<>();
    private List<String> description = new ArrayList<>();

    public SkillTree(String name, Material icon, List<String> description){
        this.name = name;
        this.description = description;
        this.icon = new ItemStack(icon);
        if (!getFolder().exists()){
            getFolder().mkdir();
        }
        this.file = new File(getFolder(), getName() + ".yml");
        this.configManager = new ConfigManager(plugin, getFile());
        initializeDefaultData();
        loadIcon();
    }

    public SkillTree(String name, ItemStack icon, List<String> description){
        this.name = name;
        this.icon = icon;
        this.description = description;
        if (!getFolder().exists()){
            getFolder().mkdir();
        }
        this.file = new File(getFolder(), getName() + ".yml");
        this.configManager = new ConfigManager(plugin, getFile());
        initializeDefaultData();
    }

    //------------------------------------------------

    public void initializeDefaultData(){
        getConfigManager().init("enable", true);
        getConfigManager().init("description", getDescription());
        getConfigManager().saveConfig();
        if (getConfigManager().contains("description")){
            setDescription(getConfigManager().getConfig().getStringList("description"));
        }
    }

    public void loadIcon(){
        ItemMeta meta = getIcon().getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + getName());
        List<String> lore = new ArrayList<>();
        List<String> sk = new ArrayList<>();
        ConfigurationSection cs = getConfigManager().getConfig().getConfigurationSection("skills");
        if (getConfigManager().contains("skills")){
            sk = new ArrayList<>(cs.getKeys(false));
        }

        lore.add(" ");
        lore.add("&fThis tree contains &b" + sk.size() + " &fskill(s)");
        lore.add(" ");
        lore.add("&e&lDESCRIPTION");
        lore.addAll(getDescription());
        meta.setLore(DataConverter.colored(lore));
        getIcon().setItemMeta(meta);
    }

    public static void register(SkillTree... tree){
        for (SkillTree st : tree){
            skillTree.put(st.getName(), st);
            st.initializeDefaultData();
        }
    }

    //------------------------------------------------

    // Action

    public void addSkill(Skill... skills){
        for (Skill s : skills){
            this.skills.put(s.getName(), s);
            String path = "skills." + s.getName();
            getConfigManager().init(path + ".max-level", s.getMaxLevel());
            getConfigManager().init(path + ".description", s.getDescription());
            for (String a : s.getAttribute().keySet()) {
                getConfigManager().input(path + ".attribute." + a, s.getAttribute().get(a));
            }
            for (String a : s.getCustomAttribute().keySet()) {
                getConfigManager().input(path + ".custom-attribute." + a, s.getCustomAttribute().get(a));
            }
            s.setConnectedTree(this);
            Skill.skills.put(s.getName(), s);
        }
        getConfigManager().saveConfig();
    }

    public void setDescription(List<String> description) {
        this.description = description;
    }

    // Getter

    public List<String> getDescription() {
        return description;
    }

    public ItemStack getIcon() {
        return icon;
    }

    public File getFolder() {
        return folder;
    }

    public File getFile() {
        return file;
    }

    public HashMap<String, Skill> getSkills() {
        return skills;
    }

    public String getName() {
        return name;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }
}
