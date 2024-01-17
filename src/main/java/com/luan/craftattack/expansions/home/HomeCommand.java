package com.luan.craftattack.expansions.home;

import com.luan.craftattack.CraftAttack;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.LocalDate;

public class HomeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Location location = CraftAttack.getInstance().getHomeManager().getHome(player);
            if (location != null) {
                Location currentLocation = player.getLocation();
                player.sendMessage("§aDu wirst in 5s zu deinem Home teleportiert.");
                Bukkit.getScheduler().runTaskLater(CraftAttack.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        if (currentLocation.distance(player.getLocation()) <= 1) {
                            player.teleport(location);
                            player.sendMessage("§aDu wurdest zu deinem Home teleportiert.");
                        }else{
                            player.sendMessage("§cDu darfst dich während der Teleportation nicht bewegen.");
                        }
                    }
                },20 * 4);
            }else{
                player.sendMessage("§cDu hast kein Home gesetzt.");
            }
        }
        return false;
    }
}