package com.luan.craftattack.expansions;

import com.luan.craftattack.CraftAttack;
import com.luan.craftattack.discord.DiscordLink;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Registration {
    private final File file;
    private HashMap<DiscordLink, Boolean> registration = new HashMap<>();

    public Registration (File file) {
        this.file = file;
    }

    public void load() throws IOException {

    }

    public void save() throws IOException {

    }

    public void accept(String username, String discordid) {
        // Whitelist player
        Bukkit.getOfflinePlayer(username).setWhitelisted(true);
        CraftAttack.getInstance().getDiscordIntegration().getCurrentGuild().addRoleToMember(
                CraftAttack.getInstance().getDiscordIntegration().getCurrentGuild().getMemberById(discordid),
                CraftAttack.getInstance().getDiscordIntegration().getCurrentGuild().getRoleById("993647131938263190")
        );
    }

    public void deny(String username, String discordid) {

    }
}
