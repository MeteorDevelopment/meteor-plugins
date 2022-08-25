package meteordevelopment.meteoressentials.commands;

import meteordevelopment.meteoressentials.Perm;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginBase;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public abstract class MeteorCommand extends Command {
    protected final PluginBase plugin;
    protected final Perm perm;

    public MeteorCommand(PluginBase plugin, String name, String description, String usage) {
        super(name, description, usage, Collections.emptyList());

        this.plugin = plugin;
        this.perm = null;
    }

    public MeteorCommand(PluginBase plugin, String name, String description, String usage, Perm perm) {
        super(name, description, usage, Collections.emptyList());

        this.plugin = plugin;
        this.perm = perm;

        if (perm != null) setPermission(perm.permission().getName());
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        boolean success;

        if (!plugin.isEnabled()) {
            throw new CommandException("Cannot execute command '" + commandLabel + "' in plugin " + plugin.getDescription().getFullName() + " - plugin is disabled.");
        }

        if (!testPermission(sender)) {
            return true;
        }

        try {
            success = onCommand(sender, commandLabel, args);
        }
        catch (Throwable ex) {
            throw new CommandException("Unhandled exception executing command '" + commandLabel + "' in plugin " + plugin.getDescription().getFullName(), ex);
        }

        if (!success && usageMessage.length() > 0) {
            for (String line : usageMessage.replace("<command>", commandLabel).split("\n")) {
                sender.sendMessage(line);
            }
        }

        return success;
    }

    protected abstract boolean onCommand(CommandSender sender, String label, String[] args);

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, String[] args, Location location) throws IllegalArgumentException {
        List<String> completions;

        try {
            completions = onTabComplete(sender, alias, args);
        }
        catch (Throwable ex) {
            StringBuilder message = new StringBuilder();
            message.append("Unhandled exception during tab completion for command '/").append(alias).append(' ');

            for (String arg : args) {
                message.append(arg).append(' ');
            }

            message.deleteCharAt(message.length() - 1).append("' in plugin ").append(plugin.getDescription().getFullName());
            throw new CommandException(message.toString(), ex);
        }

        return completions;
    }

    protected List<String> onTabComplete(CommandSender sender, String alias, String[] args) {
        if (!sender.getServer().suggestPlayerNamesWhenNullTabCompletions()) return com.google.common.collect.ImmutableList.of();
        return tabComplete(sender, alias, args);
    }
}
