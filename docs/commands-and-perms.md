## RedStoneMe - Commands & Permissions

Make sure you have [installed](installation.md) the plugin before using these commands.

This page documents the current `/rsm` command set and permission nodes.

### Commands

| Command | Permission | Description |
|---------|------------|-------------|
| `/rsm help [page]` | `redstoneme.help` | Show paginated help. |
| `/rsm new <TriggerName> <Radius>` | `redstoneme.new` | Create a trigger on the targeted block. |
| `/rsm destroy <TriggerName>` | `redstoneme.destroy` | Remove a trigger. |
| `/rsm info <TriggerName>` | `redstoneme.info` | Show trigger details visible to the player. |
| `/rsm tp <TriggerName>` | `redstoneme.tp` | Teleport to a trigger you can manage. |
| `/rsm rename <OldName> <NewName>` | `redstoneme.rename` | Rename a trigger you can manage. |
| `/rsm move <TriggerName>` | `redstoneme.move` | Move a trigger to the currently targeted block. |
| `/rsm toggle <TriggerName>` | `redstoneme.state` | Toggle trigger state between `ON` and `OFF`. |
| `/rsm action <TriggerName> [ActionType]` | `redstoneme.action` | Show or change the trigger action. |
| `/rsm add <TriggerName> <PlayerName>` | `redstoneme.add` | Allow a player on a private trigger. |
| `/rsm remove <TriggerName> <PlayerName>` | `redstoneme.remove` | Remove a player from a private trigger. |
| `/rsm clearplayers <TriggerName>` | `redstoneme.remove` | Remove all non-owner players from a trigger. |
| `/rsm who <TriggerName> [page]` | `redstoneme.list` | Show the allowed player list for a trigger. |
| `/rsm radius <TriggerName> [Radius]` | `redstoneme.radius` | Show or change the trigger radius. |
| `/rsm state <TriggerName> <OFF\|ON>` | `redstoneme.state` | Force the trigger enabled state. |
| `/rsm public <TriggerName> <OFF\|ON>` | `redstoneme.public` | Change trigger visibility between private and public. |
| `/rsm setowner <TriggerName> <PlayerName>` | `redstoneme.admin` | Transfer trigger ownership. |
| `/rsm list [page]` | `redstoneme.list` | List visible triggers. |
| `/rsm list <TriggerName> [page]` | `redstoneme.list` | List players attached to a trigger. |
| `/rsm reload` | `redstoneme.reload` | Reload plugin configuration. |
| `/rsm debug <OFF\|ON>` | `redstoneme.debug` | Enable or disable debug logging. |

### Permission Nodes

| Permission | Description | Default |
|------------|-------------|---------|
| `redstoneme.*` | Aggregate permission for the main command set declared in `plugin.yml`. | `op` |
| `redstoneme.new` | Create a new trigger. | `op` |
| `redstoneme.destroy` | Destroy a trigger you can manage. | `op` |
| `redstoneme.list` | List triggers and trigger players. | `op` |
| `redstoneme.add` | Add a player to a trigger. | `op` |
| `redstoneme.remove` | Remove a player from a trigger and use `clearplayers`. | `op` |
| `redstoneme.action` | Read or update a trigger action. | `op` |
| `redstoneme.help` | View help pages. | `op` |
| `redstoneme.radius` | Read or update trigger radius. | `op` |
| `redstoneme.state` | Edit trigger state and toggle it. | `op` |
| `redstoneme.public` | Change a trigger between public and private. | `op` |
| `redstoneme.info` | Inspect a trigger. | `op` |
| `redstoneme.tp` | Teleport to a managed trigger. | `op` |
| `redstoneme.rename` | Rename a trigger. | `op` |
| `redstoneme.move` | Move a trigger to another target block. | `op` |
| `redstoneme.reload` | Reload configuration. | `op` |
| `redstoneme.debug` | Toggle debug logging. | `op` |
| `redstoneme.admin` | Manage all triggers and bypass normal ownership checks. | `op` |

### Access Rules

- Most management commands accept the trigger owner or a player with `redstoneme.admin`.
- `info` allows players who already have access to the trigger, plus admins.
- `list` only shows triggers visible to the player unless they have `redstoneme.admin`.
- `setowner` is reserved to `redstoneme.admin`.
