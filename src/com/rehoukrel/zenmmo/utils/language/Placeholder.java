package com.rehoukrel.zenmmo.utils.language;

import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.List;

public class Placeholder {

    private HashMap<String, String> replacer = new HashMap<String, String>();
    private Plugin plugin = null;

    public Placeholder(){
        initializeDefaultReplacer();
    }

    public Placeholder(Plugin plugin){
        initializeDefaultReplacer();
        this.plugin = plugin;
    }

    public List<String> useMass(List<String> l){
        for (int i = 0; i < l.size(); i++){
            String s = l.get(i);
            for (String r : getReplacer().keySet()){
                s = s.replace("%" + r +"%", getReplacer().get(r));
                l.set(i, s);
            }
        }
        return l;
    }

    public List<String> translateColor(List<String> l){
        for (int i = 0; i < l.size(); i++){
            l.set(i, ChatColor.translateAlternateColorCodes('&', l.get(i)));
        }
        return l;
    }

    public String use(String s){
        for (String r : getReplacer().keySet()){
            s = s.replace("%" + r + "%", getReplacer().get(r));
        }
        return s;
    }

    public void addReplacer(String key, String replacer){
        getReplacer().put(key, replacer);
    }

    public void initializeDefaultReplacer(){
        if (getPlugin() != null) {
            addReplacer("prefix", getPlugin().getConfig().contains("prefix") ? getPlugin().getConfig().getString("prefix") : "");
        }
    }

    public boolean isUsingPlugin(){
        return !getPlugin().equals(null);
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public HashMap<String, String> getReplacer() {
        return replacer;
    }
}
