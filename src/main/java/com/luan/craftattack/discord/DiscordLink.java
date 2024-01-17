package com.luan.craftattack.discord;

import java.util.UUID;

public class DiscordLink {
    private final UUID uuid;
    private final String discordid;

    public DiscordLink(UUID uuid, String discordid) {
        this.uuid = uuid;
        this.discordid = discordid;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getDiscordid() {
        return discordid;
    }

}