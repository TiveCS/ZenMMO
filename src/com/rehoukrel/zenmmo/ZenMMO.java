package com.rehoukrel.zenmmo;

import com.rehoukrel.zenmmo.api.Skill;
import com.rehoukrel.zenmmo.api.SkillTree;
import com.rehoukrel.zenmmo.api.skilltree.Resource;
import com.rehoukrel.zenmmo.cmd.CmdZenMMO;
import com.rehoukrel.zenmmo.event.BasicEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class ZenMMO extends JavaPlugin {

    int minExp = 0, maxExp = 0;

    @Override
    public void onEnable(){
        loadConfig();
        loadSoftDepend();
        loadEvents();
        loadCmd();
        loadSkillTree();
    }

    public void loadSkillTree(){
        SkillTree.register(new Resource());
    }

    private void loadSoftDepend(){

    }

    private void loadEvents(){
        getServer().getPluginManager().registerEvents(new BasicEvent(), this);
    }

    private void loadCmd(){
        getCommand("zenmmo").setExecutor(new CmdZenMMO());
    }

    public void loadConfig(){
        getConfig().options().copyDefaults(true);
        saveConfig();

        minExp = getConfig().getInt("system.exp-min");
        maxExp = getConfig().getInt("system.exp-max");
    }

}
