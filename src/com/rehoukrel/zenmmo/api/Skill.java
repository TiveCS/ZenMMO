package com.rehoukrel.zenmmo.api;

import com.rehoukrel.zenmmo.ZenMMO;
import com.rehoukrel.zenmmo.utils.DataConverter;
import com.rehoukrel.zenmmo.utils.language.Placeholder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class Skill {

    private ZenMMO plugin = ZenMMO.getPlugin(ZenMMO.class);
    public static HashMap<String, Skill> skills = new HashMap<>();
    private Placeholder plc = new Placeholder();

    private PlayerData playerData;
    private double exp = 0;
    private int level = 0;

    private String path;
    private String name;
    private int maxLevel = 15;
    private ItemStack icon;
    private List<String> description = new ArrayList<>();
    private SkillTree connectedTree;
    private HashMap<String, Double> attribute = new HashMap<>();
    private HashMap<String, Object> customAttribute = new HashMap<>();

    // Player
    public Skill(PlayerData playerData){
        this.playerData = playerData;

        getCreatorData();
        this.level = Integer.parseInt(playerData.getConfigManager().get("skill-tree." + getConnectedTree().getName() + "." + getName()).toString());

        loadAttribute();
        loadCustomAttribute();
        loadPlaceholderAttribute(playerData);
        loadDefaultIconTemplate();

        getIcon().setAmount(getLevel());
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
        this.maxLevel = maxLevel;
        this.path = "skills." + name;

        getPlaceholder().addReplacer(name + "_max_level".toLowerCase(), maxLevel + "");
    }

    // Base

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
        for (String tree : pd.getConfigManager().getConfig().getConfigurationSection("skill-tree").getKeys(false)){
            getPlaceholder().addReplacer(("skill_" + tree + "_level").toLowerCase(), pd.getConfigManager().get("skill-tree." + tree + ".level").toString());
            getPlaceholder().addReplacer(("skill_" + tree + "_exp").toLowerCase(), pd.getConfigManager().get("skill-tree." + tree + ".exp").toString());
            for (String skill : pd.getConfigManager().getConfig().getConfigurationSection("skill-tree." + tree).getKeys(false)){
                String path = "skill-tree." + tree + "." + skill;
                getPlaceholder().addReplacer(("skill_" + tree + "_" + skill).toLowerCase(), pd.getConfigManager().get(path).toString());
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
        for (String s : getAttribute().keySet()){
            getPlaceholder().addReplacer((getName() + "_attribute_" + s).toLowerCase(), getAttribute().get(s) + "");
        }
        for (String s : getCustomAttribute().keySet()){
            getPlaceholder().addReplacer((getName() + "_custom_attribute_" + s).toLowerCase(), getCustomAttribute().get(s) + "");
        }
    }

    public void loadDefaultIconTemplate(){
        loadPlaceholderAttribute();
        ItemMeta meta = getIcon().getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&e" + getName()));
        List<String> lore = new ArrayList<>();
        lore.add(" ");
        lore.add("&fMax level of this skill &b" + getMaxLevel());
        lore.add(" ");
        lore.add("&e&lDESCRIPTION");
        lore.addAll(getDescription());
        lore = getPlaceholder().useMass(lore);
        meta.setLore(DataConverter.colored(lore));
        getIcon().setItemMeta(meta);
    }

    // Player action

    // Load attribute from player's file
    public void loadCustomAttribute(){

    }

    // Load attribute from player's file
    public void loadAttribute(){

    }

    public void setExp(double exp) {
        this.exp = exp;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setPlayerData(PlayerData playerData) {
        this.playerData = playerData;
    }


    // Creator action


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

    // Player getter

    public PlayerData getPlayerData() {
        return playerData;
    }

    public double getExp() {
        return exp;
    }

    public int getLevel() {
        return level;
    }


    // Creator getter

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

    public Placeholder getPlaceholder(){
        return plc;
    }
}
