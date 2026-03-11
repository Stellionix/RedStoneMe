package me.crylonz;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.WorldMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import org.bukkit.Location;
import org.bukkit.Material;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SQLiteStorageTest {

    @TempDir
    Path tempDir;

    private ServerMock server;
    private WorldMock world;
    private TestPluginFactory.TestPlugin plugin;
    private SQLiteStorage storage;
    private PlayerMock owner;
    private PlayerMock guest;

    @BeforeEach
    void setUp() {
        server = MockBukkit.mock();
        world = server.addSimpleWorld("world");
        plugin = TestPluginFactory.create(tempDir);
        owner = server.addPlayer("owner");
        guest = server.addPlayer("guest");
        storage = new SQLiteStorage(plugin);
        resetDatabase();
    }

    @AfterEach
    void tearDown() {
        RedStoneMe.redStoneTriggers.clear();
        MockBukkit.unmock();
    }

    @Test
    void hasTriggersReflectsDatabaseState() {
        assertFalse(storage.hasTriggers());

        ArrayList<RedStoneTrigger> triggers = new ArrayList<>();
        triggers.add(createTrigger("gate"));
        storage.saveTriggers(triggers);

        assertTrue(storage.hasTriggers());
    }

    @Test
    void saveAndLoadRoundTripPreservesTriggerData() {
        RedStoneTrigger trigger = createTrigger("gate");
        trigger.setEnable(false);
        trigger.setPublic(true);
        trigger.addPlayer(guest);

        ArrayList<RedStoneTrigger> triggers = new ArrayList<>();
        triggers.add(trigger);
        storage.saveTriggers(triggers);

        ArrayList<RedStoneTrigger> loaded = storage.loadTriggers();

        assertEquals(1, loaded.size());
        RedStoneTrigger restored = loaded.get(0);
        assertEquals("gate", restored.getTriggerName());
        assertEquals(5, restored.getRadius());
        assertEquals(Material.STONE, restored.getMaterial());
        assertEquals(owner.getUniqueId().toString(), restored.getOwner());
        assertEquals("world", restored.getWorldName());
        assertFalse(restored.isEnable());
        assertTrue(restored.isPublic());
        assertTrue(restored.getPlayers().contains(owner.getUniqueId().toString()));
        assertTrue(restored.getPlayers().contains(guest.getUniqueId().toString()));
    }

    private RedStoneTrigger createTrigger(String name) {
        RedStoneTrigger trigger = new RedStoneTrigger(name, 5, new Location(world, 20, 64, 20), Material.STONE, owner);
        trigger.addPlayer(owner);
        return trigger;
    }

    private void resetDatabase() {
        File database = tempDir.resolve("redstoneme.db").toFile();
        if (database.exists()) {
            assertTrue(database.delete(), "Failed to delete test SQLite database");
        }
        storage.initialize();
    }
}
