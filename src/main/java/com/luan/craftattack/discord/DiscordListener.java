package com.luan.craftattack.discord;

import com.luan.craftattack.CraftAttack;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class DiscordListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        System.out.println("Received message from " + event.getAuthor().getName() + ": " + event.getMessage().getContentRaw());

        // Registration
        if (event.getMessage().getContentRaw().startsWith("!register")) {
            String[] args = event.getMessage().getContentRaw().split(" ");
            if (args.length == 2) {
                String username = args[1];
                event.getGuild().getTextChannelById("1197213289420161164").sendMessage("<@" + event.getAuthor().getId() + "> möchte \"" + username + "\" registieren.\n\n" + event.getAuthor().getId() + ":" + username).queue(message -> {
                    message.addReaction("✅").queue();
                    message.addReaction("❌").queue();
                });
                event.getMessage().reply("Deine Registrierung wurde erfasst. Bitte warte auf eine Antwort.").queue();
            } else {
                event.getMessage().reply("<@" + event.getAuthor().getId() + "> Bitte gib einen Username an.").queue(message -> {
                    message.delete().queueAfter(5, TimeUnit.SECONDS);
                    event.getMessage().delete().queue();
                });
            }
        }

        if (!event.getTextChannel().getId().equalsIgnoreCase(DiscordIntegration.channelId)) return;
        Bukkit.broadcastMessage("§9[DC] §f" + event.getAuthor().getName() + " » " + event.getMessage().getContentRaw());
    }

    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        if (event.getUser().isBot()) return;

        if (!event.getChannel().getId().equalsIgnoreCase("1197213289420161164")) return;

        if (!event.getReaction().getReactionEmote().getName().equalsIgnoreCase("✅") && !event.getReaction().getReactionEmote().getName().equalsIgnoreCase("❌")) return;

        String[] split = event.retrieveMessage().complete().getContentRaw().split("\n");
        String[] args = split[split.length - 1].split(":");
        String discordid = args[0];
        String username = args[1];

        if (event.getReaction().getReactionEmote().getName().equalsIgnoreCase("✅")) {
            CraftAttack.getInstance().getRegistration().accept(username, discordid);
            event.retrieveMessage().complete().editMessage("<@" + discordid + "> \"" + username + "\" wurde von <@" + event.getUserId() + "> registriert.").queue();
        } else if (event.getReaction().getReactionEmote().getName().equalsIgnoreCase("❌")) {
            CraftAttack.getInstance().getRegistration().deny(username, discordid);
            event.retrieveMessage().complete().editMessage("<@" + discordid + "> \"" + username + "\" wurde von <@" + event.getUserId() + "> abgelehnt.").queue();
        }
    }
}