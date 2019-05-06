package com.rehoukrel.zenmmo.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.Plugin;

public class ParticleManager {

    private Location location;
    private Plugin plugin;
    private Entity entity;
    private double offsetX = 0, offsetY = 0, offsetZ = 0;

    public ParticleManager(Plugin plugin, Location loc){
        this.location = loc;
        this.plugin = plugin;
    }

    public ParticleManager(Plugin plugin, Entity entity){
        this(plugin, entity.getLocation());
        this.entity = entity;
    }

    public ParticleManager(Plugin plugin, Location loc, double offsetX, double offsetY, double offsetZ){
        this.plugin = plugin;
        this.location = loc;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
    }

    public void dotParticle(Particle particle, int amount, Object... obj){
        getWorld().spawnParticle(particle, getLocation(), amount, offsetX, offsetY, offsetZ, 0);
    }

    public void trails(Particle particle, Entity entity,int particleAmount, long duration, int durationIncrement){
        for (int i = 0; i < duration; i+=durationIncrement){
            Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () ->{
                this.location = entity.getLocation();
                dotParticle(particle, particleAmount);
            }, i);
        }
    }

    public void circle(Particle particle, double radius, double increment,long duration, int tickDelayPerIncrement){
        int now = 0;
        for (double degree = 0; degree < 360; degree+=increment){
            double radian = Math.toRadians(degree);
            double dZ = radius*Math.sin(radian), dX = radius*Math.cos(radian);
            double x = getLocation().getX() + getOffsetX() + dX, z = getLocation().getZ() + getOffsetZ() + dZ;
            Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, new Runnable() {
                @Override
                public void run() {
                    Location nLoc = new Location(getWorld(), x*dX, getLocation().getY(), z*dZ);
                    dotParticle(particle, 1, nLoc);
                }
            }, now);
            if (now < duration){
                now+= tickDelayPerIncrement;
            }
        }
    }

    public World getWorld(){
        return getLocation().getWorld();
    }

    public Location getLocation() {
        return location;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public double getOffsetX() {
        return offsetX;
    }

    public double getOffsetY() {
        return offsetY;
    }

    public double getOffsetZ() {
        return offsetZ;
    }
}
