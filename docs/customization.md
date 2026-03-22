## Customization

RedStoneMe keeps customization intentionally small and operational.

### Allowed Trigger Materials

Use `selection.allowed-trigger-blocks` to restrict which blocks may become trigger sources.

Example:

```yaml
selection:
  allowed-trigger-blocks:
    - STONE_BRICKS
    - IRON_BLOCK
    - REDSTONE_LAMP
```

Operational effect:

- Players can only create or move triggers onto those materials.
- An empty list means "allow everything except internally blocked materials".

### Protected Break Messaging

Use the `messages` section to adapt server-facing messages:

```yaml
messages:
  protected-break: "&6[RedStoneMe] &cOnly the trigger owner can break this trigger."
  trigger-broken: "&6[RedStoneMe] &aTrigger {trigger} has been removed."
```

Notes:

- Color codes use Bukkit `&` formatting.
- `{trigger}` is replaced with the actual trigger name.

### Public vs Private Triggers

Trigger behavior is also part of operational customization:

- Private triggers only react to the owner and explicitly allowed players.
- Public triggers react to any player entering the radius.

Use `/rsm public <TriggerName> ON` to expose a trigger more broadly without editing config.
