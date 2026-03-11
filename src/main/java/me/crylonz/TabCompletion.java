package me.crylonz;

import me.crylonz.command.CommandSpec;
import me.crylonz.command.CommandSpecs;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static me.crylonz.RedStoneMe.redStoneTriggers;

public class TabCompletion implements TabCompleter {

    private final List<String> suggestions = new ArrayList<String>();

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        suggestions.clear();

        if (!command.getName().equalsIgnoreCase("rsm")) {
            return suggestions;
        }

        if (!(sender instanceof Player)) {
            return suggestions;
        }

        Player player = (Player) sender;

        if (args.length == 1) {
            completeRoot(player);
        } else if (args.length == 2) {
            completeSecondArgument(player, args[0]);
        } else if (args.length == 3) {
            completeThirdArgument(args[0]);
        }

        return suggestions;
    }

    private void completeRoot(Player player) {
        for (CommandSpec spec : CommandSpecs.ALL) {
            if (hasPermission(player, spec.permission())) {
                suggestions.add(spec.name());
            }
        }
    }

    private void completeSecondArgument(Player player, String subCommand) {
        if (isTriggerNameCommand(subCommand)) {
            addAccessibleTriggerNames(player);
        }

        if (subCommand.equals("new")) {
            suggestions.add("<triggerName>");
        }

        if (subCommand.equals("debug")) {
            suggestions.add("OFF");
            suggestions.add("ON");
        }

        if (subCommand.equals("list")) {
            suggestions.add("<page>");
        }
    }

    private void completeThirdArgument(String subCommand) {
        if (subCommand.equals("new") || subCommand.equals("radius")) {
            suggestions.add("<Radius>");
        }

        if (subCommand.equals("add") || subCommand.equals("remove") || subCommand.equals("setowner")) {
            addOnlinePlayers();
        }

        if (subCommand.equals("state") || subCommand.equals("public")) {
            suggestions.add("OFF");
            suggestions.add("ON");
        }

        if (subCommand.equals("action")) {
            for (TriggerAction action : TriggerAction.values()) {
                suggestions.add(action.name());
            }
        }

        if (subCommand.equals("rename")) {
            suggestions.add("<newTriggerName>");
        }
    }

    private boolean hasPermission(Player player, String permission) {
        return player.hasPermission(permission) || player.hasPermission("redstoneme.admin");
    }

    private boolean isTriggerNameCommand(String subCommand) {
        return subCommand.equals("destroy")
                || subCommand.equals("info")
                || subCommand.equals("tp")
                || subCommand.equals("who")
                || subCommand.equals("clearplayers")
                || subCommand.equals("rename")
                || subCommand.equals("move")
                || subCommand.equals("toggle")
                || subCommand.equals("action")
                || subCommand.equals("setowner")
                || subCommand.equals("add")
                || subCommand.equals("remove")
                || subCommand.equals("radius")
                || subCommand.equals("state")
                || subCommand.equals("public")
                || subCommand.equals("list");
    }

    private void addAccessibleTriggerNames(Player player) {
        for (RedStoneTrigger trigger : redStoneTriggers) {
            if (trigger.hasAccess(player)) {
                suggestions.add(trigger.getTriggerName());
            }
        }
    }

    private void addOnlinePlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            suggestions.add(player.getName());
        }
    }
}
