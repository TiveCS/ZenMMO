package com.rehoukrel.zenmmo.utils.nms;

import com.rehoukrel.zenmmo.utils.nms.NBT_Item.NBTManager;
import com.rehoukrel.zenmmo.utils.nms.NBT_Item.*;
import org.bukkit.Bukkit;
import org.yaml.snakeyaml.DumperOptions;

public class NMSManager {

    private String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    private NBTManager nbtManager = null;

    private enum VersionString{
        v1_12_R1, v1_13_R1, v1_13_R2, v1_14_R1, v1_14_R2;
    }

    public NMSManager(){}

    public NBTManager getNbtManager() {
        if (nbtManager == null){
            switch (VersionString.valueOf(getVersion())){
                case v1_12_R1:
                    this.nbtManager = new NBTManager_1_12_R1(this);
                    break;
                case v1_13_R1:
                    this.nbtManager = new NBTManager_1_13_R1(this);
                    break;
                case v1_13_R2:
                    this.nbtManager = new NBTManager_1_13_R2(this);
                    break;
                case v1_14_R1:
                    this.nbtManager = new NBTManager_1_14_R1(this);
                    break;
                case v1_14_R2:
                    this.nbtManager = new NBTManager_1_14_R2(this);
                    break;
            }
        }
        return nbtManager;
    }

    public String getVersion(){
        return this.version;
    }

}
