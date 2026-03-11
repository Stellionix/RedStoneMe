package me.crylonz.command;

import me.crylonz.RedStoneTrigger;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class MoveCommand extends AbstractSubCommand {

    public MoveCommand(CommandContext context) {
        super(context, CommandSpecs.MOVE);
    }

    @Override
    public boolean execute(Player player, String[] args) {
        if (args.length != 2) {
            return context().usage(player, "/rsm move <TriggerName>");
        }

        if (!context().hasPermission(player, "redstoneme.move")) {
            return context().deny(player, "redstoneme.move");
        }

        RedStoneTrigger trigger = context().findTrigger(args[1]);
        if (trigger == null) {
            return context().error(player, "The trigger " + args[1] + " doesn't exist");
        }

        if (!context().canManage(player, trigger)) {
            return context().ownerOnly(player);
        }

        Block block = player.getTargetBlockExact(5);
        if (!context().validateTargetBlock(player, block)) {
            return true;
        }

        trigger.trigger(false);
        trigger.setLoc(block.getLocation());
        trigger.setWorldName(block.getWorld().getName());
        trigger.setMaterial(block.getType());

        context().plugin().persistTriggers();
        context().plugin().debug("Trigger moved: " + trigger.getTriggerName());

        return context().success(player, "Trigger " + trigger.getTriggerName() + " moved");
    }
}
