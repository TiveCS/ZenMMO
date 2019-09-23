package com.rehoukrel.zenmmo.utils.menu;

import com.rehoukrel.zenmmo.ZenMMO;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public abstract class UneditableMenu {

    public static HashMap<Player, UneditableMenu> menus = new HashMap<>();

    private ZenMMO plugin = ZenMMO.getPlugin(ZenMMO.class);
    private Inventory menu;
    private HashMap<Integer, HashMap<Integer, ItemStack>> inventoryData = new HashMap<>();
    private int page = 1;

    public UneditableMenu(int rows){
        this(rows, null);
    }

    public UneditableMenu(int rows, String title){
        if (title != null){
            this.menu = Bukkit.createInventory(null, rows*9, ChatColor.translateAlternateColorCodes('&', title));
        }else{
            this.menu = Bukkit.createInventory(null, rows*9);
        }
    }
    // Setting

    public void setPage(int page) {
        this.page = page;
    }

    public void setMenu(Inventory menu) {
        this.menu = menu;
    }

    public void setInventoryData(HashMap<Integer, HashMap<Integer, ItemStack>> inventoryData) {
        this.inventoryData = inventoryData;
    }

    public void addInventoryData(int page, HashMap<Integer, ItemStack> itemSlot){
        this.inventoryData.put(page, itemSlot);
    }

    //----------------------
    // Action

    public void loadInventoryData(int page){
        getMenu().clear();
        HashMap<Integer, ItemStack> itemSlot = getInventoryData().containsKey(page) ? getInventoryData().get(page) : new HashMap<>(),
                base = getInventoryData().containsKey(0) ? getInventoryData().get(0) : new HashMap<>();
        for (int slot : base.keySet()){
            getMenu().setItem(slot, base.get(slot));
        }
        for (int slot : itemSlot.keySet()){
            getMenu().setItem(slot, itemSlot.get(slot));
        }
    }

    public void previousPage(Player p){
        if (getPage() > 1) {
            setPage(getPage() - 1);
        }else{
            setPage(1);
        }
        open(p);
    }

    public void nextPage(Player p){
        setPage(getPage() + 1);
        open(p);
    }

    public void open(Player p){
        loadInventoryData(getPage());

        p.openInventory(getMenu());
        menus.put(p, this);
    }

    public abstract void actionClick(InventoryClickEvent event);

    //----------------------
    // Getter

    public int getPage() {
        return page;
    }

    public HashMap<Integer, HashMap<Integer, ItemStack>> getInventoryData() {
        return inventoryData;
    }

    public Inventory getMenu() {
        return menu;
    }
}
