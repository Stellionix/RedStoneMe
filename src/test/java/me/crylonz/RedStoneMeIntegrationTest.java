package me.crylonz;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.WorldMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import org.bukkit.command.Command;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class RedStoneMeIntegrationTest {

    @TempDir
    Path tempDir;

    private ServerMock server;
    private WorldMock world;
    private TestPluginFactory.TestPlugin plugin;
    private PlayerMock owner;
    private PlayerMock guest;
    private RSMCommandExecutor executor;
    private Command command;

    @BeforeEach
    void setUp() {
        server = MockBukkit.mock();
        world = server.addSimpleWorld("world");
        plugin = TestPluginFactory.create(tempDir);
        owner = server.addPlayer("owner");
        guest = server.addPlayer("guest");
        owner.setOp(true);
        guest.setOp(true);
        executor = new RSMCommandExecutor(plugin);
        command = new TestCommand("rsm");
        RedStoneMe.redStoneTriggers.clear();
    }

    @AfterEach
    void tearDown() {
        RedStoneMe.redStoneTriggers.clear();
        MockBukkit.unmock();
    }

    @Test
    void blockBreakRemovesTriggerAndPersistsDeletion() {
        RedStoneTrigger trigger = createTrigger("gate");
        RedStoneMe.redStoneTriggers.add(trigger);

        BlockBreakEvent event = new BlockBreakEvent(world.getBlockAt(trigger.getLoc()), owner);
        plugin.onBlockBreakEvent(event);

        assertTrue(RedStoneMe.redStoneTriggers.isEmpty());
        assertEquals(1, plugin.getPersistCalls());
    }

    @Test
    void commandsUpdateTriggerAndPersistChanges() {
        RedStoneTrigger trigger = createTrigger("gate");
        RedStoneMe.redStoneTriggers.add(trigger);

        assertTrue(executor.onCommand(owner, command, "rsm", new String[]{"radius", "gate", "9"}));
        assertTrue(executor.onCommand(owner, command, "rsm", new String[]{"state", "gate", "off"}));
        assertTrue(executor.onCommand(owner, command, "rsm", new String[]{"public", "gate", "on"}));
        assertTrue(executor.onCommand(owner, command, "rsm", new String[]{"add", "gate", "guest"}));
        assertTrue(executor.onCommand(owner, command, "rsm", new String[]{"remove", "gate", "guest"}));

        assertEquals(9, trigger.getRadius());
        assertFalse(trigger.isEnable());
        assertTrue(trigger.isPublic());
        assertFalse(trigger.getPlayers().contains(guest.getUniqueId().toString()));
        assertEquals(5, plugin.getPersistCalls());
    }

    @Test
    void destroyCommandRemovesTriggerAndPersistsDeletion() {
        RedStoneTrigger trigger = createTrigger("gate");
        RedStoneMe.redStoneTriggers.add(trigger);

        assertTrue(executor.onCommand(owner, command, "rsm", new String[]{"destroy", "gate"}));

        assertTrue(RedStoneMe.redStoneTriggers.isEmpty());
        assertEquals(1, plugin.getPersistCalls());
    }

    @Test
    void migrateLegacyConfigImportsYamlIntoSQLite() throws Exception {
        SQLiteStorage storage = new SQLiteStorage(plugin);
        storage.initialize();
        plugin.setPersistStorage(storage);
        injectStorage(storage);

        RedStoneTrigger trigger = createTrigger("legacy");
        ArrayList<RedStoneTrigger> legacyTriggers = new ArrayList<>();
        legacyTriggers.add(trigger);
        plugin.getConfig().set("redStoneTriggers", legacyTriggers);
        RedStoneMe.redStoneTriggers.clear();
        Files.createFile(tempDir.resolve("config.yml"));

        Method migrate = RedStoneMe.class.getDeclaredMethod("migrateLegacyConfig");
        migrate.setAccessible(true);
        migrate.invoke(plugin);

        ArrayList<RedStoneTrigger> loaded = storage.loadTriggers();
        assertEquals(1, loaded.size());
        assertEquals("legacy", loaded.get(0).getTriggerName());
        assertNull(plugin.getConfig().get("redStoneTriggers"));
    }

    @Test
    void tabCompletionListsOnlyAccessibleTriggers() {
        RedStoneTrigger owned = createTrigger("owned");
        RedStoneTrigger shared = createTrigger("shared");
        shared.addPlayer(guest);
        RedStoneTrigger hidden = createTrigger("hidden");
        hidden.setOwner(guest.getUniqueId().toString());
        hidden.setPlayers(new ArrayList<>(Collections.singletonList(guest.getUniqueId().toString())));
        RedStoneMe.redStoneTriggers.add(owned);
        RedStoneMe.redStoneTriggers.add(shared);
        RedStoneMe.redStoneTriggers.add(hidden);

        TabCompletion completion = new TabCompletion();
        owner.setOp(true);

        assertTrue(completion.onTabComplete(owner, command, "rsm", new String[]{"destroy", ""}).contains("owned"));
        assertTrue(completion.onTabComplete(owner, command, "rsm", new String[]{"destroy", ""}).contains("shared"));
        assertFalse(completion.onTabComplete(owner, command, "rsm", new String[]{"destroy", ""}).contains("hidden"));
    }

    private RedStoneTrigger createTrigger(String name) {
        Location location = new Location(world, 30, 64, 30);
        world.getBlockAt(location).setType(Material.STONE);
        RedStoneTrigger trigger = new RedStoneTrigger(name, 5, location, Material.STONE, owner);
        trigger.addPlayer(owner);
        return trigger;
    }

    private void injectStorage(SQLiteStorage storage) throws Exception {
        Field storageField = RedStoneMe.class.getDeclaredField("storage");
        storageField.setAccessible(true);
        storageField.set(plugin, storage);
    }

    private static final class TestCommand extends Command {

        private TestCommand(String name) {
            super(name);
        }

        @Override
        public boolean execute(org.bukkit.command.CommandSender sender, String commandLabel, String[] args) {
            return false;
        }
    }
}
