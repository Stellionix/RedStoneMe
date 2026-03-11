package me.crylonz.command;

import me.crylonz.RedStoneTrigger;
import org.bukkit.entity.Player;

public class TpCommand extends AbstractSubCommand {

    public TpCommand(CommandContext context) {
        super(context, CommandSpecs.TP);
    }

    @Override
    public boolean execute(Player player, String[] args) {
        if (args.length != 2) {
            return context().usage(player, "/rsm tp <TriggerName>");
        }

        if (!context().hasPermission(player, "redstoneme.tp")) {
            return context().deny(player, "redstoneme.tp");
        }

        RedStoneTrigger trigger = context().findTrigger(args[1]);
        if (trigger == null) {
            return context().error(player, "The trigger " + args[1] + " doesn't exist");
        }

        if (!context().canManage(player, trigger)) {
            return context().ownerOnly(player);
        }

        player.teleport(trigger.getLoc().clone().add(0.5, 1.0, 0.5));
        return context().success(player, "Teleported to " + trigger.getTriggerName());
    }
}
