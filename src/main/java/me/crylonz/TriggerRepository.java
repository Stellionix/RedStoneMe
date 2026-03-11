package me.crylonz;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TriggerRepository {

    private final RedStoneMe plugin;
    private final SQLiteStorage storage;

    public TriggerRepository(RedStoneMe plugin) {
        this.plugin = plugin;
        this.storage = new SQLiteStorage(plugin);
    }

    public void initialize() {
        storage.initialize();
        RedStoneMe.redStoneTriggers = storage.loadTriggers();
        migrateLegacyConfigIfNeeded();
    }

    public void saveAll() {
        storage.saveTriggers(RedStoneMe.redStoneTriggers);
    }

    private void migrateLegacyConfigIfNeeded() {
        File configFile = new File(plugin.getDataFolder(), "config.yml");
        if (!configFile.exists() || storage.hasTriggers()) {
            return;
        }

        plugin.reloadConfig();
        Object serializedTriggers = plugin.getConfig().get("redStoneTriggers");
        if (!(serializedTriggers instanceof ArrayList)) {
            return;
        }

        @SuppressWarnings("unchecked")
        ArrayList<RedStoneTrigger> migratedTriggers = (ArrayList<RedStoneTrigger>) serializedTriggers;
        if (migratedTriggers == null || migratedTriggers.isEmpty()) {
            RedStoneMe.redStoneTriggers = new ArrayList<RedStoneTrigger>();
            return;
        }

        RedStoneMe.redStoneTriggers = migratedTriggers;
        saveAll();
        plugin.getLogger().info("Migrated " + migratedTriggers.size() + " triggers from config.yml to SQLite");
        plugin.getConfig().set("redStoneTriggers", null);
        plugin.saveConfig();
    }
}
