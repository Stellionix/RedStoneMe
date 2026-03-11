package me.crylonz;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.UUID;

import static me.crylonz.RedStoneMe.isOwnerOfTrigger;
import static me.crylonz.RedStoneMe.redStoneTriggers;

public class RSMCommandExecutor implements CommandExecutor {

    private final RedStoneMe plugin;

    public RSMCommandExecutor(RedStoneMe p) {
        this.plugin = p;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        Player player;
        if ((sender instanceof Player)) {
            player = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("redstoneme") || cmd.getName().equalsIgnoreCase("rsm")) {

                if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("list")) {
                        if (player.hasPermission("redstoneme.list")) {
                            player.sendMessage(ChatColor.GOLD + "[RedStoneMe] " + ChatColor.GREEN + "Triggers list :");
                            for (RedStoneTrigger rt : redStoneTriggers) {
                                player.sendMessage(ChatColor.WHITE + "Name: " + ChatColor.LIGHT_PURPLE
                                        + rt.getTriggerName() + ChatColor.WHITE + " Owner: " + ChatColor.LIGHT_PURPLE +
                                        Bukkit.getOfflinePlayer(UUID.fromString(rt.getOwner())).getName());
                            }
                        } else
                            player.sendMessage(ChatColor.GOLD + "[RedStoneMe] " + ChatColor.RED + "You need redstoneme.list permission to do that ");


                    } else if (args[0].equalsIgnoreCase("help")) {

                        if (player.hasPermission("redstoneme.help")) {
                            player.sendMessage(ChatColor.GOLD + "[RedStoneMe] " + ChatColor.GREEN + "Commands list :");
                            player.sendMessage(ChatColor.LIGHT_PURPLE + "/rsm new <TriggerName> <Radius> " + ChatColor.WHITE + " Generate a new trigger");
                            player.sendMessage(ChatColor.LIGHT_PURPLE + "/rsm destroy <TriggerName> " + ChatColor.WHITE + " Remove the given trigger");
                            player.sendMessage(ChatColor.LIGHT_PURPLE + "/rsm add <TriggerName> <PlayerName>" + ChatColor.WHITE + " Add a player to the given trigger");
                            player.sendMessage(ChatColor.LIGHT_PURPLE + "/rsm remove <TriggerName> <PlayerName>" + ChatColor.WHITE + " Remove a player to the given trigger");
                            player.sendMessage(ChatColor.LIGHT_PURPLE + "/rsm radius <TriggerName> [Radius]" + ChatColor.WHITE + " Edit/See the radius effect to the given trigger");
                            player.sendMessage(ChatColor.LIGHT_PURPLE + "/rsm state <TriggerName> <OFF|ON>" + ChatColor.WHITE + " Edit the state of the current trigger");
                            player.sendMessage(ChatColor.LIGHT_PURPLE + "/rsm public <TriggerName> <OFF|ON>" + ChatColor.WHITE + " Edit the access to the trigger");
                            player.sendMessage(ChatColor.LIGHT_PURPLE + "/rsm list [TriggerName] " + ChatColor.WHITE + "display list of triggers or list of players for a trigger");
                        } else
                            player.sendMessage(ChatColor.GOLD + "[RedStoneMe] " + ChatColor.RED + "You need redstoneme.help permission to do that ");
                    } else
                        player.sendMessage(ChatColor.GOLD + "[RedStoneMe] " + ChatColor.RED + "Unknow command use" + ChatColor.WHITE + " /rsm help");
                } else if (args.length == 2) {
                    if (args[0].equalsIgnoreCase("destroy")) {
                        if (player.hasPermission("redstoneme.destroy")) {
                            boolean isDestroyed = true;
                            for (RedStoneTrigger rt : redStoneTriggers) {
                                if (rt.getTriggerName().equalsIgnoreCase(args[1])) {
                                    if(isOwnerOfTrigger(player,rt) || player.hasPermission("redstoneme.admin"))
                                    {
                                        rt.trigger(false);
                                        isDestroyed = redStoneTriggers.remove(rt);
                                        plugin.persistTriggers();
                                    }
                                    else {
                                        player.sendMessage(ChatColor.GOLD + "[RedStoneMe] " + ChatColor.RED +"Only the owner can access to this trigger");
                                        return true;
                                    }

                                    break;
                                }
                            }

                            if (isDestroyed)
                                player.sendMessage(ChatColor.GOLD + "[RedStoneMe] " + ChatColor.GREEN + "Trigger " + args[1] + " is destroyed");
                            else
                                player.sendMessage(ChatColor.GOLD + "[RedStoneMe] " + ChatColor.RED + "Trigger " + args[1] + " doesn't exist");

                        } else
                            player.sendMessage(ChatColor.GOLD + "[RedStoneMe] " + ChatColor.RED + "You need redstoneme.destroy permission to do that ");


                    } else if (args[0].equalsIgnoreCase("list")) {
                        if (player.hasPermission("redstoneme.list")) {
                            boolean ffound = false;
                            for (RedStoneTrigger rt : redStoneTriggers) {
                                if (rt.getTriggerName().equalsIgnoreCase(args[1])) {
                                    ffound = true;
                                    player.sendMessage(ChatColor.GOLD + "[RedStoneMe] " + ChatColor.GREEN + "Players list of trigger " + rt.getTriggerName() + " :");
                                    for (String uuid : rt.getPlayers()) {
                                        player.sendMessage(ChatColor.GOLD + "-" + ChatColor.GREEN + Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName());
                                    }
                                    break;
                                }
                            }
                            if (!ffound)
                                player.sendMessage(ChatColor.GOLD + "[RedStoneMe] " + ChatColor.RED + "The given trigger doesn't exist");

                        } else
                            player.sendMessage(ChatColor.GOLD + "[RedStoneMe] " + ChatColor.RED + "You need redstoneme.list permission to do that ");

                    } else if (args[0].equalsIgnoreCase("radius")) {
                        if (player.hasPermission("redstoneme.radius")) {
                            boolean found = false;
                            for (RedStoneTrigger rt : redStoneTriggers) {
                                if (rt.getTriggerName().equalsIgnoreCase(args[1])) {
                                    if(isOwnerOfTrigger(player,rt) || player.hasPermission("redstoneme.admin"))
                                    {
                                        player.sendMessage(ChatColor.GOLD + "[RedStoneMe] " + ChatColor.GREEN + "Radius for " + rt.getTriggerName() + " : " + rt.getRadius());
                                        found = true;
                                    } else {
                                        player.sendMessage(ChatColor.GOLD + "[RedStoneMe] " + ChatColor.RED +"Only the owner can access to this trigger");
                                        return true;
                                    }

                                    break;
                                }
                            }
                            if (!found) {
                                player.sendMessage(ChatColor.GOLD + "[RedStoneMe] " + ChatColor.RED + "The given trigger doesn't exist");
                            }
                        } else
                            player.sendMessage(ChatColor.GOLD + "[RedStoneMe] " + ChatColor.RED + "You need redstoneme.radius permission to do that ");
                    } else
                        player.sendMessage(ChatColor.GOLD + "[RedStoneMe] " + ChatColor.RED + "Unknow command use" + ChatColor.WHITE + " /rsm help");

                } else if (args.length == 3) {
                    if (args[0].equalsIgnoreCase("new")) {

                        if (player.hasPermission("redstoneme.new")) {
                            Block block = player.getTargetBlockExact(5);
                            boolean namefree = true;

                            for (RedStoneTrigger rt : redStoneTriggers) {
                                if (rt.getTriggerName().equalsIgnoreCase(args[1])) {
                                    namefree = false;
                                    break;
                                }
                            }

                            if (namefree) {
                                if (block != null) {
                                    RedStoneTrigger rt = new RedStoneTrigger(args[1], Integer.parseInt(args[2]), block.getLocation(), block.getType(), player);
                                    rt.addPlayer(player);
                                    redStoneTriggers.add(rt);
                                    player.sendMessage(ChatColor.GOLD + "[RedStoneMe] " + ChatColor.GREEN + "Trigger " + args[1] + " is enable");
                                    plugin.persistTriggers();
                                } else {
                                    player.sendMessage(ChatColor.GOLD + "[RedStoneMe] " + ChatColor.RED + "You need to target with your cursor the block you want to trigger");
                                }
                            } else {
                                player.sendMessage(ChatColor.GOLD + "[RedStoneMe] " + ChatColor.RED + "This trigger name is already used");
                            }
                        } else
                            player.sendMessage(ChatColor.GOLD + "[RedStoneMe] " + ChatColor.RED + "You need redstoneme.new permission to do that ");

                    } else if (args[0].equalsIgnoreCase("radius")) {

                        if (player.hasPermission("redstoneme.radius")) {

                            for (RedStoneTrigger rt : redStoneTriggers) {
                                if (rt.getTriggerName().equalsIgnoreCase(args[1])) {

                                    if(isOwnerOfTrigger(player,rt) || player.hasPermission("redstoneme.admin"))
                                    {
                                        rt.setRadius(Integer.parseInt(args[2]));
                                        player.sendMessage(ChatColor.GOLD + "[RedStoneMe] " + ChatColor.GREEN + "Trigger radius of " + rt.getTriggerName() + " is set to " + args[2]);
                                        plugin.persistTriggers();
                                    }
                                    else {
                                        player.sendMessage(ChatColor.GOLD + "[RedStoneMe] " + ChatColor.RED +"Only the owner can access to this trigger");
                                        return true;
                                    }

                                    break;
                                }
                            }
                        } else
                            player.sendMessage(ChatColor.GOLD + "[RedStoneMe] " + ChatColor.RED + "You need redstoneme.radius permission to do that ");

                    } else if (args[0].equalsIgnoreCase("state")) {
                        if (player.hasPermission("redstoneme.state")) {
                            if (args[2].equalsIgnoreCase("on") || args[2].equalsIgnoreCase("off")) {
                                boolean foundRt = false;
                                for (RedStoneTrigger rt : redStoneTriggers) {
                                    if (rt.getTriggerName().equalsIgnoreCase(args[1])) {

                                        if(isOwnerOfTrigger(player,rt) || player.hasPermission("redstoneme.admin"))
                                        {
                                            foundRt = true;
                                            rt.setEnable(args[2].equalsIgnoreCase("on"));
                                            this.plugin.persistTriggers();
                                        }
                                        else {
                                            player.sendMessage(ChatColor.GOLD + "[RedStoneMe] " + ChatColor.RED +"Only the owner can access to this trigger");
                                            return true;
                                        }

                                        break;
                                    }
                                }
                                if (foundRt) {
                                    if (args[2].equalsIgnoreCase("on"))
                                        player.sendMessage(ChatColor.GOLD + "[RedStoneMe] " + ChatColor.GREEN + "Trigger " + args[1] + " is now ON");
                                    else
                                        player.sendMessage(ChatColor.GOLD + "[RedStoneMe] " + ChatColor.GREEN + "Trigger " + args[1] + " is now OFF");
                                } else
                                    player.sendMessage(ChatColor.GOLD + "[RedStoneMe] " + ChatColor.RED + "The trigger " + args[1] + " doesn't exist");

                            } else
                                player.sendMessage(ChatColor.GOLD + "[RedStoneMe] " + ChatColor.RED + "State must be ON or OFF");

                        } else
                            player.sendMessage(ChatColor.GOLD + "[RedStoneMe] " + ChatColor.RED + "You need redstoneme.state permission to do that ");

                    } else if (args[0].equalsIgnoreCase("public")) {
                        if (player.hasPermission("redstoneme.public")) {
                            if (args[2].equalsIgnoreCase("on") || args[2].equalsIgnoreCase("off")) {
                                boolean foundRt = false;
                                for (RedStoneTrigger rt : redStoneTriggers) {
                                    if (rt.getTriggerName().equalsIgnoreCase(args[1])) {

                                        if(isOwnerOfTrigger(player,rt) || player.hasPermission("redstoneme.admin"))
                                        {
                                            foundRt = true;
                                            rt.setPublic(args[2].equalsIgnoreCase("on"));
                                            this.plugin.persistTriggers();
                                        }
                                        else {
                                            player.sendMessage(ChatColor.GOLD + "[RedStoneMe] " + ChatColor.RED +"Only the owner can access to this trigger");
                                            return true;
                                        }

                                        break;
                                    }
                                }
                                if (foundRt) {
                                    if (args[2].equalsIgnoreCase("on"))
                                        player.sendMessage(ChatColor.GOLD + "[RedStoneMe] " + ChatColor.GREEN + "Trigger " + args[1] + " is now public");
                                    else
                                        player.sendMessage(ChatColor.GOLD + "[RedStoneMe] " + ChatColor.GREEN + "Trigger " + args[1] + " is now private");
                                } else
                                    player.sendMessage(ChatColor.GOLD + "[RedStoneMe] " + ChatColor.RED + "The trigger " + args[1] + " doesn't exist");

                            } else
                                player.sendMessage(ChatColor.GOLD + "[RedStoneMe] " + ChatColor.RED + "public must be ON or OFF");

                        } else
                            player.sendMessage(ChatColor.GOLD + "[RedStoneMe] " + ChatColor.RED + "You need redstoneme.public permission to do that ");

                    }

                    else if (args[0].equalsIgnoreCase("add")) {

                        if (player.hasPermission("redstoneme.add")) {
                            boolean foundRt = false;
                            boolean foundPlayer = false;
                            for (RedStoneTrigger rt : redStoneTriggers) {
                                if (rt.getTriggerName().equalsIgnoreCase(args[1])) {
                                    if(isOwnerOfTrigger(player,rt) || player.hasPermission("redstoneme.admin"))
                                    {
                                        foundRt = true;
                                        for (Player p : Bukkit.getOnlinePlayers()) {
                                            if (p.getName().equalsIgnoreCase(args[2]) || args[2].equals("*")) {
                                                foundPlayer = true;
                                                if (!rt.getPlayers().contains(p.getUniqueId().toString())) {
                                                    rt.addPlayer(p);
                                                    plugin.persistTriggers();
                                                }
                                                break;
                                            }
                                        }
                                    } else {
                                        player.sendMessage(ChatColor.GOLD + "[RedStoneMe] " + ChatColor.RED +"Only the owner can access to this trigger");
                                        return true;
                                    }
                                }
                            }
                            if (!foundRt)
                                player.sendMessage(ChatColor.GOLD + "[RedStoneMe] " + ChatColor.RED + "Trigger " + args[1] + " doesn't exist");
                            else if (!foundPlayer)
                                player.sendMessage(ChatColor.GOLD + "[RedStoneMe] " + ChatColor.RED + "Player " + args[2] + " doesn't exist");
                            else
                                player.sendMessage(ChatColor.GOLD + "[RedStoneMe] " + ChatColor.GREEN + "Player " + args[2] + " added");
                        } else
                            player.sendMessage(ChatColor.GOLD + "[RedStoneMe] " + ChatColor.RED + "You need redstoneme.add permission to do that ");

                    } else if (args[0].equalsIgnoreCase("remove")) {
                        if (player.hasPermission("redstoneme.remove")) {
                            boolean foundRt = false;
                            boolean foundPlayer = false;
                            boolean removed = false;
                            for (RedStoneTrigger rt : redStoneTriggers) {
                                if (rt.getTriggerName().equalsIgnoreCase(args[1])) {
                                    if(isOwnerOfTrigger(player,rt) || player.hasPermission("redstoneme.admin"))
                                    {
                                        foundRt = true;
                                        for (Player p : Bukkit.getOnlinePlayers()) {
                                            if (p.getName().equalsIgnoreCase(args[2])) {
                                                foundPlayer = true;
                                                removed = rt.removePlayer(p);
                                                plugin.persistTriggers();
                                                break;
                                            }
                                        }
                                    } else {
                                        player.sendMessage(ChatColor.GOLD + "[RedStoneMe] " + ChatColor.RED +"Only the owner can access to this trigger");
                                        return true;
                                    }

                                }
                            }
                            if (!foundRt)
                                player.sendMessage(ChatColor.GOLD + "[RedStoneMe] " + ChatColor.RED + "The trigger " + args[1] + " doesn't exist");
                            else if (!foundPlayer)
                                player.sendMessage(ChatColor.GOLD + "[RedStoneMe] " + ChatColor.RED + "The player " + args[2] + " doesn't exist");
                            else if (!removed)
                                player.sendMessage(ChatColor.GOLD + "[RedStoneMe] " + ChatColor.RED + "The player is not in the list");
                            else
                                player.sendMessage(ChatColor.GOLD + "[RedStoneMe] " + ChatColor.GREEN + "The player " + args[2] + " is removed");
                        } else
                            player.sendMessage(ChatColor.GOLD + "[RedStoneMe] " + ChatColor.RED + "You need redstoneme.remove permission to do that ");

                    } else
                        player.sendMessage(ChatColor.GOLD + "[RedStoneMe] " + ChatColor.RED + "Unknow command use" + ChatColor.WHITE + " /rsm help");


                } else {
                    player.sendMessage(ChatColor.GOLD + "[RedStoneMe] " + ChatColor.RED + "Unknow command use" + ChatColor.WHITE + " /rsm help");
                }
            }
        }
        return true;
    }
}
