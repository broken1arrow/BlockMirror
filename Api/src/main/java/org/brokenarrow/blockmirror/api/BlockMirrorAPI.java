package org.brokenarrow.blockmirror.api;

import org.brokenarrow.blockmirror.api.blockpattern.PatternData;
import org.brokenarrow.blockmirror.api.blockpattern.PatternSetting;
import org.brokenarrow.blockmirror.api.builders.ItemWrapper;
import org.brokenarrow.blockmirror.api.builders.patterns.PatternDisplayItem;
import org.brokenarrow.blockmirror.api.settings.LanguageCacheApi;
import org.brokenarrow.blockmirror.api.settings.SettingsApi;
import org.brokenarrow.blockmirror.api.utility.BlockVisualizeAPI;
import org.brokenarrow.blockmirror.api.utility.BossBarAPI;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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

	BossBarAPI getBar();

	SettingsApi getSettings();

	PatternDisplayItem createPatternWrapperApi(@Nonnull final ItemWrapper passive, final ItemWrapper active);

	PatternSetting createSetFillBlocksInPattern(@Nullable String permission, @Nonnull final ItemWrapper passive, final ItemWrapper active);

	PatternSetting createChangeFacingToPattern(@Nullable String permission, @Nonnull ItemWrapper passive, ItemWrapper active);

	void addPatterns(PatternData pattern);

	PlayerCacheApi getPlayerCache();
}
