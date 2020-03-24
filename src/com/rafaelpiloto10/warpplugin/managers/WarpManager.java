package com.rafaelpiloto10.warpplugin.managers;

import com.rafaelpiloto10.warpplugin.Main;
import com.rafaelpiloto10.warpplugin.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;

import java.io.*;
import java.nio.file.Path;
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
        File file = getWarpFile();
        ObjectOutputStream output = new ObjectOutputStream(new GZIPOutputStream(new FileOutputStream(file)));

        try {
            output.writeObject(warps);
            output.flush();
            output.close();
            Bukkit.broadcastMessage(Utils.chat("&aSuccessfully saved warps!"));

        } catch (IOException e) {
            e.printStackTrace();
            Bukkit.broadcastMessage(Utils.chat("&cCould not save warps!"));
        }

    }

    public void loadWarpFile() throws IOException, ClassNotFoundException {
        File file = getWarpFile();

        if (file != null) {
            ObjectInputStream input = new ObjectInputStream(new GZIPInputStream(new FileInputStream(file)));
            Object readObject = input.readObject();

            input.close();

            if (!(readObject instanceof HashMap)) {
                Bukkit.broadcastMessage(Utils.chat("&cCould not load warps - Not in readable format!"));
                throw new IOException("Data is not a valid HashMap!");
            }

            warps = (HashMap<String, HashMap<String, Location>>) readObject;
            for (String key : warps.keySet()) {
                warps.put(key, warps.get(key));
            }

            if (warps == null) {
                warps = new HashMap<String, HashMap<String, Location>>();
                warps.put("world", new HashMap<String, Location>());
            }
        }
    }

    public File getWarpFile(){
        return new File("WarpData/warps.dat");
    }

    public void setWarp(OfflinePlayer player, String name, Location warp) {
        setWarp(player.getUniqueId().toString(), name, warp);
    }

    public void setWarp(String branch, String name, Location warp) {
        HashMap<String, Location> playerSavedWarps = warps.get(branch);

        if (playerSavedWarps == null) {
            warps.put(branch, new HashMap<String, Location>());
            playerSavedWarps = warps.get(branch);
        }

        playerSavedWarps.put(name, warp);
        warps.put(branch, playerSavedWarps);
    }

    public boolean removeWarp(OfflinePlayer player, String name) {
        return removeWarp(player.getUniqueId().toString(), name);
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

    public String[] getWarps(OfflinePlayer p) {
        return getWarps(p.getUniqueId().toString());
    }

    public String[] getWarps(String branch) {
        return warps.get(branch).keySet().toArray(new String[warps.get(branch).keySet().size()]);
    }

    public Location getWarpLocation(OfflinePlayer p, String name) {
        return getWarpLocation(p.getUniqueId().toString(), name);
    }

    public Location getWarpLocation(String branch, String name) {
        return warps.get(branch).get(name);
    }


}
