package com.luan.craftattack;

import com.luan.craftattack.commands.CommandMaintenance;
import com.luan.craftattack.commands.CommandTPS;
import com.luan.craftattack.discord.DiscordIntegration;
import com.luan.craftattack.expansions.ElytraSpawn;
import com.luan.craftattack.expansions.NightSkip;
import com.luan.craftattack.expansions.Registration;
import com.luan.craftattack.expansions.TablistDeaths;
import com.luan.craftattack.expansions.home.HomeManager;
import com.luan.craftattack.expansions.spawn.SpawnManager;
import com.luan.craftattack.expansions.teams.TeamManager;
import com.luan.craftattack.listener.PlayerCommandPreProcessListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.IOException;

public final class CraftAttack extends JavaPlugin {

    private static CraftAttack instance;
    private TeamManager teamManager;
    private HomeManager homeManager;
    private Registration registration;
    private DiscordIntegration discordIntegration;
    private SpawnManager spawnManager;
    private boolean maintenance = false;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        this.getDataFolder().mkdirs();
        if(!new File(getDataFolder(), "config.yml").exists()) {
            this.saveDefaultConfig();
        }
        this.reloadConfig();

        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new PlayerCommandPreProcessListener(), this);
        pluginManager.registerEvents(new CommandMaintenance(), this);

        this.getCommand("tps").setExecutor(new CommandTPS());
        this.getCommand("maintenance").setExecutor(new CommandMaintenance());


        registration = new Registration(new File(getDataFolder(), "registration.json"));
        try {
            registration.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        teamManager = new TeamManager();

        homeManager = new HomeManager();
        homeManager.initialize();

        spawnManager = new SpawnManager();
        spawnManager.initialize();

        new TablistDeaths();
        new ElytraSpawn(this.getConfig().getInt("elytra_radius"));
        new NightSkip();

        if (new File(getDataFolder(), "maintenance").exists()) {
            maintenance = true;
        }

        if (this.getConfig().getBoolean("discord_enabled")) {
            try {
                discordIntegration = new DiscordIntegration(this.getConfig().getString("discord_token"), this.getConfig().getString("discord_channelid"));
            } catch (LoginException exception) {
                throw new RuntimeException(exception);
            } catch (InterruptedException exception) {
                throw new RuntimeException(exception);
            }
        }
    }

    @Override
    public void onDisable() {
        if (!maintenance) {
            new File(getDataFolder(), "maintenance").delete();
        } else {
            try {
                new File(getDataFolder(), "maintenance").createNewFile();
            } catch (IOException exception) {
                throw new RuntimeException(exception);
            }
        }
        try {
            registration.save();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }


    public static CraftAttack getInstance() {
        return instance;
    }

    public TeamManager getTeamManager() {
        return teamManager;
    }

    public HomeManager getHomeManager() {
        return homeManager;
    }

    public Registration getRegistration() {
        return registration;
    }

    public boolean isMaintenance() {
        return maintenance;
    }

    public SpawnManager getSpawnManager() {
        return spawnManager;
    }

    public void setMaintenance(boolean maintenance) {
        this.maintenance = maintenance;
    }

    public DiscordIntegration getDiscordIntegration() {
        return discordIntegration;
    }
}
