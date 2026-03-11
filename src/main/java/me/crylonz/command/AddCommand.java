package me.crylonz.command;

import me.crylonz.RedStoneTrigger;
import org.bukkit.entity.Player;

public class AddCommand extends AbstractSubCommand {

    public AddCommand(CommandContext context) {
        super(context, CommandSpecs.ADD);
    }

    @Override
    public boolean execute(Player player, String[] args) {
        if (args.length != 3) {
            return context().usage(player, "/rsm add <TriggerName> <PlayerName>");
        }

        if (!context().hasPermission(player, "redstoneme.add")) {
            return context().deny(player, "redstoneme.add");
        }

        RedStoneTrigger trigger = context().findTrigger(args[1]);
        if (trigger == null) {
            return context().error(player, "Trigger " + args[1] + " doesn't exist");
        }

        if (!context().canManage(player, trigger)) {
            return context().ownerOnly(player);
        }

        Player target = context().findOnlinePlayer(args[2]);
        if (target == null) {
            return context().error(player, "Player " + args[2] + " doesn't exist");
        }

        if (!trigger.getPlayers().contains(target.getUniqueId().toString())) {
            trigger.addPlayer(target);
            context().plugin().persistTriggers();
        }

        return context().success(player, "Player " + args[2] + " added");
    }
}
