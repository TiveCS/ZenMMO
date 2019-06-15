package com.rehoukrel.zenmmo.utils.nms.NBT_Item;

import com.rehoukrel.zenmmo.utils.nms.NMSManager;
import org.bukkit.inventory.ItemStack;

public abstract class NBTManager {

    private ItemStack item = null;
    private NMSManager nmsManager = null;

    public NBTManager(NMSManager nmsManager){
        this.nmsManager = nmsManager;
    }

    public void setItem(ItemStack item){
        this.item = item;
    }

    public ItemStack getItem(){
        return this.item;
    }

    public abstract void setNbt(String path, Object value);
    public abstract void deleteNbt(String path);

    public abstract String getString(String path);
    public abstract boolean getBoolean(String path);
    public abstract int getInt(String path);
    public abstract double getDouble(String path);
    public abstract byte getByte(String path);
    public abstract Object get(String path);

}
