package me.crylonz.command;

import me.crylonz.RedStoneTrigger;
import org.bukkit.entity.Player;

public class RenameCommand extends AbstractSubCommand {

    public RenameCommand(CommandContext context) {
        super(context, CommandSpecs.RENAME);
    }

    @Override
    public boolean execute(Player player, String[] args) {
        if (args.length != 3) {
            return context().usage(player, "/rsm rename <OldName> <NewName>");
        }

        if (!context().hasPermission(player, "redstoneme.rename")) {
            return context().deny(player, "redstoneme.rename");
        }

        RedStoneTrigger trigger = context().findTrigger(args[1]);
        if (trigger == null) {
            return context().error(player, "The trigger " + args[1] + " doesn't exist");
        }

        if (!context().canManage(player, trigger)) {
            return context().ownerOnly(player);
        }

        if (context().findTrigger(args[2]) != null) {
            return context().error(player, "This trigger name is already used");
        }

        trigger.setTriggerName(args[2]);
        context().plugin().persistTriggers();
        context().plugin().debug("Trigger renamed from " + args[1] + " to " + args[2]);

        return context().success(player, "Trigger renamed to " + args[2]);
    }
}
