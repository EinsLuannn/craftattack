package com.luan.craftattack.discord;

import com.luan.craftattack.CraftAttack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.PluginManager;

import javax.security.auth.login.LoginException;
import java.awt.*;
import java.util.ArrayList;

public class DiscordIntegration implements Listener {
    private JDA jda;

    public static String channelId;
    private ArrayList<String> hide = new ArrayList<>();

    public DiscordIntegration(String token, String channelId) throws LoginException, InterruptedException {
        DiscordIntegration.channelId = channelId;
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(this, CraftAttack.getInstance());

        Bukkit.getScheduler().runTaskAsynchronously(CraftAttack.getInstance(), ( ) -> {
           try {
               jda = JDABuilder.createDefault(token)
                       .addEventListeners(new DiscordListener())
                       .enableIntents(
                               GatewayIntent.DIRECT_MESSAGES,
                               GatewayIntent.GUILD_MEMBERS
                       )
                       .setChunkingFilter(ChunkingFilter.ALL)
                       .setMemberCachePolicy(MemberCachePolicy.ALL)
                       .build();
           } catch (LoginException exception) {
               throw new RuntimeException(exception);
           }
           try {
               jda.awaitReady();
           } catch (InterruptedException exception) {
               throw new RuntimeException(exception);
           }
           updateStatus();
           Bukkit.getScheduler().scheduleSyncRepeatingTask(CraftAttack.getInstance(), this::updateStatus, 0, 20 * 60 * 5);
        });
    }

    public void updateStatus() {
        jda.getPresence().setActivity(Activity.of(Activity.ActivityType.PLAYING, "mit " + Bukkit.getOnlinePlayers().size() + " Spielern"));
    }

    @EventHandler
    public void handleDeath(PlayerDeathEvent event) {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setTitle("Tod");
        embedBuilder.setColor(Color.RED);
        embedBuilder.setDescription(event.getDeathMessage());
        embedBuilder.setThumbnail("https://crafatar.com/avatars/" + event.getPlayer().getUniqueId().toString() + "?size=128&default=MHF_Steve&overlay");

        getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
    }

    @EventHandler
    public void handleJoin(PlayerJoinEvent event) {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setTitle("Join");
        embedBuilder.setColor(Color.GREEN);
        embedBuilder.setDescription(ChatColor.stripColor(event.getJoinMessage()));
        embedBuilder.setThumbnail("https://crafatar.com/avatars/" + event.getPlayer().getUniqueId().toString() + "?size=128&default=MHF_Steve&overlay");

        hide.add(event.getPlayer().getUniqueId().toString());
        Bukkit.getScheduler().runTaskLater(CraftAttack.getInstance(), () -> {
            if (event.getPlayer().isOnline()) {
                hide.remove(event.getPlayer().getUniqueId().toString());
                getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
            }
        }, 5);
        updateStatus();
    }

    @EventHandler
    public void handleQuit(PlayerQuitEvent event) {
        if (hide.contains(event.getPlayer().getUniqueId().toString())) {
            return;
        }
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Quit");
        embedBuilder.setColor(Color.GREEN);
        embedBuilder.setDescription(ChatColor.stripColor(event.getQuitMessage()));
        embedBuilder.setThumbnail("https://crafatar.com/avatars/" + event.getPlayer().getUniqueId().toString() + "?size=128&default=MHF_Steve&overlay");

        getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
        updateStatus();
    }

    public TextChannel getChannel() {
        return jda.getTextChannelById(channelId);
    }

    @EventHandler
    public void handleChat(AsyncPlayerChatEvent event) {
        getChannel().sendMessage((event.getPlayer().getName() + " » " + event.getMessage()).replaceAll("§.", "")).queue();
    }

    public Guild getCurrentGuild() {
        return jda.getGuilds().get(0);
    }

    public JDA getJda() {
        return jda;
    }
}
