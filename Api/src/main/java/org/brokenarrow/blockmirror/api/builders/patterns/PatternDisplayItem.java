package org.brokenarrow.blockmirror.api.builders.patterns;

import org.brokenarrow.blockmirror.api.blockpattern.PatternSetting;
import org.brokenarrow.blockmirror.api.builders.ItemWrapper;

import javax.annotation.Nullable;
import java.util.List;

public interface PatternDisplayItem {


	/**
	 * Get the settings for the pattern.
	 *
	 * @return the settings for the pattern.
	 */
	List<PatternSetting> getPatternSettings();

	/**
	 * Try get either the active or passive item wrapper.
	 * If active wrapper is null then it will choose the
	 * passive even if that is null.
	 *
	 * @param active set to true if player has activated this function.
	 * @return the item wrapper or null.
	 */
	@Nullable
	ItemWrapper getItemWrapper(boolean active);

	/**
	 * Return the item data, when player had not select the option.
	 *
	 * @return the wrapper for the item.
	 */
	@Nullable
	ItemWrapper getPassive();

	/**
	 * Return the item data, when player have select the option.
	 *
	 * @return the wrapper for the item.
	 */
	@Nullable
	ItemWrapper getActive();

	boolean isFillAllBlocks();

	/**
	 * Get from settings if it shall fill all blocks.
	 *
	 * @param fillAllBlocks {#code true} if shall fill all blocks inside the pattern.
	 * @return {#code true} if it shall fill the pattern.
	 */
	PatternDisplayItem  fillAllBlocks(boolean fillAllBlocks);



	/**
	 * The settings set for the build in patterns.
	 *
	 * @param patternSettingsWraper
	 * @return this class.
	 */
	PatternDisplayItem setPatternSettingsWraper(final PatternSetting patternSettingsWraper);

	PatternDisplayItem setPatternSettingsWraper(final PatternSetting... patternSettingsWraper);

}
