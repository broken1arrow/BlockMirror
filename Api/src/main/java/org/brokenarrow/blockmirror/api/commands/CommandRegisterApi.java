package org.brokenarrow.blockmirror.api.commands;

public interface CommandRegisterApi {

	/**
	 * Register subcommand use {@link CommandBuilder.Builder} to build your
	 * command. Don't forget you also need create 1 command class for every
	 * sub command.
	 *
	 * @param commandBuilder register your build command.
	 */
	void registerSubCommand(CommandBuilder commandBuilder);
}
