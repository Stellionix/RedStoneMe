## Troubleshooting

### A trigger does not activate

Check the following:

- the trigger is `ON`
- the player is inside the configured radius
- the trigger is public, or the player is in the access list
- the trigger is in the same world as the player
- the action type is valid for what you want to simulate

Use `/rsm info <TriggerName>` to inspect the effective state.

### `/rsm new` says the block cannot be used

Common causes:

- the player is not targeting a block
- the targeted material is internally forbidden, such as bedrock or barrier
- `selection.allowed-trigger-blocks` does not include the targeted material
- another protection plugin or the server would deny breaking that block and `selection.respect-break-protection` is enabled

### Trigger block breaking does nothing

Check `protection.breaking-trigger-deletes-it`.

- If `true`, an authorized break removes the trigger from SQLite.
- If `false`, the block break is allowed but the trigger record is kept.

### A player cannot manage a trigger

Most write operations require:

- being the trigger owner, or
- having `redstoneme.admin`

Read-only visibility is broader for some commands such as `info` and `list`, but management stays owner-or-admin scoped.

### Data did not disappear after restart

That is expected. RedStoneMe persists trigger records in SQLite and reloads them on startup.

Default location:

```text
plugins/RedStoneMe/redstoneme.db
```

### Need more diagnostics

Enable debug logging:

```text
/rsm debug ON
```

You can also set:

```yaml
debug:
  enabled: true
```
