package me.crylonz.command;

import org.bukkit.entity.Player;

public class DebugCommand extends AbstractSubCommand {

    public DebugCommand(CommandContext context) {
        super(context, CommandSpecs.DEBUG);
    }

    @Override
    public boolean execute(Player player, String[] args) {
        if (args.length != 2) {
            return context().usage(player, "/rsm debug <OFF|ON>");
        }

        if (!context().hasPermission(player, "redstoneme.debug")) {
            return context().deny(player, "redstoneme.debug");
        }

        if (!context().isOnOff(args[1])) {
            return context().error(player, "Debug must be ON or OFF");
        }

        boolean enabled = args[1].equalsIgnoreCase("on");
        context().plugin().getConfig().set("debug.enabled", enabled);
        context().plugin().saveConfig();

        return context().success(player, "Debug is now " + (enabled ? "ON" : "OFF"));
    }
}
