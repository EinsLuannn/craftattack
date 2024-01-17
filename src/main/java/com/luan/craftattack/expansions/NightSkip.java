package com.luan.craftattack.expansions;

import com.luan.craftattack.CraftAttack;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.PluginManager;

import java.util.ArrayList;

public class NightSkip implements Listener {
    private ArrayList<Player> sleepingPlayers = new ArrayList<>();

    public NightSkip() {
        initialize();
    }

    public void initialize() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(this, CraftAttack.getInstance());
    }

    @EventHandler
    public void onEnterBed(PlayerBedEnterEvent event) {
        if (event.getBedEnterResult() == PlayerBedEnterEvent.BedEnterResult.OK) {
            sleepingPlayers.add(event.getPlayer());
            Bukkit.broadcastMessage("§aEs schlafen §c" + sleepingPlayers.size() + " §a/ §c" + Math.round(Math.ceil(Bukkit.getOnlinePlayers().size() * 1.0 / 3)) + " §aSpieler.");
        }

        if (sleepingPlayers.size() >= Bukkit.getOnlinePlayers().size() / 3) {
            Bukkit.getWorld("world").setTime(0);
            Bukkit.getWorld("world").setStorm(false);
            Bukkit.getWorld("world").setThundering(false);
            sleepingPlayers.clear();
        }
    }

    @EventHandler
    public void onExitBed(PlayerBedLeaveEvent event) {
        sleepingPlayers.remove(event.getPlayer());
        Bukkit.broadcastMessage("§aEs schlafen §c" + sleepingPlayers.size() + " §a/ §c" + Math.round(Math.ceil(Bukkit.getOnlinePlayers().size() * 1.0 / 3)) + " §aSpieler.");
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        sleepingPlayers.remove(event.getPlayer());
    }
}