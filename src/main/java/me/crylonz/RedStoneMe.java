package me.crylonz;

import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
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
    private TriggerRepository triggerRepository;
    private TriggerProtectionService triggerProtectionService;
    private TriggerScheduler triggerScheduler;
    private TriggerTargetBlockPolicy triggerTargetBlockPolicy;

    public void onEnable() {
        ensureDataFolder();
        initializeConfig();
        registerBukkitComponents();
        initializeServices();
    }

    public void onDisable() {
        getTriggerRepository().saveAll();
    }

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent event) {
        getTriggerProtectionService().handleBlockBreak(event);
    }

    public static boolean isOwnerOfTrigger(Player p, RedStoneTrigger rt) {
        return rt.getOwner().equalsIgnoreCase(p.getUniqueId().toString());
    }

    public void persistTriggers() {
        getTriggerRepository().saveAll();
    }

    public void debug(String message) {
        if (getConfig().getBoolean("debug.enabled", false)) {
            getLogger().info("[Debug] " + message);
        }
    }

    private void ensureDataFolder() {
        if (!getDataFolder().exists() && !getDataFolder().mkdirs()) {
            getLogger().warning("Unable to create plugin data directory");
        }
    }

    private void initializeConfig() {
        saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    private void registerBukkitComponents() {
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(this, this);

        Objects.requireNonNull(getCommand("rsm"), "Command rsm not found")
                .setExecutor(new RSMCommandExecutor(this));
        Objects.requireNonNull(getCommand("rsm"))
                .setTabCompleter(new TabCompletion());
    }

    private void initializeServices() {
        getTriggerRepository().initialize();
        getTriggerProtectionService();
        getTriggerScheduler().start();
    }

    private TriggerRepository getTriggerRepository() {
        if (triggerRepository == null) {
            triggerRepository = new TriggerRepository(this);
        }
        return triggerRepository;
    }

    private TriggerProtectionService getTriggerProtectionService() {
        if (triggerProtectionService == null) {
            triggerProtectionService = new TriggerProtectionService(this);
        }
        return triggerProtectionService;
    }

    private TriggerScheduler getTriggerScheduler() {
        if (triggerScheduler == null) {
            triggerScheduler = new TriggerScheduler(this);
        }
        return triggerScheduler;
    }

    public TriggerTargetBlockPolicy getTriggerTargetBlockPolicy() {
        if (triggerTargetBlockPolicy == null) {
            triggerTargetBlockPolicy = new TriggerTargetBlockPolicy(this);
        }
        return triggerTargetBlockPolicy;
    }
}

