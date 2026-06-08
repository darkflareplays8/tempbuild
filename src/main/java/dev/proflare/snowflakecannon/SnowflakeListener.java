package dev.proflare.snowflakecannon;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class SnowflakeListener implements Listener {

    private final JavaPlugin plugin;
    private final Set<UUID> fired = new HashSet<>();

    private static final long CAST_DELAY_TICKS = 40L;

    public SnowflakeListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onFish(PlayerFishEvent event) {
        if (event.getState() != PlayerFishEvent.State.FISHING) return;

        Player player = event.getPlayer();
        ItemStack rod = player.getInventory().getItemInMainHand();

        if (!SnowflakeRod.isSnowflakeRod(rod)) {
            rod = player.getInventory().getItemInOffHand();
            if (!SnowflakeRod.isSnowflakeRod(rod)) return;
        }

        if (fired.contains(player.getUniqueId())) return;
        fired.add(player.getUniqueId());

        FishHook hook = event.getHook();
        ItemStack finalRod = rod;

        new BukkitRunnable() {
            @Override
            public void run() {
                Location target = hook.getLocation();
                hook.remove();

                player.playSound(player.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1.0f, 0.7f);

                SnowflakeStrike.fire(target, plugin);

                ItemStack held = player.getInventory().getItemInMainHand();
                if (SnowflakeRod.isSnowflakeRod(held)) {
                    if (held.getAmount() > 1) {
                        held.setAmount(held.getAmount() - 1);
                    } else {
                        player.getInventory().setItemInMainHand(null);
                    }
                } else {
                    ItemStack offhand = player.getInventory().getItemInOffHand();
                    if (SnowflakeRod.isSnowflakeRod(offhand)) {
                        if (offhand.getAmount() > 1) {
                            offhand.setAmount(offhand.getAmount() - 1);
                        } else {
                            player.getInventory().setItemInOffHand(null);
                        }
                    }
                }

                fired.remove(player.getUniqueId());
            }
        }.runTaskLater(plugin, CAST_DELAY_TICKS);
    }
}
