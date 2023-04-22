package org.brokenarrow.blockmirror.commandhandler;


import org.brokenarrow.blockmirror.api.commands.CommandBuilder;
import org.brokenarrow.blockmirror.api.commands.CommandRegisterApi;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public enum CommandRegister implements CommandRegisterApi {
	INSTANCE();

	private final List<CommandBuilder> commands = Collections.synchronizedList(new ArrayList<>());

	private String commandLableMessage;
	private String commandLableMessageNoPerms;
	private List<String> helpPrefixMessage;
	private List<String> helpSuffixMessage;
	private boolean registedMainCommand;

	public static CommandRegister getInstance() {
		return INSTANCE;
	}

	/**
	 * Register subcommand use {@link org.brokenarrow.blockmirror.api.commands.CommandBuilder.Builder} to build your
	 * command. Don't forget you also need create 1 command class for every
	 * sub command.
	 *
	 * @param commandBuilder register your build command.
	 */
	@Override
	public void registerSubCommand(final CommandBuilder commandBuilder) {
		final String[] lableSplit;
		if (commandBuilder.getSubLable() == null) {
			lableSplit = commandBuilder.getExecutor().getCommandLable().split("\\|");
		} else {
			lableSplit = commandBuilder.getSubLable().split("\\|");
		}
		if (collectCommands(commandBuilder, lableSplit)) {
			return;
		}
		commands.removeIf(oldCommandBuilder -> oldCommandBuilder.equals(commandBuilder));
		commands.removeIf(oldCommandBuilder -> oldCommandBuilder.getSubLable().equals(commandBuilder.getSubLable()));
		commands.add(commandBuilder);
		commands.sort(Comparator.comparing(CommandBuilder::getSubLable));
	}

	public String getCommandLableMessage() {
		return commandLableMessage;
	}

	/**
	 * Use {lable} to replace it with the command name and {perm} to get permission.
	 *
	 * @param commandLableMessage the message send for every subcomnmand.
	 * @return this class.
	 */
	public CommandRegister setCommandLableMessage(final String commandLableMessage) {
		this.commandLableMessage = commandLableMessage;
		return this;
	}

	public String getCommandLableMessageNoPerms() {
		return commandLableMessageNoPerms;
	}

	/**
	 * Use {lable} to replace it with the command name and {perm} to get permission. Used if you not have permission.
	 *
	 * @param commandLableMessage the message send for every subcomnmand.
	 * @return this class.
	 */
	public CommandRegister setCommandLableMessageNoPerms(final String commandLableMessage) {
		this.commandLableMessageNoPerms = commandLableMessage;
		return this;
	}

	public List<String> getHelpPrefixMessage() {
		return helpPrefixMessage;
	}

	/**
	 * Help message befor the command sugestions.
	 *
	 * @param helpPrefixMessage the message send before.
	 * @return this class.
	 */
	public CommandRegister setHelpPrefixMessage(final String... helpPrefixMessage) {
		this.helpPrefixMessage = Arrays.asList(helpPrefixMessage);
		return this;
	}

	/**
	 * Help message befor the command sugestions.
	 *
	 * @param helpPrefixMessage the message send before.
	 * @return this class.
	 */
	public CommandRegister setHelpPrefixMessage(final List<String> helpPrefixMessage) {
		this.helpPrefixMessage = helpPrefixMessage;
		return this;
	}

	public List<String> getHelpSuffixMessage() {
		return helpSuffixMessage;
	}

	/**
	 * Help message after the command sugestions.
	 *
	 * @param helpSuffixMessage the message send before.
	 * @return this class.
	 */
	public CommandRegister setHelpSuffixMessage(final String... helpSuffixMessage) {
		this.helpSuffixMessage = Arrays.asList(helpSuffixMessage);
		return this;
	}

	/**
	 * Help message after the command sugestions.
	 *
	 * @param helpSuffixMessage the message send before.
	 * @return this class.
	 */
	public CommandRegister setHelpSuffixMessage(final List<String> helpSuffixMessage) {
		this.helpSuffixMessage = helpSuffixMessage;
		return this;
	}

	/**
	 * Unregister your added command. You can't then run the command when is removed.
	 *
	 * @param subLable your command used and it will be unregisted.
	 */

	public void unregisterSubCommand(final String subLable) {
		commands.removeIf(commandBuilder -> commandBuilder.getSubLable().equals(subLable));
	}

	/**
	 * Get all registed commands.
	 *
	 * @return list of all commands added.
	 */
	public List<CommandBuilder> getCommands() {
		return commands;
	}

	public CommandBuilder getCommandBuilder(final String lable) {
		return getCommandBuilder(lable, false);
	}

	public CommandBuilder getCommandBuilder(final String lable, final boolean startsWith) {
		for (final CommandBuilder command : commands) {
			if (startsWith && (lable.isEmpty() || command.getSubLable().startsWith(lable)))
				return command;
			if (command.getSubLable().equalsIgnoreCase(lable))
				return command;
		}
		return null;
	}

	/**
	 * Register main command used for your subcommands.
	 *
	 * @param fallbackPrefix the prefix to use if could not use the normal command.
	 * @param mainCommand    the command you want to register.
	 */
	public CommandRegister registerMainCommand(final String fallbackPrefix, final String mainCommand) {
		return this.registerMainCommand(fallbackPrefix, mainCommand, "", "", new String[]{});
	}

	/**
	 * Register main command used for your subcommands.
	 *
	 * @param fallbackPrefix the prefix to use if could not use the normal command.
	 * @param aliases        set alias for your command to use insted of the main command.
	 * @param mainCommand    the command you want to register.
	 */
	public CommandRegister registerMainCommand(final String fallbackPrefix, final String mainCommand, final String... aliases) {
		return this.registerMainCommand(fallbackPrefix, mainCommand, "", "", aliases);
	}

	/**
	 * Register main command used for your subcommands.
	 *
	 * @param fallbackPrefix the prefix to use if could not use the normal command.
	 * @param mainCommand    the command you want to register.
	 * @param description    description of the command.
	 * @param usageMessage   message how to use the command.
	 * @param aliases        set alias for your command to use insted of the main command.
	 */

	public CommandRegister registerMainCommand(final String fallbackPrefix, final String mainCommand, final String description, final String usageMessage, final String... aliases) {
		final String[] main = mainCommand.split("\\|");
		if (registedMainCommand) return this;
		if (main.length > 1)
			for (final String command : main)
				this.register(fallbackPrefix, new CommandsUtility(command, description, usageMessage, Arrays.asList(aliases)));
		else
			this.register(fallbackPrefix, new CommandsUtility(mainCommand, description, usageMessage, Arrays.asList(aliases)));
		registedMainCommand = true;

		return this;
	}

	public boolean collectCommands(final CommandBuilder commandBuilder, final String[] commandlabels) {
		if (commandlabels.length > 1) {
			for (final String lable : commandlabels) {
				final CommandBuilder newComandBuilder = commandBuilder.getBuilder().setSubLable(lable).build();
				commands.removeIf(oldCommandBuilder -> oldCommandBuilder.getSubLable().equals(lable));
				commands.add(newComandBuilder);
			}
			commands.sort(Comparator.comparing(CommandBuilder::getSubLable));
			return true;
		}
		return false;
	}

	/**
	 * Use registerMainCommand metods to register a command.
	 *
	 * @param fallbackPrefix the prefix to use if could not use the normal command.
	 * @param command        the command you want to register.
	 */
	public void register(final String fallbackPrefix, final Command command) {
		try {
			final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");

			bukkitCommandMap.setAccessible(true);
			final CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());

			commandMap.register(fallbackPrefix, command);
		} catch (final NoSuchFieldException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

}
