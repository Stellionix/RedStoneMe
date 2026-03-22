## Configuration

RedStoneMe uses `plugins/RedStoneMe/config.yml` for runtime settings and `plugins/RedStoneMe/redstoneme.db` for trigger persistence by default.

## Storage

```yaml
storage:
  sqlite-file: redstoneme.db
```

- `storage.sqlite-file`: SQLite filename created inside the plugin data folder.

## Trigger Selection

```yaml
selection:
  respect-break-protection: true
  allowed-trigger-blocks:
    - STONE
    - COBBLESTONE
    - STONE_BRICKS
```

- `selection.respect-break-protection`: rejects `/rsm new` and `/rsm move` on blocks that another plugin or the server would block from being broken.
- `selection.allowed-trigger-blocks`: whitelist of Bukkit material names. When empty, all materials are allowed except RedStoneMe's internal deny-list.

## Trigger Protection

```yaml
protection:
  prevent-non-owner-break: true
  allow-owner-break: true
  allow-admin-break: true
  breaking-trigger-deletes-it: true
```

- `protection.prevent-non-owner-break`: protects trigger blocks from non-owners.
- `protection.allow-owner-break`: lets owners break their own trigger blocks.
- `protection.allow-admin-break`: lets `redstoneme.admin` bypass trigger protection.
- `protection.breaking-trigger-deletes-it`: deletes the trigger record when an authorized break happens.

## Player Messages

```yaml
messages:
  notify-protected-break: true
  notify-trigger-broken: true
  protected-break: "&6[RedStoneMe] &cOnly the trigger owner can break this trigger."
  trigger-broken: "&6[RedStoneMe] &aTrigger {trigger} has been removed."
```

- `messages.notify-protected-break`: shows a chat message when a protected trigger block cannot be broken.
- `messages.notify-trigger-broken`: shows a chat message when a trigger is removed through block breaking.
- `messages.protected-break`: message template for denied breaks.
- `messages.trigger-broken`: message template for successful trigger removal. `{trigger}` is replaced by the trigger name.

## Debug Logging

```yaml
debug:
  enabled: false
```

- `debug.enabled`: prints command and protection diagnostics to the server log.
