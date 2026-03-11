package me.crylonz.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public interface SubCommand {
    CommandSpec spec();

    boolean execute(Player player, String[] args);

    default boolean executeConsole(CommandSender sender, String[] args) {
        sender.sendMessage(context().prefix(false) + "Only players can use this command");
        return true;
    }

    CommandContext context();
}
