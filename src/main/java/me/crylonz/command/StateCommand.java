package me.crylonz.command;

import me.crylonz.RedStoneTrigger;
import org.bukkit.entity.Player;

public class StateCommand extends AbstractSubCommand {

    public StateCommand(CommandContext context) {
        super(context, CommandSpecs.STATE);
    }

    @Override
    public boolean execute(Player player, String[] args) {
        if (args.length != 3) {
            return context().usage(player, "/rsm state <TriggerName> <OFF|ON>");
        }

        if (!context().hasPermission(player, "redstoneme.state")) {
            return context().deny(player, "redstoneme.state");
        }

        if (!context().isOnOff(args[2])) {
            return context().error(player, "State must be ON or OFF");
        }

        RedStoneTrigger trigger = context().findTrigger(args[1]);
        if (trigger == null) {
            return context().error(player, "The trigger " + args[1] + " doesn't exist");
        }

        if (!context().canManage(player, trigger)) {
            return context().ownerOnly(player);
        }

        trigger.setEnable(args[2].equalsIgnoreCase("on"));
        context().plugin().persistTriggers();
        context().plugin().debug("State updated for " + trigger.getTriggerName());

        return context().success(player, "Trigger " + args[1] + " is now " + args[2].toUpperCase());
    }
}
