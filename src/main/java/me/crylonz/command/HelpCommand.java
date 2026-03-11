package me.crylonz.command;

import org.bukkit.entity.Player;

public class HelpCommand extends AbstractSubCommand {

    public HelpCommand(CommandContext context) {
        super(context, CommandSpecs.HELP);
    }

    @Override
    public boolean execute(Player player, String[] args) {
        if (!context().hasPermission(player, "redstoneme.help")) {
            return context().deny(player, "redstoneme.help");
        }

        return context().sendHelpPage(player, 1);
    }
}
