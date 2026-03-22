<h1 align="center">
    <img src="redstoneme-logo.png" alt="RedStoneMe" width="800" /><br>
</h1>

<h2 align="center">
    <img src="http://cf.way2muchnoise.eu/full_322434_downloads.svg" alt="download"/>
    <a href="https://github.com/apavarino/redstoneme/actions/workflows/ci.yml">
        <img src="https://github.com/apavarino/redstoneme/actions/workflows/ci.yml/badge.svg" alt="CI"/>
    </a>
    <a href="https://stellionix.github.io/RedStoneMe/">
        <img src="https://img.shields.io/badge/docs-online-blue" alt="Docs"/>
    </a>
    <img src="https://img.shields.io/github/license/apavarino/redstoneme" alt="licence"/>
    <img src="https://img.shields.io/github/last-commit/apavarino/redstoneme" alt="commit"/>
</h2>

## Description
RedStoneMe lets you activate redstone-related blocks when players get close to a trigger area.

It is designed for Bukkit-compatible servers and works with Bukkit, Spigot and Paper.

## Features
- Create proximity-based triggers with a configurable radius
- Persist triggers in a dedicated SQLite database
- Protect trigger blocks against unauthorized breaking
- Manage private or public triggers
- Allow selected players to use a private trigger
- Teleport, rename, move, inspect and administrate triggers in game
- Choose the action executed by a trigger

## Available Actions
Current trigger action types:

- `REDSTONE_TORCH`
- `REDSTONE_BLOCK`

## Configuration
The plugin stores trigger data in SQLite and keeps general settings in `config.yml`.

Important configuration areas:

- `storage.sqlite-file`: SQLite database filename inside the plugin data folder
- `protection.prevent-non-owner-break`: prevents non-owners from breaking trigger blocks
- `protection.allow-owner-break`: allows the owner to break their own trigger block
- `protection.allow-admin-break`: allows `redstoneme.admin` to bypass protection
- `protection.breaking-trigger-deletes-it`: decides whether breaking a trigger removes it
- `messages.*`: configurable in-game protection and deletion messages
- `debug.enabled`: enables console debug logs

## Documentation
The official documentation is available at [stellionix.github.io/RedStoneMe](https://stellionix.github.io/RedStoneMe/).

Useful pages:

- [Installation](https://stellionix.github.io/RedStoneMe/latest/installation/)
- [Configuration](https://stellionix.github.io/RedStoneMe/latest/configuration/)
- [Commands and Permissions](https://stellionix.github.io/RedStoneMe/latest/commands-and-perms/)
- [Troubleshooting](https://stellionix.github.io/RedStoneMe/latest/troubleshooting/)

## Commands
Main aliases:

- `/redstoneme`
- `/rsm`

Available subcommands:

| Command | Description |
| --- | --- |
| `/rsm help [page]` | Show paginated help |
| `/rsm new <TriggerName> <Radius>` | Create a trigger on the targeted block |
| `/rsm destroy <TriggerName>` | Remove a trigger |
| `/rsm info <TriggerName>` | Show trigger details |
| `/rsm tp <TriggerName>` | Teleport to a trigger |
| `/rsm rename <OldName> <NewName>` | Rename a trigger |
| `/rsm move <TriggerName>` | Move a trigger to the targeted block |
| `/rsm toggle <TriggerName>` | Toggle a trigger ON/OFF |
| `/rsm action <TriggerName> [ActionType]` | Show or change the trigger action |
| `/rsm add <TriggerName> <PlayerName>` | Allow a player on a trigger |
| `/rsm remove <TriggerName> <PlayerName>` | Remove a player from a trigger |
| `/rsm clearplayers <TriggerName>` | Remove all non-owner players |
| `/rsm who <TriggerName> [page]` | Show allowed players |
| `/rsm radius <TriggerName> [Radius]` | Show or change radius |
| `/rsm state <TriggerName> <OFF\|ON>` | Force trigger state |
| `/rsm public <TriggerName> <OFF\|ON>` | Change trigger visibility |
| `/rsm setowner <TriggerName> <PlayerName>` | Transfer ownership |
| `/rsm list [page]` | List visible triggers |
| `/rsm list <TriggerName> [page]` | List players on a trigger |
| `/rsm reload` | Reload configuration |
| `/rsm debug <OFF\|ON>` | Enable or disable debug mode |

## Permissions
| Permission | Description | Default |
| --- | --- | --- |
| `redstoneme.*` | Gives access to all RedStoneMe commands | `op` |
| `redstoneme.new` | Create a new trigger | `op` |
| `redstoneme.destroy` | Destroy an existing trigger | `op` |
| `redstoneme.list` | List triggers and players | `op` |
| `redstoneme.add` | Add a player to a trigger | `op` |
| `redstoneme.remove` | Remove a player from a trigger | `op` |
| `redstoneme.action` | Read or edit a trigger action | `op` |
| `redstoneme.help` | View help pages | `op` |
| `redstoneme.radius` | Read or edit trigger radius | `op` |
| `redstoneme.state` | Edit trigger state | `op` |
| `redstoneme.public` | Edit trigger public/private access | `op` |
| `redstoneme.info` | Inspect a trigger | `op` |
| `redstoneme.tp` | Teleport to a trigger | `op` |
| `redstoneme.rename` | Rename a trigger | `op` |
| `redstoneme.move` | Move a trigger | `op` |
| `redstoneme.reload` | Reload configuration | `op` |
| `redstoneme.debug` | Toggle debug logging | `op` |
| `redstoneme.admin` | Access and manage all triggers | `op` |

## Storage
Triggers are stored in:

- `plugins/RedStoneMe/<sqlite-file>`

By default this is:

- `plugins/RedStoneMe/redstoneme.db`

## Contributing
Developer-oriented setup, build and contribution notes are available in [contribute.md](/C:/Users/arthu/Documents/GitHub/plugins/bukkit/redstoneme/contribute.md).

## Project Pages
- https://dev.bukkit.org/projects/redstoneme
- https://www.curseforge.com/minecraft/bukkit-plugins/redstoneme
