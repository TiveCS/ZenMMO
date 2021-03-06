package com.rehoukrel.zenmmo.api;

import com.rehoukrel.zenmmo.ZenMMO;
import com.rehoukrel.zenmmo.utils.DataConverter;
import com.rehoukrel.zenmmo.utils.language.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class Skill implements Cloneable {

    private ZenMMO plugin = ZenMMO.getPlugin(ZenMMO.class);
    public static HashMap<String, Skill> skills = new HashMap<>();
    private Placeholder plc = new Placeholder();

    private PlayerData playerData;
    private int level = 0;

    private boolean manualUpgrade = true, enable = true;
    private String path;
    private String name;
    private int maxLevel = 15;
    private ItemStack icon;
    private List<String> description = new ArrayList<>(), rawDescription = new ArrayList<>();
    private SkillTree connectedTree;
    private HashMap<Skill, Integer> requiredSkill = new HashMap<Skill, Integer>();
    private HashMap<String, Double> attribute = new HashMap<>();
    private HashMap<String, Object> customAttribute = new HashMap<>();

    // Player
    public Skill(PlayerData playerData){
        this.playerData = playerData;
        this.plc = playerData.getPlaceholder();

        getCreatorData();
        this.level = Integer.parseInt(playerData.getConfigManager().get("skill-tree." + getConnectedTree().getName() + "." + getName()).toString());

        loadPlaceholderAttribute(playerData);
        loadDefaultIconTemplate();

        getIcon().setAmount(getLevel() > 0 ? getLevel() : 1);
    }

    public Skill(OfflinePlayer player){
        this(new PlayerData(player));
    }

    // Creator
    public Skill(String name, Material material, List<String> description, int maxLevel){
        this(name, new ItemStack(material), description, maxLevel);
        loadDefaultIconTemplate();
    }
    public Skill(String name, ItemStack icon, List<String> description, int maxLevel){
        this.name = name;
        this.icon = icon;
        this.description = description;
        this.rawDescription = description;
        this.maxLevel = maxLevel;
        this.path = "skills." + name;

        getPlaceholder().addReplacer(name + "_max_level".toLowerCase(), maxLevel + "");
    }

    // Base

    public Skill getPlayerProfile(PlayerData pd){
        Skill skill = null;
        try {
            skill = (Skill) super.clone();
            skill.connectedTree = this.getConnectedTree();
            skill.maxLevel = this.getMaxLevel();
            skill.name = this.getName();
            skill.description = this.getDescription();
            skill.rawDescription = this.rawDescription;
            skill.attribute = this.getAttribute();
            skill.customAttribute = this.getCustomAttribute();
            skill.manualUpgrade = this.isManualUpgrade();
            skill.requiredSkill = this.getRequiredSkill();

        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        skill.setPlayerData(pd);
        skill.setLevel(pd.getConfigManager().getConfig().getInt("skill-tree." + this.getConnectedTree().getName() + ".skill." + skill.getName()));

        skill.setPlaceholder(new Placeholder());
        skill.loadPlaceholderAttribute(pd);

        skill.setIcon(this.icon.clone());
        skill.loadPlayerDefaultIconTemplate();

        return skill;
    }

    public Skill clone(){
        Skill skill = null;
        try {
            skill = (Skill) super.clone();
            skill.connectedTree = this.getConnectedTree();
            skill.maxLevel = this.getMaxLevel();
            skill.name = this.getName();
            skill.description = this.getDescription();
            skill.rawDescription = this.rawDescription;
            skill.attribute = this.getAttribute();
            skill.customAttribute = this.getCustomAttribute();
            skill.manualUpgrade = this.isManualUpgrade();
            skill.requiredSkill = this.getRequiredSkill();

            skill.setPlaceholder(this.getPlaceholder());
            skill.setPlayerData(this.getPlayerData());
            skill.setLevel(this.getLevel());
            skill.setIcon(this.getIcon().clone());
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return skill;
    }

    public void getCreatorData(){
        for (Skill sk : skills.values()){
            if (sk.getClass().isAssignableFrom(this.getClass())){
                this.setDescription(sk.getDescription());
                this.setName(sk.getName());
                this.setMaxLevel(sk.getMaxLevel());
                this.setConnectedTree(sk.getConnectedTree());
                break;
            }
        }
    }

    public void loadPlaceholderAttribute(PlayerData pd){
        getPlaceholder().addReplacer("level", getLevel() + "");
        getPlaceholder().addReplacer("max_level", getMaxLevel() + "");
        getPlaceholder().addReplacer(getName() + "_level", getLevel() + "");
        getPlaceholder().addReplacer(getName() + "_maxlevel", getMaxLevel() + "");
        getPlaceholder().addReplacer("player", pd.getPlayer().getName());
        getPlaceholder().addReplacer("uuid", pd.getPlayer().getUniqueId().toString());

        for (String tree : pd.getConfigManager().getConfig().getConfigurationSection("skill-tree").getKeys(false)){
            getPlaceholder().addReplacer(("skilltree_" + tree + "_level").toLowerCase(), pd.getConfigManager().get("skill-tree." + tree + ".level").toString());
            getPlaceholder().addReplacer(("skilltree_" + tree + "_exp").toLowerCase(), pd.getConfigManager().get("skill-tree." + tree + ".exp").toString());
            for (String skill : pd.getConfigManager().getConfig().getConfigurationSection("skill-tree." + tree).getKeys(false)){
                String path = "skill-tree." + tree + "." + skill;
                getPlaceholder().addReplacer(("skilltree_" + tree + "_" + skill).toLowerCase(), pd.getConfigManager().get(path).toString());
            }
        }
        for (String s : skills.get(getName()).getAttribute().keySet()){
            getPlaceholder().addReplacer((getName() + "_attribute_" + s).toLowerCase(), getAttribute().get(s) + "");
        }
        for (String s : skills.get(getName()).getCustomAttribute().keySet()){
            getPlaceholder().addReplacer((getName() + "_custom_attribute_" + s).toLowerCase(), getCustomAttribute().get(s) + "");
        }
    }

    public void loadPlaceholderAttribute(){
        getPlaceholder().addReplacer("level", "0");
        for (String s : getAttribute().keySet()){
            String a = getAttribute().get(s) + "";
            if (a.endsWith(".0")){
                a = a.substring(0, a.lastIndexOf(".0"));
            }
            getPlaceholder().addReplacer((getName() + "_attribute_" + s).toLowerCase(), a + "");
            getPlaceholder().addReplacer((s).toLowerCase(), a + "");
        }
        for (String s : getCustomAttribute().keySet()){
            getPlaceholder().addReplacer((getName() + "_custom_attribute_" + s).toLowerCase(), getCustomAttribute().get(s) + "");
            getPlaceholder().addReplacer((s).toLowerCase(), getCustomAttribute().get(s) + "");
        }
    }

    public void loadPlayerDefaultIconTemplate(){
        getDescription().clear();
        getDescription().addAll(this.rawDescription);
        loadPlaceholderAttribute(getPlayerData());
        ItemMeta meta = getIcon().getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6" + getName() + " &7(Level " + getLevel() + " / " + getMaxLevel() + " )"));
        List<String> lore = new ArrayList<>();
        lore.add(" ");

        loadRequirementDescription(lore);

        lore.add("&e&lDESCRIPTION");
        lore.addAll(getDescription());
        if (!isManualUpgrade()){
            lore.add(" ");
            lore.add("&cCannot manual upgrade");
        }
        lore = getPlaceholder().useMass(lore);
        for (int i = 0; i < lore.size(); i++){
            lore.set(i, DataConverter.calculateString(lore.get(i), 1));
        }
        meta.setLore(DataConverter.colored(lore));
        getIcon().setItemMeta(meta);
    }

    public void loadRequirementDescription(List<String> lore){
        if ((getRequiredSkill().size() > 0)) {
            lore.add("&4&lREQUIREMENT");
            if (getRequiredSkill().size() > 0) {
                StringBuilder b = new StringBuilder();
                int count = 0;
                for (Skill s : getRequiredSkill().keySet()){
                    String r = "&c" + s.getName() + "("+ getRequiredSkill().get(s) +")" + ((count == getRequiredSkill().size() - 1) ? "&7, " : "");
                    b.append(r);
                    count++;
                }
                lore.add(b.toString());
            }
        }
    }

    public void loadDefaultIconTemplate(){
        loadPlaceholderAttribute();
        ItemMeta meta = getIcon().getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6" + getName()));
        List<String> lore = new ArrayList<>();
        lore.add(" ");
        lore.add("&fMax level of this skill &b" + getMaxLevel());
        lore.add(" ");

        loadRequirementDescription(lore);

        lore.add("&e&lDESCRIPTION");
        lore.addAll(getDescription());
        if (!isManualUpgrade()){
            lore.add(" ");
            lore.add("&cCannot manual upgrade");
        }
        lore = getPlaceholder().useMass(lore);
        for (int i = 0; i < lore.size(); i++){
            lore.set(i, DataConverter.calculateString(lore.get(i), 1));
        }
        meta.setLore(DataConverter.colored(lore));
        getIcon().setItemMeta(meta);
    }

    // Player action

    // Admin use only
    public void levelupTo(int newlevel){
        setLevel(newlevel);
    }

    // Manual upgrade (player action) [ isManualUpgrade() is checked here ]
    public void levelup(int addlevel){
        HashMap<String, Skill> psk = getPlayerData().getSkillTree().get(getConnectedTree().getName()).getSkills();
        if (getRequiredSkill().size() > 0) {
            for (Skill sk : getRequiredSkill().keySet()) {
                if (!(psk.get(sk).getLevel() >= getRequiredSkill().get(sk))) {
                    getPlayerData().getPlayer().getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cRequired skill " + sk.getName() + " Level " + sk.getLevel() + " to upgrade this skill"));
                    return;
                }
            }
        }
        if (!isManualUpgrade()){
            getPlayerData().getPlayer().getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cCannot manual upgrade this skill"));
            return;
        }else {
            getPlayerData().getPlayer().getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[ Skill " + getName() + " on tree " + getConnectedTree().getName() + " has been level up &7]"));
            levelupTo(getLevel() + addlevel);
        }
    }
    public void levelup(){
        levelup(1);
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setPlayerData(PlayerData playerData) {
        this.playerData = playerData;
    }


    // Creator action

    public void addRequiredSkill(Skill sk, int skillLevel){
        getRequiredSkill().put(sk, skillLevel);
    }

    public void setPlaceholder(Placeholder plc){
        this.plc = plc;
    }

    public void setConnectedTree(SkillTree connectedTree) {
        this.connectedTree = connectedTree;
    }

    public void setCustomAttribute(HashMap<String, Object> customAttribute) {
        this.customAttribute = customAttribute;
    }

    public void setAttribute(HashMap<String, Double> attribute) {
        this.attribute = attribute;
    }

    public void addCustomAttribute(String path, Object obj){
        this.customAttribute.put(path, obj);
    }

    public void addAttribute(String path, double value){
        this.attribute.put(path, value);
    }

    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIcon(ItemStack icon) {
        this.icon = icon;
    }

    public void setDescription(List<String> description) {
        this.description = description;
    }

    public void setManualUpgrade(boolean manualUpgrade) {
        this.manualUpgrade = manualUpgrade;
    }

    public void setRequiredSkill(HashMap<Skill, Integer> requiredSkill) {
        this.requiredSkill = requiredSkill;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public void setRawDescription(List<String> rawDescription) {
        this.rawDescription = rawDescription;
    }

    // Player getter

    public PlayerData getPlayerData() {
        return playerData;
    }

    public int getLevel() {
        return level;
    }


    // Creator getter


    public List<String> getRawDescription() {
        return rawDescription;
    }

    public boolean isEnable() {
        return enable;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public SkillTree getConnectedTree() {
        return connectedTree;
    }

    public List<String> getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public ItemStack getIcon() {
        return icon;
    }

    public HashMap<String, Object> getCustomAttribute() {
        return customAttribute;
    }

    public HashMap<String, Double> getAttribute() {
        return attribute;
    }

    public String getPath() {
        return path;
    }

    public String getAutoPath(){
        return "skill-tree." + getConnectedTree().getName() + ".skill." + getName();
    }

    public Placeholder getPlaceholder(){
        return plc;
    }

    public boolean isManualUpgrade() {
        return manualUpgrade;
    }

    public HashMap<Skill, Integer> getRequiredSkill() {
        return requiredSkill;
    }
}
