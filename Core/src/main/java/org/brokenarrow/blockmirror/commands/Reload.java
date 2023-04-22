package org.brokenarrow.blockmirror.commands;

import org.brokenarrow.blockmirror.BlockMirror;
import org.brokenarrow.blockmirror.api.builders.language.Language;
import org.brokenarrow.blockmirror.api.commands.CommandHolder;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.List;

public class Reload extends CommandHolder {

	public Reload() {
		super("reload");
	}

	@Override
	public void onCommand(final CommandSender sender, @Nonnull final String commandLabel, @Nonnull final String[] cmdArg) {
		Language language = BlockMirror.getPlugin().getLanguageCache().getLanguage();
		BlockMirror.getPlugin().getSettings().reload();
		BlockMirror.getPlugin().getMenusCache().reload();
		BlockMirror.getPlugin().getLanguageCache().reload();
		if (language.getPluginMessages() != null) {
			String pluginName = language.getPluginMessages().getPluginName();
			if (pluginName == null)
				pluginName = "";
			for (String message : language.getPluginMessages().getMessage("Command_reload"))
				BlockMirror.getPlugin().sendPlainMessage(sender, pluginName + message);
		}
	}

	@Nullable
	@Override
	public List<String> onTabComplete(@Nonnull final CommandSender sender, @Nonnull final String commandLabel, @Nonnull final String[] cmdArg) {
		return super.onTabComplete(sender, commandLabel, cmdArg);
	}
}
