package com.luan.craftattack.expansions;

import com.luan.craftattack.CraftAttack;
import com.luan.craftattack.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;

import java.util.HashMap;
import java.util.UUID;

public class ElytraSpawn implements Listener {
    private Location spawn;
    private static int RADIUS = 15;

    private final HashMap<UUID, ItemStack> elytra = new HashMap<>();

    private ItemStack elytraItem;

    public ElytraSpawn(int radius) {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(this, CraftAttack.getInstance());

        RADIUS = radius;
        initialize();
    }

    public void initialize() {
        spawn = CraftAttack.getInstance().getSpawnManager().getSpawn();

        elytraItem = ItemBuilder.of(Material.ELYTRA).addEnchant(Enchantment.BINDING_CURSE, 1).build();

        Bukkit.getScheduler().scheduleSyncRepeatingTask(CraftAttack.getInstance(), new Runnable() {
            @Override
            public void run() {
                check();
            }
        }, 0L, 20L);
    }

    public void check() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getLocation().getWorld().getName().equalsIgnoreCase(spawn.getWorld().getName()) &&  player.getLocation().distance(spawn) <= RADIUS && !elytra.containsKey(player.getUniqueId())) {
                elytra.put(player.getUniqueId(), player.getInventory().getChestplate());
                player.getInventory().setChestplate(elytraItem);
            } else if (player.getLocation().getWorld().getName().equalsIgnoreCase(spawn.getWorld().getName()) && player.getLocation().distance(spawn) >= RADIUS && player.getLocation().add(0, -1, 0).getBlock().getType() != Material.AIR && elytra.containsKey(player.getUniqueId())) {
                player.getInventory().setChestplate(elytra.get(player.getUniqueId()));
                elytra.remove(player.getUniqueId());
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if (elytra.get(event.getPlayer().getUniqueId()) != null) {
            event.getPlayer().getInventory().setChestplate(elytra.get(event.getPlayer().getUniqueId()));
        }
        elytra.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onDeath(EntityDamageByEntityEvent event) {
        if (event.getEntityType() == EntityType.PLAYER && event.getDamager().getType() == EntityType.PLAYER) {
            Player player = (Player) event.getEntity();
            if (player.getHealthScale() - event.getFinalDamage() <= 0) {
                player.getInventory().setChestplate(elytra.get(player.getUniqueId()));
                elytra.remove(player.getUniqueId());
            }
        }
    }

}