package com.rehoukrel.zenmmo.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class ConfigManager {

    private Plugin plugin;
    private File file;
    private FileConfiguration config;

    public ConfigManager(Plugin plugin, File file){
        this.plugin = plugin;
        this.file = file;

        if (!this.file.exists()){
            try {
                if (file.isDirectory()){
                    this.file.mkdir();
                }else {
                    this.file.createNewFile();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.config = YamlConfiguration.loadConfiguration(this.file);
    }

    public boolean contains(String path){
        return getConfig().contains(path);
    }

    public Object get(String path){
        if (getConfig().contains(path)){
            return getConfig().get(path);
        }
        return null;
    }

    public void init(String path, Object value){
        if (!getConfig().contains(path)){
            getConfig().set(path, value);
        }
    }

    public void initAndSave(String path, Object value){
        if (!getConfig().contains(path)){
            getConfig().set(path, value);
            saveConfig();
        }
    }

    public void inputAndSave(String path, Object value){
        getConfig().set(path, value);
        saveConfig();
    }

    public void input(String path, Object value){
        getConfig().set(path, value);
    }

    public void saveConfig(){
        try {
            getConfig().save(getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public File getFile() {
        return file;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public FileConfiguration getConfig() {
        return config;
    }

}
