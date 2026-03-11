package me.crylonz;

import java.io.File;
import java.util.Iterator;
import java.util.*;
import java.util.logging.Logger;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

public class RedStoneMe extends JavaPlugin implements Listener {

    static {
        ConfigurationSerialization.registerClass(RedStoneTrigger.class, "RedStoneTrigger");
    }

    public static ArrayList<RedStoneTrigger> redStoneTriggers = new ArrayList<>();
    public static final Logger log = Logger.getLogger("Minecraft");
    private SQLiteStorage storage;

    public void onEnable() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(this, this);

        Objects.requireNonNull(this.getCommand("rsm"), "Command rsm not found")
                .setExecutor(new RSMCommandExecutor(this));

        Objects.requireNonNull(getCommand("rsm")).setTabCompleter(new TabCompletion());

        if (!getDataFolder().exists() && !getDataFolder().mkdirs()) {
            getLogger().warning("Unable to create plugin data directory");
        }

        saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        saveConfig();

        storage = new SQLiteStorage(this);
        storage.initialize();
        redStoneTriggers = storage.loadTriggers();
        migrateLegacyConfig();

        checkTrigger();
    }

    public void onDisable() {
        persistTriggers();
    }

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent e) {
        Iterator<RedStoneTrigger> iterator = redStoneTriggers.iterator();
        while (iterator.hasNext()) {
            RedStoneTrigger rt = iterator.next();
            if (rt.getLoc().equals(e.getBlock().getLocation())) {
                if (shouldProtectTriggerBreak(e.getPlayer(), rt)) {
                    e.setCancelled(true);
                    if (getConfig().getBoolean("messages.notify-protected-break", true)) {
                        e.getPlayer().sendMessage(colorize(getConfig().getString(
                                "messages.protected-break",
                                "&6[RedStoneMe] &cOnly the trigger owner can break this trigger."
                        )));
                    }
                    return;
                }

                if (!getConfig().getBoolean("protection.breaking-trigger-deletes-it", true)) {
                    return;
                }

                iterator.remove();
                persistTriggers();
                if (getConfig().getBoolean("messages.notify-trigger-broken", true)) {
                    e.getPlayer().sendMessage(colorize(
                            getConfig().getString(
                                    "messages.trigger-broken",
                                    "&6[RedStoneMe] &aTrigger {trigger} has been removed."
                            ).replace("{trigger}", rt.getTriggerName())
                    ));
                }
                break;
            }
        }
    }

    private void checkTrigger() {
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            public void run() {


                Bukkit.getOnlinePlayers();

                for (RedStoneTrigger rt : redStoneTriggers) {
                    boolean needTrigger = false;
                    if(!rt.isPublic()) {
                        for (String uuid : rt.getPlayers()) {
                            Player p = Bukkit.getPlayer(UUID.fromString(uuid));
                            if (p != null && p.getWorld().equals(rt.getLoc().getWorld())) {
                                if (p.getLocation().distance(rt.getLoc()) <= rt.getRadius()) {
                                    needTrigger = true;
                                    break;
                                }
                            }
                        }
                    } else {
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            if (player != null && player.getWorld().equals(rt.getLoc().getWorld())) {
                                if (player.getLocation().distance(rt.getLoc()) <= rt.getRadius()) {
                                    needTrigger = true;
                                    break;
                                }
                            }
                        }
                    }
                    rt.trigger(needTrigger);
                }

            }
        }, 20, 10);
    }


    public static boolean isOwnerOfTrigger(Player p, RedStoneTrigger rt) {
        return rt.getOwner().equalsIgnoreCase(p.getUniqueId().toString());
    }

    public void persistTriggers() {
        storage.saveTriggers(redStoneTriggers);
    }

    private boolean shouldProtectTriggerBreak(Player player, RedStoneTrigger trigger) {
        if (!getConfig().getBoolean("protection.prevent-non-owner-break", true)) {
            return false;
        }

        if (isOwnerOfTrigger(player, trigger)) {
            return !getConfig().getBoolean("protection.allow-owner-break", true);
        }

        if (player.hasPermission("redstoneme.admin")) {
            return !getConfig().getBoolean("protection.allow-admin-break", true);
        }

        return true;
    }

    private String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    @SuppressWarnings("unchecked")
    private void migrateLegacyConfig() {
        File configFile = new File(this.getDataFolder(), "config.yml");
        if (!configFile.exists() || storage.hasTriggers()) {
            return;
        }

        reloadConfig();
        Object serializedTriggers = getConfig().get("redStoneTriggers");
        if (!(serializedTriggers instanceof ArrayList)) {
            return;
        }

        redStoneTriggers = (ArrayList<RedStoneTrigger>) serializedTriggers;
        if (redStoneTriggers == null || redStoneTriggers.isEmpty()) {
            redStoneTriggers = new ArrayList<>();
            return;
        }

        persistTriggers();
        getLogger().info("Migrated " + redStoneTriggers.size() + " triggers from config.yml to SQLite");
        getConfig().set("redStoneTriggers", null);
        saveConfig();
    }
}

