package me.crylonz.command;

import me.crylonz.RedStoneTrigger;
import org.bukkit.entity.Player;

public class ToggleCommand extends AbstractSubCommand {

    public ToggleCommand(CommandContext context) {
        super(context, CommandSpecs.TOGGLE);
    }

    @Override
    public boolean execute(Player player, String[] args) {
        if (args.length != 2) {
            return context().usage(player, "/rsm toggle <TriggerName>");
        }

        if (!context().hasPermission(player, "redstoneme.state")) {
            return context().deny(player, "redstoneme.state");
        }

        RedStoneTrigger trigger = context().findTrigger(args[1]);
        if (trigger == null) {
            return context().error(player, "The trigger " + args[1] + " doesn't exist");
        }

        if (!context().canManage(player, trigger)) {
            return context().ownerOnly(player);
        }

        trigger.setEnable(!trigger.isEnable());
        context().plugin().persistTriggers();
        context().plugin().debug("Trigger toggled: " + trigger.getTriggerName());

        return context().success(
                player,
                "Trigger " + trigger.getTriggerName() + " is now " + (trigger.isEnable() ? "ON" : "OFF")
        );
    }
}
