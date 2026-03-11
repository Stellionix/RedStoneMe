package me.crylonz.command;

import me.crylonz.RedStoneMe;
import me.crylonz.RedStoneTrigger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static me.crylonz.RedStoneMe.isOwnerOfTrigger;
import static me.crylonz.RedStoneMe.redStoneTriggers;

public class CommandContext {

    public static final int PAGE_SIZE = 5;

    private final RedStoneMe plugin;

    public CommandContext(RedStoneMe plugin) {
        this.plugin = plugin;
    }

    public RedStoneMe plugin() {
        return plugin;
    }

    public boolean hasPermission(Player player, String permission) {
        return player.hasPermission(permission) || player.hasPermission("redstoneme.admin");
    }

    public boolean canManage(Player player, RedStoneTrigger trigger) {
        return isOwnerOfTrigger(player, trigger) || player.hasPermission("redstoneme.admin");
    }

    public RedStoneTrigger findTrigger(String name) {
        for (RedStoneTrigger trigger : redStoneTriggers) {
            if (trigger.getTriggerName().equalsIgnoreCase(name)) {
                return trigger;
            }
        }
        return null;
    }

    public Player findOnlinePlayer(String name) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getName().equalsIgnoreCase(name)) {
                return player;
            }
        }
        return null;
    }

    public boolean isOnOff(String value) {
        return value.equalsIgnoreCase("on") || value.equalsIgnoreCase("off");
    }

    public boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean success(Player player, String message) {
        player.sendMessage(prefix(true) + message);
        return true;
    }

    public boolean error(Player player, String message) {
        player.sendMessage(prefix(false) + message);
        return true;
    }

    public boolean usage(Player player, String usage) {
        return error(player, "Usage: " + usage);
    }

    public boolean deny(Player player, String permission) {
        return error(player, "You need " + permission + " permission to do that ");
    }

    public boolean ownerOnly(Player player) {
        return error(player, "Only the owner can access to this trigger");
    }

    public String prefix(boolean success) {
        return ChatColor.GOLD + "[RedStoneMe] " + (success ? ChatColor.GREEN : ChatColor.RED);
    }

    public String rawPrefix() {
        return ChatColor.GOLD + "[RedStoneMe] ";
    }

    public boolean sendTriggerPage(Player player, int page) {
        List<RedStoneTrigger> visible = new ArrayList<RedStoneTrigger>();
        for (RedStoneTrigger trigger : redStoneTriggers) {
            if (player.hasPermission("redstoneme.admin") || trigger.hasAccess(player)) {
                visible.add(trigger);
            }
        }
        if (visible.isEmpty()) {
            return error(player, "No triggers found");
        }

        int total = Math.max(1, (int) Math.ceil((double) visible.size() / PAGE_SIZE));
        int current = Math.max(1, Math.min(page, total));
        int start = (current - 1) * PAGE_SIZE;
        int end = Math.min(start + PAGE_SIZE, visible.size());

        player.sendMessage(rawPrefix() + ChatColor.GREEN + "Triggers list page " + current + "/" + total + " :");
        for (int i = start; i < end; i++) {
            RedStoneTrigger trigger = visible.get(i);
            OfflinePlayer owner = Bukkit.getOfflinePlayer(UUID.fromString(trigger.getOwner()));
            player.sendMessage(ChatColor.WHITE + "Name: " + ChatColor.LIGHT_PURPLE + trigger.getTriggerName()
                    + ChatColor.WHITE + " Owner: " + ChatColor.LIGHT_PURPLE + owner.getName());
        }
        return true;
    }

    public boolean sendPlayersPage(Player player, RedStoneTrigger trigger, int page, String titlePrefix) {
        List<String> lines = new ArrayList<String>();
        for (String uuid : trigger.getPlayers()) {
            lines.add(ChatColor.GOLD + "-" + ChatColor.GREEN + Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName());
        }
        if (lines.isEmpty()) {
            lines.add(ChatColor.GRAY + "No players found");
        }
        return sendPagedStrings(player, titlePrefix + trigger.getTriggerName(), lines, page);
    }

    public boolean sendHelpPage(Player player, int page) {
        List<String> lines = new ArrayList<String>();
        for (CommandSpec spec : CommandSpecs.ALL) {
            lines.add(commandLine(spec.usage(), spec.summary()));
        }
        return sendPagedStrings(player, "Commands list", lines, page);
    }

    public boolean sendInfo(Player player, RedStoneTrigger trigger) {
        Location location = trigger.getLoc();
        OfflinePlayer owner = Bukkit.getOfflinePlayer(UUID.fromString(trigger.getOwner()));
        player.sendMessage(rawPrefix() + ChatColor.GREEN + "Trigger " + trigger.getTriggerName() + " :");
        player.sendMessage(ChatColor.WHITE + "Owner: " + ChatColor.LIGHT_PURPLE + owner.getName());
        player.sendMessage(ChatColor.WHITE + "World: " + ChatColor.LIGHT_PURPLE + trigger.getWorldName());
        player.sendMessage(ChatColor.WHITE + "Position: " + ChatColor.LIGHT_PURPLE + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ());
        player.sendMessage(ChatColor.WHITE + "Radius: " + ChatColor.LIGHT_PURPLE + trigger.getRadius());
        player.sendMessage(ChatColor.WHITE + "State: " + ChatColor.LIGHT_PURPLE + (trigger.isEnable() ? "ON" : "OFF"));
        player.sendMessage(ChatColor.WHITE + "Access: " + ChatColor.LIGHT_PURPLE + (trigger.isPublic() ? "PUBLIC" : "PRIVATE"));
        player.sendMessage(ChatColor.WHITE + "Action: " + ChatColor.LIGHT_PURPLE + trigger.getAction().name());
        player.sendMessage(ChatColor.WHITE + "Material: " + ChatColor.LIGHT_PURPLE + trigger.getMaterial());
        player.sendMessage(ChatColor.WHITE + "Players: " + ChatColor.LIGHT_PURPLE + trigger.getPlayers().size());
        return true;
    }

    private boolean sendPagedStrings(Player player, String title, List<String> lines, int page) {
        int total = Math.max(1, (int) Math.ceil((double) lines.size() / PAGE_SIZE));
        int current = Math.max(1, Math.min(page, total));
        int start = (current - 1) * PAGE_SIZE;
        int end = Math.min(start + PAGE_SIZE, lines.size());

        player.sendMessage(rawPrefix() + ChatColor.GREEN + title + " page " + current + "/" + total + " :");
        for (int i = start; i < end; i++) {
            player.sendMessage(lines.get(i));
        }
        return true;
    }

    private String commandLine(String command, String description) {
        return ChatColor.LIGHT_PURPLE + command + " " + ChatColor.WHITE + description;
    }
}
