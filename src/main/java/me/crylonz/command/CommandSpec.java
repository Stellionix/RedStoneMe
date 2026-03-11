package me.crylonz.command;

public final class CommandSpec {

    private final String name;
    private final String permission;
    private final String usage;
    private final String summary;

    public CommandSpec(String name, String permission, String usage, String summary) {
        this.name = name;
        this.permission = permission;
        this.usage = usage;
        this.summary = summary;
    }

    public String name() {
        return name;
    }

    public String permission() {
        return permission;
    }

    public String usage() {
        return usage;
    }

    public String summary() {
        return summary;
    }
}
