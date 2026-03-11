package me.crylonz.command;

import me.crylonz.RedStoneTrigger;
import org.bukkit.entity.Player;

public class InfoCommand extends AbstractSubCommand {

    public InfoCommand(CommandContext context) {
        super(context, CommandSpecs.INFO);
    }

    @Override
    public boolean execute(Player player, String[] args) {
        if (args.length != 2) {
            return context().usage(player, "/rsm info <TriggerName>");
        }

        if (!context().hasPermission(player, "redstoneme.info")) {
            return context().deny(player, "redstoneme.info");
        }

        RedStoneTrigger trigger = context().findTrigger(args[1]);
        if (trigger == null) {
            return context().error(player, "The trigger " + args[1] + " doesn't exist");
        }

        if (!trigger.hasAccess(player) && !player.hasPermission("redstoneme.admin")) {
            return context().error(player, "You don't have access to this trigger");
        }

        return context().sendInfo(player, trigger);
    }
}
