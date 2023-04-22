package org.brokenarrow.blockmirror.api.utility.blockdrops;


import org.brokenarrow.blockmirror.api.utility.RandomUntility;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Set amount of items player can get.
 */

@FunctionalInterface
public interface AlterAmount {

	/**
	 * Sets the amount of items player can get.
	 *
	 * @param randomUntility the random class to set random values or chance.
	 * @param toolLevel      the level of the tool.
	 * @param tool           the tool used.
	 * @param mainedBlock    the block player mined.
	 * @return amount you want to set the items too.
	 */
	int calculateAmount(@Nonnull final RandomUntility randomUntility, final int toolLevel, @Nullable final ItemStack tool, @Nonnull final Block mainedBlock);

}
