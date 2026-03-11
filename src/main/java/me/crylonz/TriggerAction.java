package me.crylonz;

import me.crylonz.action.BlockReplaceActionHandler;
import me.crylonz.action.LeverActionHandler;
import me.crylonz.action.TriggerActionHandler;
import org.bukkit.Location;
import org.bukkit.Material;

public enum TriggerAction {
    REDSTONE_TORCH(new BlockReplaceActionHandler(Material.REDSTONE_TORCH)),
    REDSTONE_BLOCK(new BlockReplaceActionHandler(Material.REDSTONE_BLOCK)),
    LEVER(new LeverActionHandler());

    private final TriggerActionHandler handler;

    TriggerAction(TriggerActionHandler handler) {
        this.handler = handler;
    }

    public void apply(Location location, Material originalMaterial, boolean enabled, boolean active) {
        handler.apply(location, originalMaterial, enabled, active);
    }

    public static TriggerAction fromName(String value) {
        if (value == null || value.trim().isEmpty()) {
            return REDSTONE_TORCH;
        }

        for (TriggerAction action : values()) {
            if (action.name().equalsIgnoreCase(value)) {
                return action;
            }
        }

        return REDSTONE_TORCH;
    }
}
