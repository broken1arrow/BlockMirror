package org.brokenarrow.blockmirror.commandhandler;

import org.brokenarrow.blockmirror.api.commands.CommandBuilder;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.broken.arrow.library.color.TextTranslator.toSpigotFormat;


public class CommandsUtility extends Command {

	CommandRegister commandRegister = CommandRegister.getInstance();

	public CommandsUtility(@NotNull final String name, @NotNull final String description, @NotNull final String usageMessage, @NotNull final List<String> aliases) {
		super(name, description, usageMessage, aliases);
	}

	/**
	 * Executes the command, returning its success
	 *
	 * @param sender       Source object which is executing this command
	 * @param commandLabel The alias of the command used
	 * @param args         All arguments passed to the command, split via ' '
	 * @return true if the command was successful, otherwise false
	 */
	@Override
	public boolean execute(@NotNull final CommandSender sender, @NotNull final String commandLabel, @NotNull final String[] args) {
		if (args.length == 0) {
			final List<String> helpPrefixMessage = commandRegister.getHelpPrefixMessage();
			if (helpPrefixMessage != null && !helpPrefixMessage.isEmpty())
				for (final String prefixMssage : helpPrefixMessage)
					sender.sendMessage(colors(prefixMssage));
			final String commandLableMessage = commandRegister.getCommandLableMessage();
			if (commandLableMessage != null && !commandLableMessage.isEmpty())
				for (final CommandBuilder subcommand : commandRegister.getCommands()) {
					if (subcommand.isHideLable() && !checkPermission(sender, subcommand)) {
						continue;
					}
					if (!checkPermission(sender, subcommand) && commandRegister.getCommandLableMessageNoPerms() != null && !commandRegister.getCommandLableMessageNoPerms().isEmpty()) {
						sender.sendMessage(colors(placeholders(commandLableMessage, commandLabel, subcommand)));
					} else {
						sender.sendMessage(colors(placeholders(commandLableMessage, commandLabel, subcommand)));
					}
				}
			final List<String> helpSuffixMessage = commandRegister.getHelpSuffixMessage();
			if (helpSuffixMessage != null && !helpSuffixMessage.isEmpty())
				for (final String suffixMssage : helpSuffixMessage)
					sender.sendMessage(colors(suffixMssage));
		}
		if (args.length > 0) {
			final CommandBuilder executor = commandRegister.getCommandBuilder(args[0]);
			if (executor != null) {
				if (!checkPermission(sender, executor)) {
					if (executor.getPermissionMessage() != null)
						sender.sendMessage(colors(placeholders(executor.getPermissionMessage(), commandLabel, executor)));
					return false;
				}
				if (args.length == 1) {
					if (executor.getDescription() != null)
						sender.sendMessage(placeholders(executor.getDescription(), commandLabel, executor));
					if (executor.getUsageMessages() != null) for (final String usage : executor.getUsageMessages()) {
						sender.sendMessage(colors(placeholders(usage, commandLabel, executor)));
					}
				}
				executor.getExecutor().onCommand(sender, commandLabel, Arrays.copyOfRange(args, 1, args.length));
			}
		}
		return false;
	}

	@NotNull
	@Override
	public List<String> tabComplete(@NotNull final CommandSender sender, @NotNull final String alias, @NotNull final String[] args) throws IllegalArgumentException {
		if (args.length > 0) {
			final CommandBuilder subcommand = commandRegister.getCommandBuilder(args[0], true);
			if (subcommand == null) return new ArrayList<>();
			if (args.length == 1) return tabCompleteSubcommands(sender, args[0], subcommand.isHideLable());
			final List<String> tabComplete = subcommand.getExecutor().onTabComplete(sender, alias, Arrays.copyOfRange(args, 1, args.length));
			return tabComplete != null && checkPermission(sender, subcommand) ? tabComplete : new ArrayList<>();
		}
		return new ArrayList<>();
	}


	@NotNull
	@Override
	public List<String> tabComplete(@NotNull final CommandSender sender, @NotNull final String alias, @NotNull final String[] args, @Nullable final Location location) throws IllegalArgumentException {
		return tabComplete(sender, alias, args);
	}

	private boolean checkPermission(final CommandSender sender, final CommandBuilder commandBuilder) {
		if (commandBuilder.getPermission() == null || commandBuilder.getPermission().isEmpty()) return true;
		if (!(sender instanceof Player)) return true;
		final Player player = (Player) sender;

		return player.isOp() || player.hasPermission(commandBuilder.getPermission());
	}

	private List<String> tabCompleteSubcommands(final CommandSender sender, String param, final boolean overridePermission) {
		param = param.toLowerCase();
		final List<String> tab = new ArrayList<>();
		for (final CommandBuilder subcommand : commandRegister.getCommands()) {
			final String label = subcommand.getSubLable();
			if (!checkPermission(sender, subcommand) && overridePermission) {
				continue;
			}

			if (!label.trim().isEmpty() && label.startsWith(param)) tab.add(label);
		}
		return tab;
	}

	public String placeholders(final String message, final String commandLabel, final CommandBuilder subcommand) {
		if (message == null) return "";
		String permission = subcommand.getPermission();
		if (permission == null)
			permission = "";
		return message.replace("{lable}", "/" + commandLabel + " " + subcommand.getSubLable()).replace("{perm}", permission);
	}

	public String colors(final String message) {
		if (message == null) return "";
		return toSpigotFormat(message);
	}
}
