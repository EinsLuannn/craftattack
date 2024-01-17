package com.luan.craftattack.expansions.teams;

import java.util.ArrayList;
import java.util.UUID;

public class Team {
    private UUID teamLeader;
    private final ArrayList<UUID> teamMembers;
    private final String teamName;

    public Team(UUID teamLeader, ArrayList<UUID> teamMembers, String teamName) {
        this.teamLeader = teamLeader;
        this.teamMembers = teamMembers;
        this.teamName = teamName;
    }

    public UUID getTeamLeader() {
        return teamLeader;
    }

    public ArrayList<UUID> getTeamMembers() {
        return teamMembers;
    }

    public void setTeamLeader(UUID teamLeader) {
        this.teamLeader = teamLeader;
    }

    public void addTeamMember(UUID teamMember) {
        this.teamMembers.add(teamMember);
    }

    public void removeTeamMember(UUID teamMember) {
        this.teamMembers.remove(teamMember);
    }

    public String getTeamName() {
        return teamName;
    }
}