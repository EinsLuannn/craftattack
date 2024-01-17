package com.luan.craftattack.commands;

import com.luan.craftattack.utils.DateManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.lang.management.ManagementFactory;
import java.text.DecimalFormat;

public class CommandTPS implements CommandExecutor {
    private static final DecimalFormat twoDPlaces = new DecimalFormat("#,###.##");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        final double[] tpsArray = Bukkit.getServer().getTPS();
        StringBuilder tpsString = new StringBuilder();
        for (double tps : tpsArray) {
            final ChatColor color;
            if (tps >= 12.0) {
                color = ChatColor.GREEN;
            } else if (tps >= 8.0) {
                color = ChatColor.YELLOW;
            } else {
                color = ChatColor.RED;
            }

            String tpsStr = twoDPlaces.format(tps);
            if (tps > 20.0) {
                tpsStr = "*20.0";
            }

            tpsString.append(color).append(tpsStr).append(ChatColor.GRAY).append(", ");
        }
        sender.sendMessage("§6Uptime§7: §a" + DateManager.formatDateDiff(ManagementFactory.getRuntimeMXBean().getStartTime()));
        sender.sendMessage("§6TPS§7: §a" + tpsString.toString());
        sender.sendMessage("§6Total RAM§7: §a" + Runtime.getRuntime().totalMemory() / 1024 / 1024 + " MB");
        sender.sendMessage("§6Free RAM§7: §a" + Runtime.getRuntime().freeMemory() / 1024 / 1024 + " MB");
        return false;
    }
}