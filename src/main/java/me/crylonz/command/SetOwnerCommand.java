package me.crylonz.command;

import me.crylonz.RedStoneTrigger;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class SetOwnerCommand extends AbstractSubCommand {

    public SetOwnerCommand(CommandContext context) {
        super(context, CommandSpecs.SET_OWNER);
    }

    @Override
    public boolean execute(Player player, String[] args) {
        if (args.length != 3) {
            return context().usage(player, "/rsm setowner <TriggerName> <PlayerName>");
        }

        if (!player.hasPermission("redstoneme.admin")) {
            return context().deny(player, "redstoneme.admin");
        }

        RedStoneTrigger trigger = context().findTrigger(args[1]);
        if (trigger == null) {
            return context().error(player, "The trigger " + args[1] + " doesn't exist");
        }

        OfflinePlayer owner = Bukkit.getOfflinePlayer(args[2]);
        trigger.setOwner(owner.getUniqueId().toString());

        if (!trigger.getPlayers().contains(owner.getUniqueId().toString())) {
            trigger.addPlayer(owner.getUniqueId());
        }

        context().plugin().persistTriggers();
        return context().success(player, "Owner of " + trigger.getTriggerName() + " is now " + args[2]);
    }
}
