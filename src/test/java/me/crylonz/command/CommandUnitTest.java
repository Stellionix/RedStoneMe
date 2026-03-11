package me.crylonz.command;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.WorldMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import me.crylonz.RedStoneMe;
import me.crylonz.RedStoneTrigger;
import me.crylonz.TestPluginFactory;
import me.crylonz.TriggerAction;
import org.bukkit.Location;
import org.bukkit.Material;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CommandUnitTest {

    @TempDir
    Path tempDir;

    private ServerMock server;
    private WorldMock world;
    private TestPluginFactory.TestPlugin plugin;
    private CommandContext context;
    private PlayerMock owner;
    private PlayerMock guest;

    @BeforeEach
    void setUp() {
        server = MockBukkit.mock();
        world = server.addSimpleWorld("world");
        plugin = TestPluginFactory.create(tempDir);
        context = new CommandContext(plugin);
        owner = server.addPlayer("owner");
        guest = server.addPlayer("guest");
        owner.setOp(true);
        guest.setOp(true);
        RedStoneMe.redStoneTriggers.clear();
    }

    @AfterEach
    void tearDown() {
        RedStoneMe.redStoneTriggers.clear();
        MockBukkit.unmock();
    }

    @Test
    void radiusCommandUpdatesRadius() {
        RadiusCommand command = new RadiusCommand(context);
        RedStoneTrigger trigger = createTrigger("gate");
        RedStoneMe.redStoneTriggers.add(trigger);

        assertTrue(command.execute(owner, new String[]{"radius", "gate", "12"}));

        assertEquals(12, trigger.getRadius());
        assertEquals(1, plugin.getPersistCalls());
    }

    @Test
    void addAndRemoveCommandsManagePlayers() {
        AddCommand addCommand = new AddCommand(context);
        RemoveCommand removeCommand = new RemoveCommand(context);
        RedStoneTrigger trigger = createTrigger("gate");
        RedStoneMe.redStoneTriggers.add(trigger);

        assertTrue(addCommand.execute(owner, new String[]{"add", "gate", "guest"}));
        assertTrue(trigger.getPlayers().contains(guest.getUniqueId().toString()));

        assertTrue(removeCommand.execute(owner, new String[]{"remove", "gate", "guest"}));
        assertFalse(trigger.getPlayers().contains(guest.getUniqueId().toString()));
        assertEquals(2, plugin.getPersistCalls());
    }

    @Test
    void renameAndToggleCommandsUpdateTriggerState() {
        RenameCommand renameCommand = new RenameCommand(context);
        ToggleCommand toggleCommand = new ToggleCommand(context);
        RedStoneTrigger trigger = createTrigger("gate");
        RedStoneMe.redStoneTriggers.add(trigger);

        assertTrue(renameCommand.execute(owner, new String[]{"rename", "gate", "gate2"}));
        assertTrue(toggleCommand.execute(owner, new String[]{"toggle", "gate2"}));

        assertEquals("gate2", trigger.getTriggerName());
        assertFalse(trigger.isEnable());
        assertEquals(2, plugin.getPersistCalls());
    }

    @Test
    void clearPlayersCommandKeepsOwnerOnly() {
        ClearPlayersCommand command = new ClearPlayersCommand(context);
        RedStoneTrigger trigger = createTrigger("gate");
        trigger.addPlayer(guest);
        RedStoneMe.redStoneTriggers.add(trigger);

        assertTrue(command.execute(owner, new String[]{"clearplayers", "gate"}));

        assertEquals(1, trigger.getPlayers().size());
        assertTrue(trigger.getPlayers().contains(owner.getUniqueId().toString()));
        assertEquals(1, plugin.getPersistCalls());
    }

    @Test
    void actionCommandUpdatesTriggerAction() {
        ActionCommand command = new ActionCommand(context);
        RedStoneTrigger trigger = createTrigger("gate");
        RedStoneMe.redStoneTriggers.add(trigger);

        assertTrue(command.execute(owner, new String[]{"action", "gate", "REDSTONE_BLOCK"}));

        assertEquals(TriggerAction.REDSTONE_BLOCK, trigger.getAction());
        assertEquals(1, plugin.getPersistCalls());
    }

    @Test
    void setOwnerCommandTransfersOwnership() {
        SetOwnerCommand command = new SetOwnerCommand(context);
        RedStoneTrigger trigger = createTrigger("gate");
        RedStoneMe.redStoneTriggers.add(trigger);

        assertTrue(command.execute(owner, new String[]{"setowner", "gate", "guest"}));

        assertEquals(guest.getUniqueId().toString(), trigger.getOwner());
        assertTrue(trigger.getPlayers().contains(guest.getUniqueId().toString()));
        assertEquals(1, plugin.getPersistCalls());
    }

    @Test
    void debugCommandUpdatesConfigOnly() {
        DebugCommand command = new DebugCommand(context);

        assertTrue(command.execute(owner, new String[]{"debug", "on"}));

        assertTrue(plugin.getConfig().getBoolean("debug.enabled"));
        assertEquals(0, plugin.getPersistCalls());
    }

    @Test
    void destroyCommandRemovesTrigger() {
        DestroyCommand command = new DestroyCommand(context);
        RedStoneTrigger trigger = createTrigger("gate");
        RedStoneMe.redStoneTriggers.add(trigger);

        assertTrue(command.execute(owner, new String[]{"destroy", "gate"}));

        assertTrue(RedStoneMe.redStoneTriggers.isEmpty());
        assertEquals(1, plugin.getPersistCalls());
    }

    @Test
    void whoCommandSupportsPagination() {
        WhoCommand command = new WhoCommand(context);
        RedStoneTrigger trigger = createTrigger("gate");
        trigger.setPlayers(new ArrayList<String>());
        trigger.addPlayer(owner);
        for (int i = 0; i < 6; i++) {
            PlayerMock extra = server.addPlayer("p" + i);
            trigger.addPlayer(extra);
        }
        RedStoneMe.redStoneTriggers.add(trigger);

        assertTrue(command.execute(owner, new String[]{"who", "gate", "2"}));

        String header = owner.nextMessage();
        assertTrue(header.contains("page 2/2"));
    }

    private RedStoneTrigger createTrigger(String name) {
        Location location = new Location(world, 20, 64, 20);
        world.getBlockAt(location).setType(Material.STONE);
        RedStoneTrigger trigger = new RedStoneTrigger(name, 5, location, Material.STONE, owner);
        trigger.addPlayer(owner);
        return trigger;
    }
}
