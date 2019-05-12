package com.rehoukrel.zenmmo.api;

import com.rehoukrel.zenmmo.ZenMMO;
import com.rehoukrel.zenmmo.utils.ConfigManager;
import com.rehoukrel.zenmmo.utils.DataConverter;
import com.rehoukrel.zenmmo.utils.MessageManager;
import com.rehoukrel.zenmmo.utils.language.Placeholder;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.xml.crypto.Data;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public abstract class SkillTree implements Cloneable {

    protected ZenMMO plugin = ZenMMO.getPlugin(ZenMMO.class);
    protected File folder = new File(plugin.getDataFolder(), "SkillTree");
    public static HashMap<String, SkillTree> skillTree = new HashMap<>();
    String format = plugin.getConfig().getString("system.progress-format");
    String progressType = plugin.getConfig().getString("system.progress-bar");
    int addSp = plugin.getConfig().getInt("system.additional-levelup-skillpoint");

    private int level, exp, skillPoint = 0;
    private PlayerData playerData;

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

    //----------------- EVENT -----------------------

    public abstract void onBlockBreak(BlockBreakEvent event);
    public abstract void onEntityBowShoot(EntityShootBowEvent event);
    public abstract void onEntityDamage(EntityDamageByEntityEvent event);
    public abstract void onPlayerFish(PlayerFishEvent event);
    public abstract void onTame(EntityTameEvent event);

    //------------------------------------------------

    public void showProgress(Player p){

        String o = format;
        Placeholder pc = new Placeholder();
        double e = getExp(), me = getMaxExp();
        double percentage = e/me*100;
        pc.addReplacer("level", getLevel() + "");
        pc.addReplacer("skill_tree", getName() + "");
        pc.addReplacer("exp", getExp() + "");
        pc.addReplacer("exp_max", getMaxExp() + "");
        pc.addReplacer("exp_percentage", DataConverter.returnDecimalFormated(1, percentage));
        pc.addReplacer("progress_bar", generateProgressBar(percentage));

        TextComponent com = new TextComponent(ChatColor.translateAlternateColorCodes('&', "&f" + pc.use(o)));
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, com);
    }

    public int generateExp(){
        int v = 0;
        return v;
    }

    public void addExp(){
        int min = plugin.getConfig().getInt("system.exp-min"), max = plugin.getConfig().getInt("system.exp-max");
        int r = new Random().nextInt(max);
        if (r < min){
            r = min;
        }
        addExp(r);
    }

    public void addExp(int add){
        if (getExp() + add >= getMaxExp()){
            setExp(0);
            levelup();
            getPlayerData().getConfigManager().inputAndSave("skill-tree." + this.getName() + ".exp", 0);
        }else{
            getPlayerData().getConfigManager().inputAndSave("skill-tree." + this.getName() + ".exp", getExp() + add);
            setExp(getPlayerData().getConfigManager().getConfig().getInt("skill-tree." + this.getName() + ".exp"));
        }
    }

    public void addSkillPoint(){
        addSkillPoint(addSp);
    }

    public void addSkillPoint(int sp){
        setSkillPoint(getSkillPoint() + sp);
        getPlayerData().getConfigManager().inputAndSave("skill-tree." + this.getName() + ".skill-point", getSkillPoint());
    }

    public void levelup(){
        addSkillPoint();
        setLevel(getLevel() + 1);
        getPlayerData().getConfigManager().inputAndSave("skill-tree." + this.getName() + ".level", getLevel());
        if (getPlayerData().getPlayer().isOnline()) {
            DataConverter.playSoundByString(getPlayerData().getPlayer().getPlayer().getLocation(), plugin.getConfig().getString("sounds.levelup"));
            getPlayerData().getPlayer().getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[ &e" + getName()+ " skill tree level is &6" + getLevel() +" &7]"));
        }
    }

    public String generateProgressBar(double percentage){
        String path = "progress-bar." + progressType;
        int size = plugin.getConfig().getInt(path + ".size");
        String symbol = plugin.getConfig().getString(path + ".symbol");
        double inc = 100 / size;
        double count = 0;
        int check = 0;
        String f = plugin.getConfig().getString(path + ".filled-prefix"), uf = plugin.getConfig().getString(path + ".unfilled-prefix");
        StringBuilder b = new StringBuilder();

        for (int i = 0; i < size; i++){
            count += inc;
            if (percentage >= count){
                if (b.length() == 0){
                    b.append(f + symbol);
                }else{
                    b.append(symbol);
                }
            }else{
                if (check == 0){
                    b.append(uf + symbol);
                    check = 1;
                }else{
                    b.append(symbol);
                }
            }
        }

        return ChatColor.translateAlternateColorCodes('&', b.toString());
    }

    public SkillTree getPlayerProfile(PlayerData playerData){
        SkillTree str = this.clone();
        String pt = "skill-tree." + str.getName();
        str.setPlayerData(playerData);
        str.setLevel(playerData.getConfigManager().getConfig().getInt(pt + ".level"));
        str.setExp(playerData.getConfigManager().getConfig().getInt(pt + ".exp"));
        str.setSkillPoint(playerData.getConfigManager().getConfig().getInt(pt + ".skill-point"));
        str.loadIconWithPlayer();
        return str;
    }

    public SkillTree clone() {
        SkillTree str = null;
        try {
            str = (SkillTree) super.clone();
            str.name = this.getName();
            str.description = this.getDescription();
            str.icon = this.getIcon();
            str.file = this.getFile();
            str.configManager = this.getConfigManager();
            str.skills = this.getSkills();
            for (String s : str.getSkills().keySet()){
                str.getSkills().put(s, str.getSkills().get(s).clone());
            }
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return str;
    }

    public void initializeDefaultData(){
        getConfigManager().init("enable", true);
        getConfigManager().init("description", getDescription());
        getConfigManager().saveConfig();
        if (getConfigManager().contains("description")){
            setDescription(getConfigManager().getConfig().getStringList("description"));
        }
    }

    public void loadIconWithPlayer(){
        ItemMeta meta = getIcon().getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a" + getName() +" &7(Level " + getLevel() + ") [&6" + DataConverter.returnDecimalFormated(1, getPercentage()) +"%&7]"));
        List<String> lore = new ArrayList<>();
        List<String> sk = new ArrayList<>();
        ConfigurationSection cs = getConfigManager().getConfig().getConfigurationSection("skills");
        if (getConfigManager().contains("skills")){
            sk = new ArrayList<>(cs.getKeys(false));
        }

        lore.add(" ");
        lore.add("&fSkill Point left &c" + getSkillPoint());
        lore.add(" ");
        lore.add("&fThis tree contains &b" + sk.size() + " &fskill(s)");
        lore.add(" ");
        lore.add("&e&lDESCRIPTION");
        lore.addAll(getDescription());
        meta.setLore(DataConverter.colored(lore));
        getIcon().setItemMeta(meta);
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

    // Setter

    public void setExp(int exp) {
        this.exp = exp;
    }

    public void setSkillPoint(int skillPoint) {
        this.skillPoint = skillPoint;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setPlayerData(PlayerData playerData) {
        this.playerData = playerData;
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

    public PlayerData getPlayerData() {
        return playerData;
    }

    public int getExp() {
        return exp;
    }

    public int getSkillPoint() {
        return skillPoint;
    }

    public int getLevel() {
        return level;
    }

    public double getPercentage(){
        double e = getExp(), me = getMaxExp();
        double percentage = e/me*100;
        return percentage;
    }

    public int getMaxExp(){
        String f = plugin.getConfig().getString("system.max-exp-formula");
        f = f.replace("%level%", getLevel() + "");
        f = f.replace("%exp%", getExp() + "");

        return DataConverter.convertStringToInt(f);
    }

}
