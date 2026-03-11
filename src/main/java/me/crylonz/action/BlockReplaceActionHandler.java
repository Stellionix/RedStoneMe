package me.crylonz.action;

import org.bukkit.Location;
import org.bukkit.Material;

public final class BlockReplaceActionHandler implements TriggerActionHandler {

    private final Material activeMaterial;

    public BlockReplaceActionHandler(Material activeMaterial) {
        this.activeMaterial = activeMaterial;
    }

    @Override
    public void apply(Location location, Material originalMaterial, boolean enabled, boolean active) {
        if (location == null || location.getWorld() == null) {
            return;
        }

        if (!enabled || !active) {
            location.getBlock().setType(originalMaterial);
            return;
        }

        location.getBlock().setType(activeMaterial);
    }
}
