package org.brokenarrow.blockmirror.api.blockpattern;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public interface PatternSettingsWrapperApi {

	/**
	 * Check if player has the permission.
	 *
	 * @param player
	 * @return true if player has the permission.
	 */
	boolean hasPermission(final Player player);

	/**
	 * Set the setting for this player.
	 *
	 * @param player
	 */
	void setSetting(final Player player);

	/**
	 * Unset the setting for this player.
	 *
	 * @param player
	 */
	void unSetSetting(final Player player);

	/**
	 * If it currently set to true or false.
	 *
	 * @param player
	 * @return true if player set the setting.
	 */
	boolean isSettingSet(final Player player);

	/**
	 * The icon used in the menu.
	 *
	 * @param active set to true if player has activated this function.
	 * @return the material you want as icon.
	 */
	@Nonnull
	Material icon(boolean active);

	/**
	 * The name of the item.
	 *
	 * @param active set to true if player has activated this function.
	 * @return the name to set.
	 */
	@Nonnull
	String displayName(boolean active);

	/**
	 * The lore on the item.
	 *
	 * @param active set to true if player has activated this function.
	 * @return the lore to set or null if it not set.
	 */
	@Nullable
	List<String> lore(boolean active);
	

}
