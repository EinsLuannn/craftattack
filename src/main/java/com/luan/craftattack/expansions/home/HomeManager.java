package com.luan.craftattack.expansions.home;

import com.luan.craftattack.CraftAttack;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class HomeManager {
    private final File homesFile = new File(CraftAttack.getInstance().getDataFolder(), "homes.yml");
    private final YamlConfiguration homesConfig = YamlConfiguration.loadConfiguration(homesFile);

    public void initialize() {
        CraftAttack.getInstance().getCommand("home").setExecutor(new HomeCommand());
        CraftAttack.getInstance().getCommand("sethome").setExecutor(new SetHomeCommand());
    }

    public void setHome(Player player, Location location) {
        homesConfig.set(player.getUniqueId().toString(), location);
        try {
            homesConfig.save(homesFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Location getHome(Player player) {
        return homesConfig.getLocation(player.getUniqueId().toString());
    }
}