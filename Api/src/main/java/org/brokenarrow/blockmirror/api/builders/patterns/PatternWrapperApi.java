package org.brokenarrow.blockmirror.api.builders.patterns;

import org.brokenarrow.blockmirror.api.blockpattern.PatternSettingsWrapperApi;
import org.brokenarrow.blockmirror.api.builders.ItemWrapper;

import javax.annotation.Nullable;
import java.util.List;

public interface PatternWrapperApi {


	/**
	 * Get the settings for the pattern.
	 *
	 * @return the settings wrapper.
	 */
	List<PatternSettingsWrapperApi> getPatternSettingsWrapperApi();

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

	/**
	 * Check if it shall set border only or
	 * fill also all blocks inside border.
	 *
	 * @return true if it shall fill all blocks.
	 */
	boolean isFillBlocks();

	/**
	 * Set if it shall fill in all blocks inside the pattern.
	 *
	 * @param fillBlocks
	 * @return this class.
	 */
	PatternWrapperApi setFillBlocks(boolean fillBlocks);

	/**
	 * The settings set for the build in patterns.
	 *
	 * @param patternSettingsWraper
	 * @return this class.
	 */
	PatternWrapperApi setPatternSettingsWraperApi(final PatternSettingsWrapperApi patternSettingsWraper);

	PatternWrapperApi setPatternSettingsWraperApi(final PatternSettingsWrapperApi... patternSettingsWraper);
}
