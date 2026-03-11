package me.crylonz;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class TriggerScheduler {

    private final RedStoneMe plugin;

    public TriggerScheduler(RedStoneMe plugin) {
        this.plugin = plugin;
    }

    public void start() {
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                for (RedStoneTrigger trigger : RedStoneMe.redStoneTriggers) {
                    trigger.trigger(shouldActivate(trigger));
                }
            }
        }, 20L, 10L);
    }

    private boolean shouldActivate(RedStoneTrigger trigger) {
        if (trigger.isPublic()) {
            return isPublicTriggerActive(trigger);
        }
        return isPrivateTriggerActive(trigger);
    }

    private boolean isPrivateTriggerActive(RedStoneTrigger trigger) {
        for (String uuid : trigger.getPlayers()) {
            Player player = Bukkit.getPlayer(UUID.fromString(uuid));
            if (isPlayerInsideTrigger(trigger, player)) {
                return true;
            }
        }
        return false;
    }

    private boolean isPublicTriggerActive(RedStoneTrigger trigger) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (isPlayerInsideTrigger(trigger, player)) {
                return true;
            }
        }
        return false;
    }

    private boolean isPlayerInsideTrigger(RedStoneTrigger trigger, Player player) {
        return player != null
                && player.getWorld().equals(trigger.getLoc().getWorld())
                && player.getLocation().distance(trigger.getLoc()) <= trigger.getRadius();
    }
}
