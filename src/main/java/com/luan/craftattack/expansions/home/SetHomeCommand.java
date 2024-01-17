package com.luan.craftattack.expansions.home;

import com.luan.craftattack.CraftAttack;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetHomeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            CraftAttack.getInstance().getHomeManager().setHome(player, player.getLocation());
            player.sendMessage("§aHome gesetzt!");
        }
        return false;
    }
}