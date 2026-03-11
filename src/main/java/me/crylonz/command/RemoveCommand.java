package me.crylonz.command;

import me.crylonz.RedStoneTrigger;
import org.bukkit.entity.Player;

public class RemoveCommand extends AbstractSubCommand {

    public RemoveCommand(CommandContext context) {
        super(context, CommandSpecs.REMOVE);
    }

    @Override
    public boolean execute(Player player, String[] args) {
        if (args.length != 3) {
            return context().usage(player, "/rsm remove <TriggerName> <PlayerName>");
        }

        if (!context().hasPermission(player, "redstoneme.remove")) {
            return context().deny(player, "redstoneme.remove");
        }

        RedStoneTrigger trigger = context().findTrigger(args[1]);
        if (trigger == null) {
            return context().error(player, "The trigger " + args[1] + " doesn't exist");
        }

        if (!context().canManage(player, trigger)) {
            return context().ownerOnly(player);
        }

        Player target = context().findOnlinePlayer(args[2]);
        if (target == null) {
            return context().error(player, "The player " + args[2] + " doesn't exist");
        }

        if (!trigger.removePlayer(target)) {
            return context().error(player, "The player is not in the list");
        }

        context().plugin().persistTriggers();
        return context().success(player, "The player " + args[2] + " is removed");
    }
}
