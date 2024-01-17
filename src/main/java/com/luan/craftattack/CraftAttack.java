package com.luan.craftattack;

import com.luan.craftattack.discord.DiscordIntegration;
import com.luan.craftattack.expansions.Registration;
import com.luan.craftattack.expansions.home.HomeManager;
import com.luan.craftattack.expansions.teams.TeamManager;
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
        homeManager = new HomeManager();
        registration = new Registration(new File(getDataFolder(), "registration.json"));
        try {
            registration.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
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
        // Plugin shutdown logic
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

    public DiscordIntegration getDiscordIntegration() {
        return discordIntegration;
    }
}
