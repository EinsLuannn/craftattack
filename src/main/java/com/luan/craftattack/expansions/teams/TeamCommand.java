package com.luan.craftattack.expansions.teams;

import com.luan.craftattack.CraftAttack;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class TeamCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0 || args.length == 1) {
                if (args.length == 1 && args[0].equalsIgnoreCase("leave")) {
                    CraftAttack.getInstance().getTeamManager().removePlayerFromTeam(player);
                    return true;
                } else {
                    player.sendMessage("§c/team <create|join> <teamname>");
                    player.sendMessage("§c/team <accept|kick> <username>");
                    player.sendMessage("§c/team leave");
                }
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("create")) {
                    try {
                        if (CraftAttack.getInstance().getTeamManager().getTeamInfo(args[1]) != null) {
                            player.sendMessage("§cDieses Team existiert bereits!");
                            return true;
                        }
                    } catch (Exception e) {

                    }
                    Team team = new Team(player.getUniqueId(), new ArrayList<>(), args[1]);
                    CraftAttack.getInstance().getTeamManager().setTeamInfo(team);
                    player.sendMessage("§aTeam erstellt!");
                    return true;
                } else if (args[0].equalsIgnoreCase("join")) {
                    if (CraftAttack.getInstance().getTeamManager().getTeamInfo(args[1]) != null) {
                        CraftAttack.getInstance().getTeamManager().requestJoin(player, args[1]);
                    }
                    return true;
                } else if (args[0].equalsIgnoreCase("accept")) {
                    if (Bukkit.getPlayer(args[1]) != null && Bukkit.getPlayer(args[1]).isOnline()) {
                        Player target = Bukkit.getPlayer(args[1]);
                        TeamManager teamManager = CraftAttack.getInstance().getTeamManager();
                        Team team = teamManager.getTeamInfo(teamManager.getTeam(player));
                        if (team != null) {
                            if (team.getTeamLeader().equals(player.getUniqueId())) {
                                teamManager.addPlayerToTeam(target, team.getTeamName());
                                target.sendMessage("§aDu wurdest ins Team §e" + team.getTeamName() + " §aghinzugefügt!");
                                player.sendMessage("§aDu hast den Spieler §e" + target.getName() + " §ains Team §e" + team.getTeamName() + " §aghinzugefügt!");
                                return true;
                            }
                        }
                        player.sendMessage("§cDu bist nicht Teamleiter oder der Spieler ist nicht online!");
                    }
                    return true;
                } else if (args[0].equalsIgnoreCase("kick")) {
                    if (Bukkit.getPlayer(args[1]) != null) {
                        Player target = Bukkit.getPlayer(args[1]);
                        TeamManager teamManager = CraftAttack.getInstance().getTeamManager();
                        Team team = teamManager.getTeamInfo(teamManager.getTeam(player));
                        if (team != null) {
                            if (team.getTeamLeader().equals(player.getUniqueId())) {
                                if (team.getTeamMembers().contains(target.getUniqueId())) {
                                    teamManager.removePlayerFromTeam(target);
                                    player.sendMessage("§aDu hast den Spieler §e" + target.getName() + " §cgekickt!");
                                    return true;
                                }
                            }
                        }
                        player.sendMessage("§cDu bist nicht Teamleiter oder der Spieler konnte nicht gefunden werden!");
                    }
                    return true;
                }
                player.sendMessage("§c/team <create|join|accept>");
            }
        }
        return false;
    }
}