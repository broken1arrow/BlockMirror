package org.brokenarrow.blockmirror.api;

import org.brokenarrow.blockmirror.api.blockpattern.PatternData;
import org.brokenarrow.blockmirror.api.blockpattern.PatternSettingsWrapperApi;
import org.brokenarrow.blockmirror.api.builders.ItemWrapper;
import org.brokenarrow.blockmirror.api.builders.patterns.PatternWrapperApi;
import org.brokenarrow.blockmirror.api.settings.LanguageCacheApi;
import org.brokenarrow.blockmirror.api.settings.SettingsApi;
import org.brokenarrow.blockmirror.api.utility.BlockVisualizeAPI;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.logging.Level;

public interface BlockMirrorAPI {

	void log(final Level level, final String message);

	/**
	 * Check if player has admin perm or permission you put in.
	 *
	 * @param player
	 * @param permission
	 * @return true if player have permission.
	 */
	boolean hasPermission(@Nonnull final Player player, @Nonnull final String permission);

	BlockVisualizeAPI getBlockVisualize();

	org.broken.arrow.library.itemcreator.ItemCreator getItemCreator();

	String getPluginName();

	LanguageCacheApi getLanguageCache();

	SettingsApi getSettings();

	PatternWrapperApi createPatternWrapperApi(@Nonnull final ItemWrapper circlePassive, final ItemWrapper circleActive);

	PatternSettingsWrapperApi createSetFillBlocksInPattern(@Nonnull final ItemWrapper passive, final ItemWrapper active);

	PatternSettingsWrapperApi createChangeFacingToPattern(@Nonnull ItemWrapper passive, ItemWrapper active);

	void addPatterns(PatternData pattern);

	PlayerCacheApi getPlayerCache();
}
