package com.rafaelpiloto10.warpplugin.commands;

import com.google.common.collect.ObjectArrays;
import com.rafaelpiloto10.warpplugin.Main;
import com.rafaelpiloto10.warpplugin.managers.WarpManager;
import com.rafaelpiloto10.warpplugin.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WarpCommand implements CommandExecutor {

    private WarpManager warpManager;
    private Main plugin;

    public WarpCommand(Main plugin) {
        this.plugin = plugin;
        this.plugin.getCommand("warp").setExecutor(this);
        warpManager = new WarpManager(this.plugin);
    }


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(Utils.chat("&cOnly players can run this command!"));
        } else {
            /**
             *
             * Available Commands:
             *  /warp location_name
             *  /warp ((set/set:world) : (remove/rm) ) location_name
             *  /warp list
             *  /warp help
             *
             */
            Player player = (Player) commandSender;
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(player.getUniqueId());

            if (player.hasPermission("warpplugin.use")) {
                if (strings.length == 1) {
                    if (strings[0].equalsIgnoreCase("list")) {
                        // /warp list
                        String[] world_warps = warpManager.getWarps(plugin.getConfig().getString("world_name"));
                        String[] player_warps = warpManager.getWarps(offlinePlayer);

                        if (world_warps != null && player_warps != null && (world_warps.length > 0 || player_warps.length > 0)) {
                            String[] total_warps = ObjectArrays.concat(world_warps, player_warps, String.class);
                            String listed_warps = "";
                            for (String warp : total_warps) {
                                listed_warps += warp + ", ";
                            }
                            player.sendMessage(Utils.chat("&7Warps: &f" + listed_warps.trim().substring(0, listed_warps.length() - 2)));
                        } else {
                            player.sendMessage(Utils.chat(plugin.getConfig().getString("no_warps")));
                        }

                    } else if (strings[0].equalsIgnoreCase("help")) {
                        // /warp help
                        player.sendMessage(Utils.chat(plugin.getConfig().getString("help")));

                    } else {
                        // /warp location_name
                        Location warpLocation = warpManager.getWarpLocation(plugin.getConfig().getString("world_name"), strings[0]) != null ?
                                warpManager.getWarpLocation(plugin.getConfig().getString("world_name"), strings[0]) : warpManager.getWarpLocation(offlinePlayer, strings[0]);
                        if (warpLocation != null) {
                            try {
                                if (player.getWorld().getName().equals(warpLocation.getWorld().getName()) || plugin.getConfig().getBoolean("allowed_interdimensional_travel")) {
                                    if (player.getLevel() >= plugin.getConfig().getInt("warp_xp_level_cost")) {
                                        player.sendMessage(Utils.chat(plugin.getConfig().getString("warp_success").replace("<warp>", strings[0])));
                                        player.teleport(warpLocation);
                                        player.setLevel(player.getLevel() - plugin.getConfig().getInt("warp_xp_level_cost"));
                                    } else {
                                        player.sendMessage(Utils.chat(plugin.getConfig().getString("warp_error_xp").replace("<xp_cost>", Integer.toString(plugin.getConfig().getInt("warp_xp_level_cost")))));
                                    }
                                } else {
                                    player.sendMessage(Utils.chat(plugin.getConfig().getString("warp_error_interdimension")));
                                }

                            } catch (NullPointerException e){
                                e.printStackTrace();
                                player.sendMessage(Utils.chat("&cThe location is returning a null world!"));
                            }
                        } else {
                            player.sendMessage(Utils.chat(plugin.getConfig().getString("warp_not_exist").replace("<warp>", strings[0])));
                        }
                    }
                } else if (strings.length == 2) {
                    if (strings[0].equalsIgnoreCase("set")) {
                        // /warp set location_name
                        for (String world_warps : warpManager.getWarps(plugin.getConfig().getString("world_name"))) {
                            if (world_warps.equalsIgnoreCase(strings[1])) {
                                player.sendMessage(Utils.chat(plugin.getConfig().getString("warp_exists").replace("<warp>", strings[1])));
                                return false;
                            }
                        }
                        for (String cmd : plugin.getConfig().getString("illegal_names").split(",")) {
                            if (cmd.equalsIgnoreCase(strings[1])) {
                                player.sendMessage(Utils.chat(plugin.getConfig().getString("illegal_name").replace("<warp>", strings[1])));
                                return false;
                            }
                        }
                        if (warpManager.getWarps(offlinePlayer).length >= plugin.getConfig().getInt("player_warp_limit")) {
                            player.sendMessage(Utils.chat(plugin.getConfig().getString("warp_limit_error").replace("<amount>", Integer.toString(plugin.getConfig().getInt("player_warp_limit")))));
                            return false;
                        }

                        warpManager.setWarp(offlinePlayer, strings[1], player.getLocation());
                        player.sendMessage(Utils.chat(plugin.getConfig().getString("set_new_warp").replace("<warp>", strings[1])));

                    } else if (strings[0].equalsIgnoreCase("set:world")) {
                        // /warp set:world location_name
                        if (player.hasPermission("warpplugin.set_world")) {
                            warpManager.setWarp(plugin.getConfig().getString("world_name"), strings[1], player.getLocation());
                            player.sendMessage(Utils.chat(plugin.getConfig().getString("set_new_warp").replace("<warp>", strings[1])));
                            Bukkit.broadcastMessage(Utils.chat(plugin.getConfig().getString("world_warp_announcement").replace("<warp>", strings[1])));
                        } else
                            player.sendMessage(Utils.chat(plugin.getConfig().getString("no_perms")));

                    } else if (strings[0].equalsIgnoreCase("remove") || strings[0].equalsIgnoreCase("rm")) {
                        if (warpManager.removeWarp(offlinePlayer, strings[1])) {
                            player.sendMessage(Utils.chat(plugin.getConfig().getString("remove_success").replace("<warp>", strings[1])));
                            return false;
                        } else if (player.hasPermission("warpplugin.set_world") && warpManager.removeWarp(plugin.getConfig().getString("world_name"), strings[1])) {
                            player.sendMessage(Utils.chat(plugin.getConfig().getString("remove_success").replace("<warp>", strings[1])));
                            return false;
                        } else {
                            player.sendMessage(Utils.chat(plugin.getConfig().getString("remove_error").replace("<warp>", strings[1])));
                            return false;
                        }
                    } else {
                        player.sendMessage(Utils.chat(plugin.getConfig().getString("no_parse")));
                    }
                } else {
                    // Not correct command syntax
                    player.sendMessage(Utils.chat(plugin.getConfig().getString("no_parse")));
                }
            }
        }
        return false;
    }
}
