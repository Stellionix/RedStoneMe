package me.crylonz;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import sun.misc.Unsafe;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.logging.Logger;

public final class TestPluginFactory {

    private TestPluginFactory() {
    }

    public static TestPlugin create(Path dataDirectory) {
        try {
            TestPlugin plugin = allocate(TestPlugin.class);
            plugin.initialize(dataDirectory.toFile(), Logger.getLogger("TestPlugin"));
            return plugin;
        } catch (InstantiationException | IllegalAccessException | NoSuchFieldException e) {
            throw new IllegalStateException("Unable to create test plugin instance", e);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> T allocate(Class<T> type)
            throws NoSuchFieldException, IllegalAccessException, InstantiationException {
        Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
        unsafeField.setAccessible(true);
        Unsafe unsafe = (Unsafe) unsafeField.get(null);
        return (T) unsafe.allocateInstance(type);
    }

    public static final class TestPlugin extends RedStoneMe {

        private File dataFolder;
        private Logger logger;
        private FileConfiguration config;
        private SQLiteStorage persistStorage;
        private int persistCalls;

        private void initialize(File dataFolder, Logger logger) {
            this.logger = logger;
            this.config = new YamlConfiguration();
            this.persistCalls = 0;
            setJavaPluginField("dataFolder", dataFolder);
        }

        public void setPersistStorage(SQLiteStorage persistStorage) {
            this.persistStorage = persistStorage;
        }

        public int getPersistCalls() {
            return persistCalls;
        }

        @Override
        public Logger getLogger() {
            return logger;
        }

        @Override
        public FileConfiguration getConfig() {
            return config;
        }

        @Override
        public void reloadConfig() {
        }

        @Override
        public void saveConfig() {
        }

        @Override
        public void persistTriggers() {
            persistCalls++;
            if (persistStorage != null) {
                persistStorage.saveTriggers(RedStoneMe.redStoneTriggers);
            }
        }

        private void setJavaPluginField(String fieldName, Object value) {
            try {
                Field field = org.bukkit.plugin.java.JavaPlugin.class.getDeclaredField(fieldName);
                field.setAccessible(true);
                field.set(this, value);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new IllegalStateException("Unable to set JavaPlugin field " + fieldName, e);
            }
        }
    }
}
