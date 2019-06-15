package com.rehoukrel.zenmmo.utils.nms.NBT_Item;

import com.rehoukrel.zenmmo.utils.nms.NMSManager;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;

public class NBTManager_1_12_R1 extends NBTManager {
    public NBTManager_1_12_R1(NMSManager nmsManager) {
        super(nmsManager);
    }

    @Override
    public void setNbt(String path, Object value) {
        ItemStack nmsItemStack = CraftItemStack.asNMSCopy(getItem());
        NBTTagCompound nbt = nmsItemStack.hasTag() ? nmsItemStack.getTag() : new NBTTagCompound();
        if (value instanceof Integer){
            nbt.setInt(path, (Integer) value);
        }else if (value instanceof Double){
            nbt.setDouble(path, (Double) value);
        }else if (value instanceof Byte){
            nbt.setByte(path, (Byte) value);
        } else if (value instanceof Boolean){
            nbt.setBoolean(path, Boolean.parseBoolean(value.toString()));
        }
        else{
            nbt.setString(path, value.toString());
        }
        nmsItemStack.setTag(nbt);
        setItem(CraftItemStack.asBukkitCopy(nmsItemStack));
    }

    @Override
    public void deleteNbt(String path) {
        ItemStack nmsItemStack = CraftItemStack.asNMSCopy(getItem());
        NBTTagCompound nbt = nmsItemStack.hasTag() ? nmsItemStack.getTag() : new NBTTagCompound();
        if (nbt.hasKey(path)){
            nbt.remove(path);
        }else{
            return;
        }
        nmsItemStack.setTag(nbt);
        setItem(CraftItemStack.asBukkitCopy(nmsItemStack));
    }

    @Override
    public String getString(String path) {
        ItemStack nmsItemStack = CraftItemStack.asNMSCopy(getItem());
        NBTTagCompound nbt = nmsItemStack.hasTag() ? nmsItemStack.getTag() : new NBTTagCompound();
        if (nbt.hasKey(path)){
            return nbt.getString(path);
        }
        return null;
    }

    @Override
    public boolean getBoolean(String path) {
        ItemStack nmsItemStack = CraftItemStack.asNMSCopy(getItem());
        NBTTagCompound nbt = nmsItemStack.hasTag() ? nmsItemStack.getTag() : new NBTTagCompound();
        if (nbt.hasKey(path)){
            return nbt.getBoolean(path);
        }
        return false;
    }

    @Override
    public int getInt(String path) {
        ItemStack nmsItemStack = CraftItemStack.asNMSCopy(getItem());
        NBTTagCompound nbt = nmsItemStack.hasTag() ? nmsItemStack.getTag() : new NBTTagCompound();
        if (nbt.hasKey(path)){
            return nbt.getInt(path);
        }
        return 0;
    }

    @Override
    public double getDouble(String path) {
        ItemStack nmsItemStack = CraftItemStack.asNMSCopy(getItem());
        NBTTagCompound nbt = nmsItemStack.hasTag() ? nmsItemStack.getTag() : new NBTTagCompound();
        if (nbt.hasKey(path)){
            return nbt.getDouble(path);
        }
        return 0;
    }

    @Override
    public byte getByte(String path) {
        ItemStack nmsItemStack = CraftItemStack.asNMSCopy(getItem());
        NBTTagCompound nbt = nmsItemStack.hasTag() ? nmsItemStack.getTag() : new NBTTagCompound();
        if (nbt.hasKey(path)){
            return nbt.getByte(path);
        }
        return 0;
    }

    @Override
    public Object get(String path) {
        ItemStack nmsItemStack = CraftItemStack.asNMSCopy(getItem());
        NBTTagCompound nbt = nmsItemStack.hasTag() ? nmsItemStack.getTag() : new NBTTagCompound();
        if (nbt.hasKey(path)){
            NBTBase base = nbt.get(path);
            if (base instanceof NBTTagDouble){
                return nbt.getDouble(path);
            }else if (base instanceof NBTTagInt) {
                return nbt.getInt(path);
            } else if (base instanceof NBTTagByte){
                return nbt.getByte(path);
            } else if (base instanceof NBTTagString){
                return nbt.getString(path);
            } else{
                return null;
            }
        }
        return null;
    }
}
