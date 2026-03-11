package me.crylonz.command;

public abstract class AbstractSubCommand implements SubCommand {

    private final CommandContext context;
    private final CommandSpec spec;

    protected AbstractSubCommand(CommandContext context, CommandSpec spec) {
        this.context = context;
        this.spec = spec;
    }

    @Override
    public CommandContext context() {
        return context;
    }

    @Override
    public CommandSpec spec() {
        return spec;
    }
}
