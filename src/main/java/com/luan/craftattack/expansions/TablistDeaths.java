package com.luan.craftattack.expansions;

import com.luan.craftattack.CraftAttack;
import com.luan.craftattack.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginManager;

public class TablistDeaths implements Listener {

    public TablistDeaths() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(this, CraftAttack.getInstance());
    }

    public static void updateTablist(Player player) {
        String teamName = " ";
        if(CraftAttack.getInstance().getTeamManager().getTeam(player) != null) {
            teamName = CraftAttack.getInstance().getTeamManager().getTeam(player);
        }
        player.setPlayerListName(player.getName() + " [" + Utils.stripColorCodes(teamName) + "] §c" + player.getStatistic(Statistic.DEATHS));
    }

    @EventHandler
    public void handleDeath(PlayerDeathEvent event) {
        updateTablist(event.getEntity());
    }

    @EventHandler
    public void handleJoin(PlayerJoinEvent event) {
        updateTablist(event.getPlayer());
    }
}
