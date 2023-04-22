package org.brokenarrow.blockmirror.api.utility.blockdrops;

import org.brokenarrow.blockmirror.api.utility.Pair;
import org.brokenarrow.blockmirror.api.utility.RandomUntility;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class DropData {

	private final Map<Integer, Double> lootChancesForLevels = new HashMap<>();
	private final double defultChance;
	private AlterAmount alterAmount;
	private CustomDropChance customDropChance;

	public DropData(double defultChance) {
		this.defultChance = defultChance;
	}

	public Double getChance(int level) {
		return lootChancesForLevels.getOrDefault(level, -1.0);
	}

	public double getDefultChance() {
		return defultChance;
	}

	@SafeVarargs
	public final DropData setChanceForLevels(Pair<Integer, Double>... chances) {
		if (chances == null) return this;

		for (Pair<Integer, Double> pair : chances)
			this.setChanceForLevel(pair.getFirst(), pair.getSecond());
		return this;
	}

	public DropData setChanceForLevel(int level, double chance) {
		this.lootChancesForLevels.put(level, chance);
		return this;
	}

	/**
	 * Set own calculation for amount of items.
	 *
	 * @param function your own caluation some need return int.
	 * @return this class.
	 */
	public DropData alterAmount(AlterAmount function) {
		this.alterAmount = function;
		return this;
	}

	/**
	 * Set own calculation after either level reach max or
	 * own percentage calculation.
	 *
	 * @param function your own caluation some need return double.
	 * @return this class.
	 */
	public DropData customDropChance(CustomDropChance function) {
		this.customDropChance = function;
		return this;
	}

	/**
	 * Execute the task to see if new amount is set.
	 *
	 * @param toolLevel
	 * @param tool
	 * @param minedBlock
	 * @return amount if items give to player.
	 */
	protected int executeCalculateAmount(@Nonnull final RandomUntility randomUntility, final int toolLevel, @Nullable final ItemStack tool, @Nonnull final Block minedBlock) {
		if (this.alterAmount == null) return -1;
		int calulateDrop = this.alterAmount.calculateAmount(randomUntility, toolLevel, tool, minedBlock);
		return calulateDrop <= 0 ? -1 : calulateDrop;
	}

	/**
	 * Execute the task to see if chance is set.
	 *
	 * @param toolLevel
	 * @param tool
	 * @param minedBlock
	 * @return procent chance the player get the item.
	 */
	protected double executeCalculateDrop(final int toolLevel, @Nullable final ItemStack tool, @Nonnull final Block minedBlock) {
		if (this.customDropChance == null) return -1;
		double calulateDrop = this.customDropChance.calculateDrop(toolLevel, tool, minedBlock);
		return calulateDrop == 0.0 ? -1 : calulateDrop;
	}
}