package com.rafaelpiloto10.warpplugin.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.Serializable;

public class Warp implements Serializable {
    public String name;
    public String owner;
    public String world;
    public int x, y, z;

    public Warp(String owner, String name, String world, int x, int y, int z){
        this.owner = owner;
        this.name = name;
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Location warpToLocation(){
        return new Location(Bukkit.getServer().getWorld(world), x, y, z);
    }

    public static Warp locationToWarp(String owner, String name, Location location){
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();
        String world = location.getWorld().getName();
        return new Warp(owner, name, world, x, y, z);
    }

    @Override
    public String toString() {
        return "Warp owner: " + owner + " set warp: " + name + " in world: " + world.toString() + " where x=" + x + ", y=" + y + ", z=" + z;
    }
}
