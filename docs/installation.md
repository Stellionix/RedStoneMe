## Installation

### Requirements

- Minecraft Java Edition server using Bukkit, Spigot, Paper, or a compatible fork
- Bukkit API `1.13+`

### Install From a Release

1. Download the latest RedStoneMe JAR from a project distribution page.
2. Stop the server.
3. Place the JAR in the server `plugins/` directory.
4. Start the server.
5. Confirm that RedStoneMe generated `plugins/RedStoneMe/config.yml`.

Expected startup behavior:

- The plugin enables normally.
- A SQLite database file is created at `plugins/RedStoneMe/redstoneme.db` unless overridden in config.

### First Trigger

1. Stand near the block you want to use as a trigger source.
2. Aim at that block with your cursor.
3. Run `/rsm new BaseDoor 5`.
4. Walk into the trigger radius.
5. Confirm that the configured action is applied to the target block.

### Updating

Replace the old JAR with the new one and restart the server.

If you are updating from a legacy configuration-based storage setup, RedStoneMe migrates `redStoneTriggers` from `config.yml` into SQLite when the database is empty.
