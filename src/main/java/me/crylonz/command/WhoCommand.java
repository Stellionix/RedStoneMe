package me.crylonz.command;

import me.crylonz.RedStoneTrigger;
import org.bukkit.entity.Player;

public class WhoCommand extends AbstractSubCommand {

    public WhoCommand(CommandContext context) {
        super(context, CommandSpecs.WHO);
    }

    @Override
    public boolean execute(Player player, String[] args) {
        if (args.length < 2 || args.length > 3) {
            return context().usage(player, "/rsm who <TriggerName> [Page]");
        }

        if (!context().hasPermission(player, "redstoneme.list")) {
            return context().deny(player, "redstoneme.list");
        }

        RedStoneTrigger trigger = context().findTrigger(args[1]);
        if (trigger == null) {
            return context().error(player, "The trigger " + args[1] + " doesn't exist");
        }

        int page = args.length == 3 && context().isInteger(args[2])
                ? Integer.parseInt(args[2])
                : 1;

        return context().sendPlayersPage(player, trigger, page, "Players allowed on ");
    }
}
