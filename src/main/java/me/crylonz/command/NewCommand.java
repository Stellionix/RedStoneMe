package me.crylonz.command;

import me.crylonz.RedStoneMe;
import me.crylonz.RedStoneTrigger;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class NewCommand extends AbstractSubCommand {

    public NewCommand(CommandContext context) {
        super(context, CommandSpecs.NEW);
    }

    @Override
    public boolean execute(Player player, String[] args) {
        if (args.length != 3) {
            return context().usage(player, "/rsm new <TriggerName> <Radius>");
        }

        if (!context().hasPermission(player, "redstoneme.new")) {
            return context().deny(player, "redstoneme.new");
        }

        if (context().findTrigger(args[1]) != null) {
            return context().error(player, "This trigger name is already used");
        }

        Block block = player.getTargetBlockExact(5);
        if (block == null) {
            return context().error(player, "You need to target with your cursor the block you want to trigger");
        }

        RedStoneTrigger trigger = new RedStoneTrigger(
                args[1],
                Integer.parseInt(args[2]),
                block.getLocation(),
                block.getType(),
                player
        );
        trigger.addPlayer(player);
        RedStoneMe.redStoneTriggers.add(trigger);

        context().plugin().persistTriggers();
        context().plugin().debug("Trigger created: " + trigger.getTriggerName() + " by " + player.getName());

        return context().success(player, "Trigger " + args[1] + " is enable");
    }
}
