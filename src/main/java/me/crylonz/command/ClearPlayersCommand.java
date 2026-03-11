package me.crylonz.command;

import me.crylonz.RedStoneTrigger;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public class ClearPlayersCommand extends AbstractSubCommand {

    public ClearPlayersCommand(CommandContext context) {
        super(context, CommandSpecs.CLEAR_PLAYERS);
    }

    @Override
    public boolean execute(Player player, String[] args) {
        if (args.length != 2) {
            return context().usage(player, "/rsm clearplayers <TriggerName>");
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

        int removed = trigger.getPlayers().contains(trigger.getOwner())
                ? Math.max(0, trigger.getPlayers().size() - 1)
                : trigger.getPlayers().size();

        trigger.setPlayers(new ArrayList<String>());
        trigger.addPlayer(UUID.fromString(trigger.getOwner()));

        context().plugin().persistTriggers();
        return context().success(player, removed + " player(s) removed from " + trigger.getTriggerName());
    }
}
