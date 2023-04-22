package org.brokenarrow.blockmirror.api.utility.blockdrops;

import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * This is for calulate dropchance either when reach max level on the tool or
 * whant set custom one from level 0 to x amount.
 */
@FunctionalInterface
public interface CustomDropChance {

	/**
	 * Your own calculation of the drop chance.
	 *
	 * @param toolLevel   the level of the tool.
	 * @param tool        the tool used.
	 * @param mainedBlock the block player mined.
	 * @return the procent chance for the drop.
	 */
	double calculateDrop(final int toolLevel, @Nullable final ItemStack tool, @Nonnull final Block mainedBlock);
}
