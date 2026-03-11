package me.crylonz.command;

import me.crylonz.RedStoneTrigger;
import me.crylonz.TriggerAction;
import org.bukkit.entity.Player;

public final class ActionCommand extends AbstractSubCommand {

    public ActionCommand(CommandContext context) {
        super(context, CommandSpecs.ACTION);
    }

    @Override
    public boolean execute(Player player, String[] args) {
        if (!context().hasPermission(player, spec().permission())) {
            return context().deny(player, spec().permission());
        }

        if (args.length < 2 || args.length > 3) {
            return context().usage(player, spec().usage());
        }

        RedStoneTrigger trigger = context().findTrigger(args[1]);
        if (trigger == null) {
            return context().error(player, "Trigger not found");
        }

        if (!context().canManage(player, trigger)) {
            return context().ownerOnly(player);
        }

        if (args.length == 2) {
            return context().success(player, "Trigger " + trigger.getTriggerName()
                    + " action is " + trigger.getAction().name());
        }

        TriggerAction action = parseAction(args[2]);
        if (action == null) {
            return context().error(player, "Unknown action. Available: REDSTONE_TORCH, REDSTONE_BLOCK");
        }

        trigger.setAction(action);
        context().plugin().persistTriggers();
        context().plugin().debug("Trigger " + trigger.getTriggerName() + " action changed to " + action.name());
        return context().success(player, "Trigger " + trigger.getTriggerName() + " action set to " + action.name());
    }

    private TriggerAction parseAction(String value) {
        for (TriggerAction action : TriggerAction.values()) {
            if (action.name().equalsIgnoreCase(value)) {
                return action;
            }
        }
        return null;
    }
}
