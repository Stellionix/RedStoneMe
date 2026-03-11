package me.crylonz.command;

import java.util.Arrays;
import java.util.List;

public final class CommandSpecs {

    public static final CommandSpec HELP = spec("help", "redstoneme.help", "/rsm help", "Show help pages");
    public static final CommandSpec LIST = spec("list", "redstoneme.list", "/rsm list [page] | /rsm list <Trigger> [page]", "Show triggers or players");
    public static final CommandSpec DESTROY = spec("destroy", "redstoneme.destroy", "/rsm destroy <TriggerName>", "Remove the given trigger");
    public static final CommandSpec RADIUS = spec("radius", "redstoneme.radius", "/rsm radius <TriggerName> [Radius]", "Read or update radius");
    public static final CommandSpec NEW = spec("new", "redstoneme.new", "/rsm new <TriggerName> <Radius>", "Generate a new trigger");
    public static final CommandSpec STATE = spec("state", "redstoneme.state", "/rsm state <TriggerName> <OFF|ON>", "Change trigger state");
    public static final CommandSpec PUBLIC = spec("public", "redstoneme.public", "/rsm public <TriggerName> <OFF|ON>", "Change trigger access");
    public static final CommandSpec ADD = spec("add", "redstoneme.add", "/rsm add <TriggerName> <PlayerName>", "Add a player to the trigger");
    public static final CommandSpec REMOVE = spec("remove", "redstoneme.remove", "/rsm remove <TriggerName> <PlayerName>", "Remove a player from the trigger");
    public static final CommandSpec INFO = spec("info", "redstoneme.info", "/rsm info <TriggerName>", "Show trigger details");
    public static final CommandSpec TP = spec("tp", "redstoneme.tp", "/rsm tp <TriggerName>", "Teleport to the trigger");
    public static final CommandSpec RENAME = spec("rename", "redstoneme.rename", "/rsm rename <OldName> <NewName>", "Rename a trigger");
    public static final CommandSpec MOVE = spec("move", "redstoneme.move", "/rsm move <TriggerName>", "Move a trigger to the targeted block");
    public static final CommandSpec TOGGLE = spec("toggle", "redstoneme.state", "/rsm toggle <TriggerName>", "Toggle trigger ON/OFF");
    public static final CommandSpec RELOAD = spec("reload", "redstoneme.reload", "/rsm reload", "Reload plugin configuration");
    public static final CommandSpec DEBUG = spec("debug", "redstoneme.debug", "/rsm debug <OFF|ON>", "Enable or disable debug logging");
    public static final CommandSpec WHO = spec("who", "redstoneme.list", "/rsm who <TriggerName> [Page]", "List players allowed on a trigger");
    public static final CommandSpec CLEAR_PLAYERS = spec("clearplayers", "redstoneme.remove", "/rsm clearplayers <TriggerName>", "Remove all non-owner players");
    public static final CommandSpec SET_OWNER = spec("setowner", "redstoneme.admin", "/rsm setowner <TriggerName> <PlayerName>", "Transfer ownership");

    public static final List<CommandSpec> ALL = Arrays.asList(
            HELP, NEW, DESTROY, INFO, TP, RENAME, MOVE, TOGGLE,
            ADD, REMOVE, CLEAR_PLAYERS, WHO, RADIUS, STATE, PUBLIC,
            SET_OWNER, LIST, RELOAD, DEBUG
    );

    private CommandSpecs() {
    }

    private static CommandSpec spec(String name, String permission, String usage, String summary) {
        return new CommandSpec(name, permission, usage, summary);
    }
}
