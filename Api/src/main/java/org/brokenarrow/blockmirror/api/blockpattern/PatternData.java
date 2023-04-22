package org.brokenarrow.blockmirror.api.blockpattern;

import org.brokenarrow.blockmirror.api.builders.PlayerBuilder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public interface PatternData {

	/**
	 * When player place one block will this be exicuted.
	 *
	 * @param data           the player data cached.
	 * @param player         the player that place the block.
	 * @param centerLocation the center location set.
	 * @param blockplacedLoc were player place the block.
	 * @param radius         the dictance from the center.
	 * @return list of locations to place blocks.
	 */
	@Nonnull
	List<Location> whenPlace(final PlayerBuilder data, final Player player, final Location centerLocation, final Location blockplacedLoc, final int radius);

	/**
	 * When player break one block will this be exicuted.
	 *
	 * @param data           the player data cached.
	 * @param player         the player that place the block.
	 * @param centerLocation the center location set.
	 * @param blockplacedLoc were player place the block.
	 * @param radius         the dictance from the center.
	 * @return list of locations to place blocks.
	 */
	@Nonnull
	List<Location> whenBreak(final PlayerBuilder data, final Player player, final Location centerLocation, final Location blockplacedLoc, final int radius);

	/**
	 * Set this to true if you want all breaked blocks
	 * end up in players inventory direcly.
	 *
	 * @return false as defult.
	 */
	default boolean allItemsOnBreak() {
		return false;
	}

	/**
	 * Set this to true if you only want air
	 * get replaced not other blocks.
	 *
	 * @return false as defult.
	 */
	default boolean replaceOnlyAir() {
		return false;
	}

	/**
	 * The list of settings for this pattern.
	 *
	 * @return the list of patterns or empty if not used.
	 */
	@Nonnull
	default List<PatternSettingsWrapperApi> getPatternSettingsWrapers() {
		return new ArrayList<>();
	}

	/**
	 * The icon used in the menu.
	 *
	 * @param active set to true if player has activated this function.
	 * @return the material you want as icon.
	 */
	@Nonnull
	Material icon(final boolean active);

	/**
	 * The name of the item.
	 *
	 * @param active set to true if player has activated this function.
	 * @return the name to set.
	 */
	@Nonnull
	String displayName(final boolean active);

	/**
	 * The lore on the item.
	 *
	 * @param active set to true if player has activated this function.
	 * @return the lore to set or null if it not set.
	 */
	@Nullable
	List<String> lore(final boolean active);

	/**
	 * To check if the class match.
	 *
	 * @param o the class instance to compere with.
	 * @return true if the two classes match.
	 */
	@Override
	boolean equals(final Object o);
}
