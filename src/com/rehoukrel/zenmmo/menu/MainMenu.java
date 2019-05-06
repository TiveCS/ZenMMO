package com.rehoukrel.zenmmo.menu;

import com.rehoukrel.zenmmo.api.PlayerData;
import com.rehoukrel.zenmmo.api.Skill;
import com.rehoukrel.zenmmo.api.SkillTree;
import com.rehoukrel.zenmmo.utils.DataConverter;
import com.rehoukrel.zenmmo.utils.XMaterial;
import com.rehoukrel.zenmmo.utils.menu.UneditableMenu;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MainMenu extends UneditableMenu {

    private OfflinePlayer target;
    private PlayerData pd;
    private Player viewer;
    private MainMenuState state = MainMenuState.MAIN;
    private List<SkillTree> trees = new ArrayList<>(SkillTree.skillTree.values());

    public enum MainMenuState{
        MAIN, SKILL_TREE, SKILL;
    }

    public MainMenu(OfflinePlayer target, Player viewer) {
        super(6, "&2ZenMMO Main Menu");
        if (!target.hasPlayedBefore() || target.getUniqueId() == null){
            return;
        }
        this.pd = new PlayerData(target);
        this.target = target;
        this.viewer = viewer;
        loadTemplate();

    }

    public MainMenu(PlayerData playerData, Player viewer) {
        super(6, "&2ZenMMO Main Menu");

        this.pd = playerData;
        this.target = playerData.getPlayer();
        this.viewer = viewer;
        loadTemplate();

    }

    public void reset(){
        setPage(1);
        getMenu().clear();
        getInventoryData().clear();
        loadTemplate();
        open(viewer);
        state = MainMenuState.MAIN;
    }

    public void loadTemplate(){
        HashMap<Integer, ItemStack> map = new HashMap<>();
        ItemStack border = XMaterial.BLACK_STAINED_GLASS_PANE.parseItem();
        ItemMeta b = border.getItemMeta();
        b.setDisplayName(" ");
        border.setItemMeta(b);

        for (int i = 0; i < getMenu().getSize()/9; i++) {
            for (int s = 0; s < 9; s++) {
                int slot = i*9 + s;
                if (i == 0 || i == 5) {
                    map.put(slot, border);
                } else {
                    if (s == 1 || s == 7) {
                        if (i == 2 || i == 3){
                            if (s == 7) {
                                map.put(slot, new ItemStack(XMaterial.LIME_STAINED_GLASS_PANE.parseItem()){{
                                    ItemMeta meta = this.getItemMeta();
                                    meta.setDisplayName(ChatColor.GREEN + "Next Page");
                                    this.setItemMeta(meta);
                                }});
                            }else{
                                map.put(slot, new ItemStack(XMaterial.LIME_STAINED_GLASS_PANE.parseItem()){{
                                    ItemMeta meta = this.getItemMeta();
                                    meta.setDisplayName(ChatColor.GREEN + "Previous Page");
                                    this.setItemMeta(meta);
                                }});
                            }
                        }else {
                            map.put(slot, border);
                        }
                    }
                }
            }
        }

        // Left

        map.put(9, new ItemStack(XMaterial.COMMAND_BLOCK.parseItem()){{
            ItemMeta meta = this.getItemMeta();
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&7&lCommand Help"));
            meta.setLore(DataConverter.colored(Arrays.asList(" ", "&fClick here to show")));
            this.setItemMeta(meta);
        }});

        map.put(18, new ItemStack(XMaterial.COMPASS.parseItem()){{
            ItemMeta meta = this.getItemMeta();
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&3&l&lSelect Skill Tree"));
            meta.setLore(DataConverter.colored(Arrays.asList(" ", "&eLeft-Click &fto &7Show&f Skill Tree", "&6Right-Click &fto &7Choose &fSkill Tree", " ")));
            this.setItemMeta(meta);
        }});

        // Right

        map.put(17, new ItemStack(Material.BOOK){{
            ItemMeta meta = this.getItemMeta();
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&e&lSkill Tree"));
            this.setItemMeta(meta);
        }});

        map.put(4, new ItemStack(Material.SIGN){{
            ItemMeta meta = this.getItemMeta();
            meta.setDisplayName(ChatColor.AQUA + "" + target.getName() + "'s Stats");
            meta.setLore(DataConverter.colored(Arrays.asList(" ",
                    "&7SkillTree ( " + pd.getConfigManager().getConfig().getStringList("player-data.skill-tree").size() +" / " + pd.getMaxSkillTree() + " )",
                    "")));
            this.setItemMeta(meta);
        }});

        map.put(49, new ItemStack(XMaterial.RED_STAINED_GLASS_PANE.parseItem()){{
            ItemMeta meta = getItemMeta();
            meta.setDisplayName(ChatColor.RED + "Back");
            setItemMeta(meta);
        }});
        addInventoryData(0, map);
    }

    public void clearContent(){
        for (int i = 1; i < 5; i++){
            for (int s = 2; s < 7;s++){
                int slot = i*9 + s;
                getMenu().setItem(slot, new ItemStack(Material.AIR));
            }
        }
    }

    public void loadSkill(SkillTree tree){
        clearContent();
        setPage(1);
        HashMap<Integer, ItemStack> map = new HashMap<>();
        List<Skill> skills = new ArrayList<>(tree.getSkills().values());
        int size = 5*4;
        int count = (size*getPage() - (size));
        for (int i = 1; i < 5; i++){
            for (int s = 2; s < 7; s++){
                if (skills.size() > count) {
                    int slot = i * 9 + s;
                    map.put(slot, skills.get(count).getIcon());
                    count++;
                }else{
                    break;
                }
            }
        }
        addInventoryData(1, map);
        open(viewer);
    }

    public void loadSkillTree(){
        clearContent();
        HashMap<Integer, ItemStack> map = new HashMap<>();
        setPage(1);
        int size = 5*4;
        int count = (size*getPage() - (size));
        for (int i = 1; i < 5; i++){
            for (int s = 2; s < 7; s++){
                if (trees.size() > count) {
                    int slot = i * 9 + s;
                    ItemStack icon = trees.get(count).getIcon();
                    if (pd.getSkillTree().containsKey(trees.get(count))){
                        ItemMeta meta = icon.getItemMeta();
                        meta.addEnchant(Enchantment.KNOCKBACK, 1, true);
                        //meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                        icon.setItemMeta(meta);
                    }else if (pd.getMaxSkillTree() - pd.getSkillTree().size() <= 0){
                        icon.setType(Material.BARRIER);
                    }
                    map.put(slot, icon);
                    count++;
                }else{
                    break;
                }
            }
        }
        addInventoryData(getPage(), map);
        open(viewer);
        state = MainMenuState.SKILL_TREE;
    }

    @Override
    public void actionClick(InventoryClickEvent event) {
        int slot = event.getSlot();
        ItemStack item = event.getClickedInventory().getItem(slot);

        if (slot == 49) {
            reset();
        }
        if (slot == 17) {
            loadSkillTree();
        }
        if (slot == (2 * 9 + 7) || slot == (3 * 9 + 7)) {
            nextPage((Player) event.getWhoClicked());
        }
        if (slot == (2 * 9 + 1) || slot == (3 * 9 + 1)) {
            if (getPage() > 1) {
                previousPage((Player) event.getWhoClicked());
            }
        }

        //------------------------------------------------
        else if (state.equals(MainMenuState.SKILL_TREE)){
            for (SkillTree st : trees){
                if (st.getIcon().equals(item)){
                    if (event.getClick().isLeftClick()) {
                        loadSkill(st);
                        state = MainMenuState.SKILL;
                    }else if (event.getClick().isRightClick()){
                        pd.chooseSkillTree(st);
                    }
                    break;
                }
            }
        }
    }
}