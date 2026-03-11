package me.crylonz.command;

import me.crylonz.RedStoneTrigger;
import org.bukkit.entity.Player;

public class ListCommand extends AbstractSubCommand {

    public ListCommand(CommandContext context) {
        super(context, CommandSpecs.LIST);
    }

    @Override
    public boolean execute(Player player, String[] args) {
        if (!context().hasPermission(player, "redstoneme.list")) {
            return context().deny(player, "redstoneme.list");
        }

        if (args.length == 1) {
            return context().sendTriggerPage(player, 1);
        }

        if (context().isInteger(args[1])) {
            return context().sendTriggerPage(player, Integer.parseInt(args[1]));
        }

        RedStoneTrigger trigger = context().findTrigger(args[1]);
        if (trigger == null) {
            return context().error(player, "The given trigger doesn't exist");
        }

        int page = args.length > 2 && context().isInteger(args[2])
                ? Integer.parseInt(args[2])
                : 1;

        return context().sendPlayersPage(player, trigger, page, "Players list of trigger ");
    }
}
