package com.rafaelpiloto10.warpplugin.managers;

import com.rafaelpiloto10.warpplugin.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

import java.io.*;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.UUID;
import java.util.stream.Stream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class WarpManager {

    private Main plugin;

    private static HashMap<String, HashMap<String, Location>> warps = new HashMap<String, HashMap<String, Location>>();

    public WarpManager(Main plugin) {
        this.plugin = plugin;
    }

    public void saveWarpFile() throws IOException {
        for (OfflinePlayer p : Bukkit.getOfflinePlayers()) {
            File file = new File("WarpData/warps.dat");
            ObjectOutputStream output = new ObjectOutputStream(new GZIPOutputStream(new FileOutputStream(file)));

            if (warps.get(p.getUniqueId()) != null) {
                warps.put(p.getUniqueId().toString(), warps.get(p.getUniqueId()));
            }

            try {
                output.writeObject(warps);
                output.flush();
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void loadWarpFile() throws IOException, ClassNotFoundException {
        File file = new File("WarpData/warps.dat");

        if (file != null) {
            ObjectInputStream input = new ObjectInputStream(new GZIPInputStream(new FileInputStream(file)));
            Object readObject = input.readObject();

            input.close();

            if (!(readObject instanceof HashMap)) {
                throw new IOException("Data is not a valid HashMap!");
            }

            warps = (HashMap<String, HashMap<String, Location>>) readObject;
            for (String key : warps.keySet()) {
                warps.put(key, warps.get(key));
            }
        }
    }

    public void setWarp(OfflinePlayer player, String name, Location warp) {
        HashMap<String, Location> playerSavedWarps = warps.get(player.getUniqueId().toString());
        playerSavedWarps.put(name, warp);
        warps.put(player.getUniqueId().toString(), playerSavedWarps);
    }

    public void setWarp(String branch, String name, Location warp) {
        HashMap<String, Location> playerSavedWarps = warps.get(branch);
        playerSavedWarps.put(name, warp);
        warps.put(branch, playerSavedWarps);
    }

    public boolean removeWarp(OfflinePlayer player, String name) {
        HashMap<String, Location> playerSavedWarps = warps.get(player.getUniqueId().toString());
        if (playerSavedWarps != null && playerSavedWarps.get(name) != null) {
            playerSavedWarps.remove(name);
            warps.put(player.getUniqueId().toString(), playerSavedWarps);
            return true;
        } else {
            return false;
        }
    }

    public boolean removeWarp(String branch, String name) {
        HashMap<String, Location> playerSavedWarps = warps.get(branch);
        if (playerSavedWarps != null && playerSavedWarps.get(name) != null) {
            playerSavedWarps.remove(name);
            warps.put(branch, playerSavedWarps);
            return true;
        } else {
            return false;
        }
    }

    public String[] getWarps(OfflinePlayer p){
        return (String[]) warps.get(p.getUniqueId().toString()).keySet().toArray();
    }

    public String[] getWarps(String branch){
        return (String[]) warps.get(branch).keySet().toArray();
    }

    public Location getWarpLocation(OfflinePlayer p, String name){
        return warps.get(p.getUniqueId().toString()).get(name);
    }

    public Location getWarpLocation(String branch, String name){
        return warps.get(branch).get(name);
    }



}
