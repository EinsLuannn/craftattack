package com.luan.craftattack.expansions.teams;

import com.luan.craftattack.CraftAttack;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.UUID;

public class TeamManager {
    private final File teamsConfigFile = new File(CraftAttack.getInstance().getDataFolder(), "teams.yml");
    private final YamlConfiguration teamsConfig = YamlConfiguration.loadConfiguration(teamsConfigFile);

    private final File playerConfigFile = new File(CraftAttack.getInstance().getDataFolder(), "player-teams.yml");
    private final YamlConfiguration playerConfig = YamlConfiguration.loadConfiguration(playerConfigFile);

    public TeamManager() {
        CraftAttack.getInstance().getCommand("team").setExecutor(new TeamCommand());

        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new TeamListener(), CraftAttack.getInstance());
    }

    public String getTeam(Player player) {
        return playerConfig.getString(player.getUniqueId().toString());
    }

    public Team getTeamInfo(String teamName) {
        JSONObject info = new JSONObject(new String(Base64.getDecoder().decode(teamsConfig.getString(teamName))));
        UUID leader = UUID.fromString(info.getString("leader"));
        ArrayList<UUID> members = new ArrayList<>();
        for (String member : info.getString("members").split(",")) {
            if (member == null || member.isEmpty()) continue;
            members.add(UUID.fromString(member));
        }
        return new Team(leader, members, teamName);
    }

    public void setTeamInfo(Team team) {
        JSONObject info = new JSONObject();
        info.put("leader", team.getTeamLeader().toString());
        StringBuilder sb = new StringBuilder();
        for (UUID member : team.getTeamMembers()) {
            sb.append(member.toString()).append(",");
        }
        info.put("members", sb.toString());
        teamsConfig.set(team.getTeamName(), Base64.getEncoder().encodeToString(info.toString().getBytes()));
        try {
            teamsConfig.save(teamsConfigFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (UUID member : team.getTeamMembers()) {
            playerConfig.set(member.toString(), team.getTeamName());
        }
        playerConfig.set(team.getTeamLeader().toString(), team.getTeamName());
        try {
            playerConfig.save(playerConfigFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removePlayerFromTeam(Player player) {
        String team = getTeam(player);
        if (team != null) {
            Team teamInfo = getTeamInfo(team);

            if (teamInfo.getTeamLeader().toString().equalsIgnoreCase(player.getUniqueId().toString())) {
                deleteTeam(team);
                playerConfig.set(player.getUniqueId().toString(), null);
            } else {
                teamInfo.removeTeamMember(player.getUniqueId());
                playerConfig.set(player.getUniqueId().toString(), null);
                setTeamInfo(teamInfo);
            }

            try {
                playerConfig.save(playerConfigFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
            player.sendMessage("§aDu hast dein Team verlassen!");
        }else{
            player.sendMessage("§cDu bist in keinem Team!");
        }
    }

    public void addPlayerToTeam(Player player, String teamString) {
        Team team = getTeamInfo(teamString);
        team.addTeamMember(player.getUniqueId());
        setTeamInfo(team);
    }

    public void deleteTeam(String teamName) {
        teamsConfig.set(teamName, null);
        try {
            teamsConfig.save(playerConfigFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void requestJoin(Player player, String teamString) {
        Team team = getTeamInfo(teamString);
        if (team != null) {
            Player teamLeader = Bukkit.getPlayer(team.getTeamLeader());
            if (teamLeader != null && teamLeader.isOnline()) {
                teamLeader.sendMessage("§c" + player.getName() + " §amöchte deinem Team beitreten!");
                teamLeader.sendMessage("§a/team accept " + player.getName() + " §aum die Anfrage anzunehmen!");
                player.sendMessage("§aDu hast eine Anfrage gestellt, um §c" + teamLeader.getName() + "§a's Team beizutreten!");
            }else{
                player.sendMessage("§cTeam leader ist offline");
            }
        } else {
            player.sendMessage("§cTeam existiert nicht!");
        }
    }

    public boolean isInSameTeam(Player player1, Player player2) {
        String team1 = getTeam(player1);
        if (team1 != null) {
            return (team1.equalsIgnoreCase(getTeam(player2)));
        }
        return false;
    }
}