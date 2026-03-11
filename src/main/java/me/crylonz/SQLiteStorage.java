package me.crylonz;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class SQLiteStorage {

    private final RedStoneMe plugin;
    private final String jdbcUrl;

    public SQLiteStorage(RedStoneMe plugin) {
        this.plugin = plugin;
        File databaseFile = new File(plugin.getDataFolder(), "redstoneme.db");
        this.jdbcUrl = "jdbc:sqlite:" + databaseFile.getAbsolutePath();
    }

    public void initialize() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("SQLite JDBC driver not found", e);
        }

        try (Connection connection = DriverManager.getConnection(jdbcUrl);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS triggers (" +
                    "trigger_name TEXT PRIMARY KEY," +
                    "owner_uuid TEXT NOT NULL," +
                    "radius INTEGER NOT NULL," +
                    "world_name TEXT NOT NULL," +
                    "x REAL NOT NULL," +
                    "y REAL NOT NULL," +
                    "z REAL NOT NULL," +
                    "enabled INTEGER NOT NULL," +
                    "material TEXT NOT NULL," +
                    "public_access INTEGER NOT NULL" +
                    ")");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS trigger_players (" +
                    "trigger_name TEXT NOT NULL," +
                    "player_uuid TEXT NOT NULL," +
                    "PRIMARY KEY (trigger_name, player_uuid)," +
                    "FOREIGN KEY (trigger_name) REFERENCES triggers(trigger_name) ON DELETE CASCADE" +
                    ")");
        } catch (SQLException e) {
            throw new IllegalStateException("Unable to initialize SQLite schema", e);
        }
    }

    public ArrayList<RedStoneTrigger> loadTriggers() {
        ArrayList<RedStoneTrigger> triggers = new ArrayList<>();
        String query = "SELECT trigger_name, owner_uuid, radius, world_name, x, y, z, enabled, material, public_access " +
                "FROM triggers ORDER BY trigger_name";

        try (Connection connection = DriverManager.getConnection(jdbcUrl);
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                String triggerName = resultSet.getString("trigger_name");
                String worldName = resultSet.getString("world_name");
                Location location = new Location(
                        Bukkit.getWorld(worldName),
                        resultSet.getDouble("x"),
                        resultSet.getDouble("y"),
                        resultSet.getDouble("z")
                );

                Material material = Material.matchMaterial(resultSet.getString("material"));
                if (material == null) {
                    material = Material.AIR;
                    plugin.getLogger().warning("Unknown material for trigger " + triggerName + ", defaulting to AIR");
                }

                triggers.add(new RedStoneTrigger(
                        triggerName,
                        resultSet.getInt("radius"),
                        loadPlayers(connection, triggerName),
                        location,
                        resultSet.getInt("enabled") == 1,
                        material,
                        resultSet.getString("owner_uuid"),
                        worldName,
                        resultSet.getInt("public_access") == 1
                ));
            }
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Unable to load triggers from SQLite", e);
        }

        return triggers;
    }

    public void saveTriggers(List<RedStoneTrigger> triggers) {
        try (Connection connection = DriverManager.getConnection(jdbcUrl)) {
            connection.setAutoCommit(false);
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate("DELETE FROM trigger_players");
                statement.executeUpdate("DELETE FROM triggers");
            }

            try (PreparedStatement triggerStatement = connection.prepareStatement(
                    "INSERT INTO triggers(trigger_name, owner_uuid, radius, world_name, x, y, z, enabled, material, public_access) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                 PreparedStatement playerStatement = connection.prepareStatement(
                         "INSERT INTO trigger_players(trigger_name, player_uuid) VALUES (?, ?)")) {
                for (RedStoneTrigger trigger : triggers) {
                    triggerStatement.setString(1, trigger.getTriggerName());
                    triggerStatement.setString(2, trigger.getOwner());
                    triggerStatement.setInt(3, trigger.getRadius());
                    triggerStatement.setString(4, trigger.getWorldName());
                    triggerStatement.setDouble(5, trigger.getLoc().getX());
                    triggerStatement.setDouble(6, trigger.getLoc().getY());
                    triggerStatement.setDouble(7, trigger.getLoc().getZ());
                    triggerStatement.setInt(8, trigger.isEnable() ? 1 : 0);
                    triggerStatement.setString(9, trigger.getMaterial().name());
                    triggerStatement.setInt(10, trigger.isPublic() ? 1 : 0);
                    triggerStatement.addBatch();

                    for (String playerUuid : trigger.getPlayers()) {
                        playerStatement.setString(1, trigger.getTriggerName());
                        playerStatement.setString(2, playerUuid);
                        playerStatement.addBatch();
                    }
                }

                triggerStatement.executeBatch();
                playerStatement.executeBatch();
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Unable to save triggers to SQLite", e);
        }
    }

    public boolean hasTriggers() {
        String query = "SELECT 1 FROM triggers LIMIT 1";
        try (Connection connection = DriverManager.getConnection(jdbcUrl);
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            return resultSet.next();
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Unable to inspect SQLite trigger table", e);
            return false;
        }
    }

    private ArrayList<String> loadPlayers(Connection connection, String triggerName) throws SQLException {
        ArrayList<String> players = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT player_uuid FROM trigger_players WHERE trigger_name = ? ORDER BY player_uuid")) {
            statement.setString(1, triggerName);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    players.add(resultSet.getString("player_uuid"));
                }
            }
        }
        return players;
    }
}
