package me.crylonz;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@SerializableAs("RedStoneTrigger")
public class RedStoneTrigger implements ConfigurationSerializable {

    private String triggerName;
    private String owner;
    private ArrayList<String> players = new ArrayList<>();
    private int radius;
    private Location loc;
    private boolean isEnable;
    private Material material;
    private TriggerAction action;
    private String worldName;
    private boolean isPublic;

    public RedStoneTrigger(String name, int radius, ArrayList<String> players, Location loc, boolean isEnable,
                           Material material, TriggerAction action, String owner, String worldName, boolean isPublic) {
        this.triggerName = name;
        this.players = players;
        this.radius = radius;
        this.loc = loc;
        this.isEnable = isEnable;
        this.material = material;
        this.action = action;
        this.owner = owner;
        this.worldName = worldName;
        this.isPublic = isPublic;
    }

    public RedStoneTrigger(String name, int radius, Location loc, Material material, Player owner) {
        this.triggerName = name;
        this.radius = radius;
        this.loc = loc;
        this.isEnable = true;
        this.material = material;
        this.action = TriggerAction.REDSTONE_TORCH;
        this.owner = owner.getUniqueId().toString();
        this.worldName = loc.getWorld().getName();
        this.isPublic = false;
    }

    public static RedStoneTrigger deserialize(Map<String, Object> map) {
        String loc[] = ((String) map.get("location")).split(";");
        Location myloc = new Location(Bukkit.getWorld(loc[0]), Double.parseDouble(loc[1]), Double.parseDouble(loc[2]), Double.parseDouble(loc[3]));
        {
            return new RedStoneTrigger(
                    (String) map.get("triggername"),
                    (int) map.get("radius"),
                    (ArrayList<String>) map.get("players"),
                    myloc,
                    (boolean) map.get("isEnable"),
                    Material.getMaterial((String) map.get("material")),
                    TriggerAction.fromName((String) map.get("action")),
                    (String) map.get("owner"),
                    (String) map.get("worldName"),
                    (boolean) map.get("isPublic"));
        }
    }

    public void addPlayer(Player p) {
        this.players.add(p.getUniqueId().toString());
    }

    public void addPlayer(UUID playerUUID) {
        this.players.add(playerUUID.toString());
    }

    public boolean removePlayer(Player p) {
        return this.players.remove(p.getUniqueId().toString());
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public String getTriggerName() {
        return triggerName;
    }

    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName;
    }

    public Location getLoc() {
        return loc;
    }

    public void setLoc(Location loc) {
        this.loc = loc;
    }

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }

    public ArrayList<String> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<String> players) {
        this.players = players;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public TriggerAction getAction() {
        return action;
    }

    public void setAction(TriggerAction action) {
        this.action = action;
    }

    public String getWorldName() {
        return worldName;
    }

    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("triggername", triggerName);
        map.put("players", players);
        map.put("radius", radius);
        map.put("location", worldName + ";" + loc.getX() + ";" + loc.getY() + ";" + loc.getZ() + ";");
        map.put("isEnable", isEnable);
        map.put("material", material.toString());
        map.put("action", action.name());
        map.put("owner", owner);
        map.put("worldName", worldName);
        map.put("isPublic", isPublic);
        return map;
    }

    public void trigger(boolean b) {
        action.apply(loc, material, isEnable(), b);
    }

    public boolean hasAccess(Player player) {
        if (this.getOwner().equals(player.getUniqueId().toString())) {
            return true;
        }
        return this.getPlayers().contains(player.getUniqueId().toString());
    }
}

