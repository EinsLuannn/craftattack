package com.luan.craftattack;

import com.luan.craftattack.expansions.teams.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class CraftAttack extends JavaPlugin {

    private static CraftAttack instance;
    private TeamManager teamManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        this.getDataFolder().mkdirs();
        if(!new File(getDataFolder(), "config.yml").exists()) {
            this.saveDefaultConfig();
        }
        this.reloadConfig();
        getLogger().info("Loading Commands...");


        getLogger().info("Loading Listeners...");
        PluginManager pluginManager = Bukkit.getPluginManager();

        teamManager = new TeamManager();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static CraftAttack getInstance() {
        return instance;
    }

    public TeamManager getTeamManager() {
        return teamManager;
    }
}
