package com.luan.craftattack.expansions.spawn;

import com.luan.craftattack.CraftAttack;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetSpawnCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (sender.hasPermission("system.sethome")) {
                Player player = (Player) sender;
                CraftAttack.getInstance().getSpawnManager().setSpawn(player.getLocation());
                player.sendMessage("§aSpawn gesetzt!");
            }else{
                sender.sendMessage("§cDu hast keine Rechte dafür!");
            }
        }
        return false;
    }
}