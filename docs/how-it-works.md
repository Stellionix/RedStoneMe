## How It Works

RedStoneMe stores a trigger as a named record with:

- owner UUID
- world and block coordinates
- radius
- enabled or disabled state
- public or private access mode
- action type
- allowed player UUID list

### Activation Model

- The scheduler checks triggers repeatedly on the main server thread.
- Public triggers activate when any online player is inside the trigger radius.
- Private triggers activate only when the owner or an allowed player is inside the trigger radius.
- If no qualifying player is inside the area, the trigger returns to its inactive state.

### Trigger Block Selection

When creating or moving a trigger, RedStoneMe validates the targeted block:

- no air, bedrock, barrier, portal, or command-block style materials
- optional whitelist from `selection.allowed-trigger-blocks`
- optional break-protection compatibility check through a synthetic `BlockBreakEvent`

This prevents players from binding triggers to invalid or protected blocks.

### Actions

Current built-in actions:

- `REDSTONE_TORCH`
- `REDSTONE_BLOCK`

These actions replace the tracked block material while the trigger is active and restore the original material when it deactivates.

### Persistence

- Trigger definitions are stored in SQLite.
- Player access lists are stored in a dedicated `trigger_players` table.
- RedStoneMe saves all triggers on shutdown and after destructive protection events such as authorized trigger block breaking.
