package me.crylonz;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Iterator;

public class TriggerProtectionService {

    private final RedStoneMe plugin;

    public TriggerProtectionService(RedStoneMe plugin) {
        this.plugin = plugin;
    }

    public void handleBlockBreak(BlockBreakEvent event) {
        Iterator<RedStoneTrigger> iterator = RedStoneMe.redStoneTriggers.iterator();
        while (iterator.hasNext()) {
            RedStoneTrigger trigger = iterator.next();
            if (!trigger.getLoc().equals(event.getBlock().getLocation())) {
                continue;
            }

            if (shouldProtectTriggerBreak(event.getPlayer(), trigger)) {
                event.setCancelled(true);
                notifyProtectedBreak(event.getPlayer());
                return;
            }

            if (!plugin.getConfig().getBoolean("protection.breaking-trigger-deletes-it", true)) {
                return;
            }

            iterator.remove();
            plugin.persistTriggers();
            notifyTriggerBroken(event.getPlayer(), trigger);
            return;
        }
    }

    private boolean shouldProtectTriggerBreak(Player player, RedStoneTrigger trigger) {
        if (!plugin.getConfig().getBoolean("protection.prevent-non-owner-break", true)) {
            return false;
        }

        if (RedStoneMe.isOwnerOfTrigger(player, trigger)) {
            return !plugin.getConfig().getBoolean("protection.allow-owner-break", true);
        }

        if (player.hasPermission("redstoneme.admin")) {
            return !plugin.getConfig().getBoolean("protection.allow-admin-break", true);
        }

        return true;
    }

    private void notifyProtectedBreak(Player player) {
        if (!plugin.getConfig().getBoolean("messages.notify-protected-break", true)) {
            return;
        }

        player.sendMessage(colorize(plugin.getConfig().getString(
                "messages.protected-break",
                "&6[RedStoneMe] &cOnly the trigger owner can break this trigger."
        )));
    }

    private void notifyTriggerBroken(Player player, RedStoneTrigger trigger) {
        if (!plugin.getConfig().getBoolean("messages.notify-trigger-broken", true)) {
            return;
        }

        player.sendMessage(colorize(
                plugin.getConfig().getString(
                        "messages.trigger-broken",
                        "&6[RedStoneMe] &aTrigger {trigger} has been removed."
                ).replace("{trigger}", trigger.getTriggerName())
        ));
    }

    private String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
