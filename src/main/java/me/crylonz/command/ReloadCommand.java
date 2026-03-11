package me.crylonz.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReloadCommand extends AbstractSubCommand {

    public ReloadCommand(CommandContext context) {
        super(context, CommandSpecs.RELOAD);
    }

    @Override
    public boolean execute(Player player, String[] args) {
        if (!context().hasPermission(player, "redstoneme.reload")) {
            return context().deny(player, "redstoneme.reload");
        }

        context().plugin().reloadConfig();
        return context().success(player, "Configuration reloaded");
    }

    @Override
    public boolean executeConsole(CommandSender sender, String[] args) {
        context().plugin().reloadConfig();
        sender.sendMessage(context().prefix(true) + "Configuration reloaded");
        return true;
    }
}
