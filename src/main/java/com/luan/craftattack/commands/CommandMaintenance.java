package com.luan.craftattack.commands;

import com.luan.craftattack.CraftAttack;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

public class CommandMaintenance implements CommandExecutor, Listener {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender.hasPermission("*")) {
            if (CraftAttack.getInstance().isMaintenance()) {
                CraftAttack.getInstance().setMaintenance(false);
                commandSender.sendMessage("§aMaintenance wurde deaktiviert!");
            } else {
                CraftAttack.getInstance().setMaintenance(true);
                commandSender.sendMessage("§aMaintenance wurde aktiviert!");
            }
        } else {
            commandSender.sendMessage("§cDu hast keine Rechte für diesen Befehl!");
        }
        return false;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (CraftAttack.getInstance().isMaintenance()) {
            if (event.getPlayer().getName().equalsIgnoreCase("langhalsluan") || event.getPlayer().getName().equalsIgnoreCase("EinsJustinnnn")) {
                return;
            }
            Bukkit.getScheduler().runTaskLater(CraftAttack.getInstance(), () -> event.getPlayer().kickPlayer("§cDer Server befindet sich im Wartungsmodus!"), 1);
        }
    }
}