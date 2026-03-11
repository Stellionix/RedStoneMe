package me.crylonz;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.WorldMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.Powerable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class RedStoneTriggerTest {

    private ServerMock server;
    private WorldMock world;
    private PlayerMock owner;
    private PlayerMock guest;

    @BeforeEach
    void setUp() {
        server = MockBukkit.mock();
        world = server.addSimpleWorld("world");
        owner = server.addPlayer("owner");
        guest = server.addPlayer("guest");
    }

    @AfterEach
    void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    void triggerTurnsBlockIntoTorchAndBack() {
        Location location = new Location(world, 10, 64, 10);
        world.getBlockAt(location).setType(Material.STONE);
        RedStoneTrigger trigger = new RedStoneTrigger("gate", 4, location, Material.STONE, owner);

        trigger.trigger(true);
        assertEquals(Material.REDSTONE_TORCH, world.getBlockAt(location).getType());

        trigger.trigger(false);
        assertEquals(Material.STONE, world.getBlockAt(location).getType());
    }

    @Test
    void triggerCanUseAlternativeActionMaterial() {
        Location location = new Location(world, 10, 64, 11);
        world.getBlockAt(location).setType(Material.STONE);
        RedStoneTrigger trigger = new RedStoneTrigger("gate", 4, location, Material.STONE, owner);
        trigger.setAction(TriggerAction.REDSTONE_BLOCK);

        trigger.trigger(true);
        assertEquals(Material.REDSTONE_BLOCK, world.getBlockAt(location).getType());

        trigger.trigger(false);
        assertEquals(Material.STONE, world.getBlockAt(location).getType());
    }

    @Test
    void leverActionPowersLeverAndRestoresOriginalBlock() {
        Location location = new Location(world, 10, 64, 12);
        world.getBlockAt(location).setType(Material.STONE);
        RedStoneTrigger trigger = new RedStoneTrigger("gate", 4, location, Material.STONE, owner);
        trigger.setAction(TriggerAction.LEVER);

        trigger.trigger(true);

        assertEquals(Material.LEVER, world.getBlockAt(location).getType());
        assertTrue(((Powerable) world.getBlockAt(location).getBlockData()).isPowered());

        trigger.trigger(false);

        assertEquals(Material.STONE, world.getBlockAt(location).getType());
    }

    @Test
    void disabledTriggerKeepsOriginalMaterial() {
        Location location = new Location(world, 11, 64, 11);
        world.getBlockAt(location).setType(Material.STONE);
        RedStoneTrigger trigger = new RedStoneTrigger("gate", 4, location, Material.STONE, owner);
        trigger.setEnable(false);

        trigger.trigger(true);

        assertEquals(Material.STONE, world.getBlockAt(location).getType());
    }

    @Test
    void serializeAndDeserializePreservesTriggerState() {
        Location location = new Location(world, 12, 64, 12);
        RedStoneTrigger trigger = new RedStoneTrigger("gate", 6, location, Material.STONE, owner);
        trigger.addPlayer(guest);
        trigger.setEnable(false);
        trigger.setPublic(true);
        trigger.setAction(TriggerAction.REDSTONE_BLOCK);

        Map<String, Object> serialized = trigger.serialize();
        RedStoneTrigger restored = RedStoneTrigger.deserialize(serialized);

        assertEquals("gate", restored.getTriggerName());
        assertEquals(6, restored.getRadius());
        assertEquals(location, restored.getLoc());
        assertEquals(Material.STONE, restored.getMaterial());
        assertEquals(owner.getUniqueId().toString(), restored.getOwner());
        assertTrue(restored.isPublic());
        assertFalse(restored.isEnable());
        assertEquals(TriggerAction.REDSTONE_BLOCK, restored.getAction());
        assertTrue(restored.getPlayers().contains(guest.getUniqueId().toString()));
    }

    @Test
    void hasAccessMatchesOwnerAndAllowedPlayers() {
        Location location = new Location(world, 13, 64, 13);
        PlayerMock stranger = server.addPlayer("stranger");
        RedStoneTrigger trigger = new RedStoneTrigger("gate", 5, location, Material.STONE, owner);
        trigger.addPlayer(guest);

        assertTrue(trigger.hasAccess(owner));
        assertTrue(trigger.hasAccess(guest));
        assertFalse(trigger.hasAccess(stranger));
    }
}
