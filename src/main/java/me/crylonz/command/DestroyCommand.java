package me.crylonz.command;

import me.crylonz.RedStoneMe;
import me.crylonz.RedStoneTrigger;
import org.bukkit.entity.Player;

public class DestroyCommand extends AbstractSubCommand {

    public DestroyCommand(CommandContext context) {
        super(context, CommandSpecs.DESTROY);
    }

    @Override
    public boolean execute(Player player, String[] args) {
        if (args.length != 2) {
            return context().usage(player, "/rsm destroy <TriggerName>");
        }

        if (!context().hasPermission(player, "redstoneme.destroy")) {
            return context().deny(player, "redstoneme.destroy");
        }

        RedStoneTrigger trigger = context().findTrigger(args[1]);
        if (trigger == null) {
            return context().error(player, "Trigger " + args[1] + " doesn't exist");
        }

        if (!context().canManage(player, trigger)) {
            return context().ownerOnly(player);
        }

        trigger.trigger(false);
        RedStoneMe.redStoneTriggers.remove(trigger);
        context().plugin().persistTriggers();
        context().plugin().debug("Trigger destroyed: " + trigger.getTriggerName() + " by " + player.getName());

        return context().success(player, "Trigger " + args[1] + " is destroyed");
    }
}
