package me.crylonz.command;

import me.crylonz.RedStoneTrigger;
import org.bukkit.entity.Player;

public class RadiusCommand extends AbstractSubCommand {

    public RadiusCommand(CommandContext context) {
        super(context, CommandSpecs.RADIUS);
    }

    @Override
    public boolean execute(Player player, String[] args) {
        if (!context().hasPermission(player, "redstoneme.radius")) {
            return context().deny(player, "redstoneme.radius");
        }

        RedStoneTrigger trigger = args.length > 1
                ? context().findTrigger(args[1])
                : null;

        if (trigger == null) {
            return args.length == 2
                    ? context().error(player, "The given trigger doesn't exist")
                    : context().usage(player, "/rsm radius <TriggerName> [Radius]");
        }

        if (!context().canManage(player, trigger)) {
            return context().ownerOnly(player);
        }

        if (args.length == 2) {
            return context().success(player, "Radius for " + trigger.getTriggerName() + " : " + trigger.getRadius());
        }

        if (args.length != 3) {
            return context().usage(player, "/rsm radius <TriggerName> [Radius]");
        }

        trigger.setRadius(Integer.parseInt(args[2]));
        context().plugin().persistTriggers();
        context().plugin().debug("Radius updated for " + trigger.getTriggerName());

        return context().success(player, "Trigger radius of " + trigger.getTriggerName() + " is set to " + args[2]);
    }
}
