package com.rafaelpiloto10.warpplugin;

import com.rafaelpiloto10.warpplugin.commands.WarpCommand;
import com.rafaelpiloto10.warpplugin.managers.WarpManager;
import com.rafaelpiloto10.warpplugin.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        WarpManager warpManager = new WarpManager(this);

        try {
            warpManager.loadWarpFile();
        } catch (IOException e) {
            Bukkit.broadcastMessage(Utils.chat("&cCould not &fload warps - &fIOException"));
            e.printStackTrace();

        } catch (ClassNotFoundException e) {
            Bukkit.broadcastMessage(Utils.chat("&cCould not &fload warps - &fClassNotFoundException"));
            e.printStackTrace();

        }

        registerManagers();
        registerCommands();
        registerListeners();

    }

    @Override
    public void onDisable(){
        WarpManager warpManager = new WarpManager(this);

        try {
            warpManager.saveWarpFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void registerManagers(){
        new WarpManager(this);
    }

    public void registerCommands(){
        new WarpCommand(this);
    }


    public void registerListeners(){}


}
