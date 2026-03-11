package me.crylonz.command;

import me.crylonz.RedStoneTrigger;
import org.bukkit.entity.Player;

public class PublicCommand extends AbstractSubCommand {

    public PublicCommand(CommandContext context) {
        super(context, CommandSpecs.PUBLIC);
    }

    @Override
    public boolean execute(Player player, String[] args) {
        if (args.length != 3) {
            return context().usage(player, "/rsm public <TriggerName> <OFF|ON>");
        }

        if (!context().hasPermission(player, "redstoneme.public")) {
            return context().deny(player, "redstoneme.public");
        }

        if (!context().isOnOff(args[2])) {
            return context().error(player, "Public must be ON or OFF");
        }

        RedStoneTrigger trigger = context().findTrigger(args[1]);
        if (trigger == null) {
            return context().error(player, "The trigger " + args[1] + " doesn't exist");
        }

        if (!context().canManage(player, trigger)) {
            return context().ownerOnly(player);
        }

        trigger.setPublic(args[2].equalsIgnoreCase("on"));
        context().plugin().persistTriggers();
        context().plugin().debug("Access updated for " + trigger.getTriggerName());

        return context().success(
                player,
                "Trigger " + args[1] + " is now " + (trigger.isPublic() ? "public" : "private")
        );
    }
}
