package me.crylonz;

import me.crylonz.command.AddCommand;
import me.crylonz.command.ActionCommand;
import me.crylonz.command.ClearPlayersCommand;
import me.crylonz.command.CommandContext;
import me.crylonz.command.CommandSpec;
import me.crylonz.command.DebugCommand;
import me.crylonz.command.DestroyCommand;
import me.crylonz.command.HelpCommand;
import me.crylonz.command.InfoCommand;
import me.crylonz.command.ListCommand;
import me.crylonz.command.MoveCommand;
import me.crylonz.command.NewCommand;
import me.crylonz.command.PublicCommand;
import me.crylonz.command.RadiusCommand;
import me.crylonz.command.ReloadCommand;
import me.crylonz.command.RemoveCommand;
import me.crylonz.command.RenameCommand;
import me.crylonz.command.SetOwnerCommand;
import me.crylonz.command.StateCommand;
import me.crylonz.command.SubCommand;
import me.crylonz.command.ToggleCommand;
import me.crylonz.command.TpCommand;
import me.crylonz.command.WhoCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;
import java.util.Map;

public class RSMCommandExecutor implements CommandExecutor {

    private final Map<String, SubCommand> commands = new LinkedHashMap<String, SubCommand>();

    public RSMCommandExecutor(RedStoneMe plugin) {
        CommandContext context = new CommandContext(plugin);

        register(new HelpCommand(context));
        register(new ListCommand(context));
        register(new DestroyCommand(context));
        register(new RadiusCommand(context));
        register(new NewCommand(context));
        register(new StateCommand(context));
        register(new PublicCommand(context));
        register(new AddCommand(context));
        register(new RemoveCommand(context));
        register(new ActionCommand(context));
        register(new InfoCommand(context));
        register(new TpCommand(context));
        register(new RenameCommand(context));
        register(new MoveCommand(context));
        register(new ToggleCommand(context));
        register(new ReloadCommand(context));
        register(new DebugCommand(context));
        register(new WhoCommand(context));
        register(new ClearPlayersCommand(context));
        register(new SetOwnerCommand(context));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("redstoneme")
                && !command.getName().equalsIgnoreCase("rsm")) {
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(colorError() + "Unknown command use /rsm help");
            return true;
        }

        SubCommand subCommand = commands.get(args[0].toLowerCase());
        if (subCommand == null) {
            sender.sendMessage(colorError() + "Unknown command use /rsm help");
            return true;
        }

        if (sender instanceof Player) {
            return subCommand.execute((Player) sender, args);
        }

        return subCommand.executeConsole(sender, args);
    }

    private void register(SubCommand subCommand) {
        commands.put(subCommand.spec().name(), subCommand);
    }

    private String colorError() {
        return ChatColor.GOLD + "[RedStoneMe] " + ChatColor.RED;
    }
}
