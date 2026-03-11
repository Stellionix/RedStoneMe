package me.crylonz.action;

import org.bukkit.Location;
import org.bukkit.Material;

public interface TriggerActionHandler {

    void apply(Location location, Material originalMaterial, boolean enabled, boolean active);
}
