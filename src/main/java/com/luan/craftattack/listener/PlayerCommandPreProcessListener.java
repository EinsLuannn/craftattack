package com.luan.craftattack.listener;

import com.luan.craftattack.commands.CommandTPS;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class PlayerCommandPreProcessListener implements Listener {
    @EventHandler
    public void onPreCommand(PlayerCommandPreprocessEvent event) {
        if (event.getMessage().equalsIgnoreCase("/tps")) {
            event.setCancelled(true);
            new CommandTPS().onCommand(event.getPlayer(), null, null, event.getMessage().split(" "));
        }
    }
}