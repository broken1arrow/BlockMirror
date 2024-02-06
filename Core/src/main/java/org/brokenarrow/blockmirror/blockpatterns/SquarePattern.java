package org.brokenarrow.blockmirror.blockpatterns;

import org.brokenarrow.blockmirror.BlockMirror;
import org.brokenarrow.blockmirror.api.blockpattern.PatternData;
import org.brokenarrow.blockmirror.api.blockpattern.PatternSettingsWrapperApi;
import org.brokenarrow.blockmirror.api.builders.ItemWrapper;
import org.brokenarrow.blockmirror.api.builders.PlayerBuilder;
import org.brokenarrow.blockmirror.api.builders.patterns.BlockPatterns;
import org.brokenarrow.blockmirror.api.builders.patterns.PatternWrapperApi;
import org.brokenarrow.blockmirror.settings.Settings;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SquarePattern implements PatternData {
	private final Settings settings = BlockMirror.getPlugin().getSettings();

	@Nonnull
	@Override
	public List<Location> whenPlace(final PlayerBuilder data, final Player player, final Location centerLocation, final Location blockplacedLoc, final int radius) {
		BlockPatterns blockPatterns = settings.getSettingsData() != null ? settings.getSettingsData().getBlockPatterns() : null;
		//if it shall fill all blocks inside the square
		boolean fillAllBlocks = false;
		if (blockPatterns != null) {
			fillAllBlocks = blockPatterns.getSquarePattern().isFillBlocks();
		}
		List<Location> locations = new ArrayList<>();

		int centerX = centerLocation.getBlockX();
		int centerY = centerLocation.getBlockZ();

		for (int x = centerX - radius; x <= centerX + radius; x++) {
			for (int z = centerY - radius; z <= centerY + radius; z++) {
				// Check if the current position is on the border of the square
				boolean isOnBorder = x == centerX - radius || x == centerX + radius || z == centerY - radius || z == centerY + radius;
				Location location = new Location(centerLocation.getWorld(), x, blockplacedLoc.getBlockY(), z);
				// Add the location to the list if it's either on the border or it's set to fill all blocks
				if (isOnBorder || fillAllBlocks) {
					locations.add(location);
				}
			}
		}
		return locations;
	}

	@Nonnull
	@Override
	public List<Location> whenBreak(final PlayerBuilder data, final Player player, final Location location, final Location blockplacedLoc, final int center) {
		return this.whenPlace(data, player, location, blockplacedLoc, center);
	}

	@Override
	public boolean allItemsOnBreak() {
		return true;
	}

	@Override
	public boolean replaceOnlyAir() {
		return true;
	}

	@Nonnull
	@Override
	public List<PatternSettingsWrapperApi> getPatternSettingsWrapers() {
		List<PatternSettingsWrapperApi> patternSettingsWrapers = new ArrayList<>();
		BlockPatterns blockPatterns = this.getBlockPatterns();
		if (blockPatterns != null) {
			patternSettingsWrapers.addAll(blockPatterns.getSquarePattern().getPatternSettingsWrapperApi());
		}
		return patternSettingsWrapers;
	}

	@Nonnull
	@Override
	public Material icon(boolean active) {
		BlockPatterns blockPatterns = this.getBlockPatterns();
		if (blockPatterns != null) {
			ItemWrapper itemWrapper = blockPatterns.getSquarePattern().getItemWrapper(active);
			if (itemWrapper != null) {
				return itemWrapper.getMaterial();
			}
		}
		return Material.OAK_SIGN;
	}

	@Nonnull
	@Override
	public String displayName(boolean active) {
		BlockPatterns blockPatterns = this.getBlockPatterns();
		if (blockPatterns != null) {
			ItemWrapper itemWrapper = blockPatterns.getSquarePattern().getItemWrapper(active);
			if (itemWrapper != null) {
				return itemWrapper.getDisplayName();
			}
		}
		return "";
	}

	@Nonnull
	@Override
	public List<String> lore(boolean active) {
		BlockPatterns blockPatterns = this.getBlockPatterns();
		if (blockPatterns != null) {
			ItemWrapper itemWrapper = blockPatterns.getSquarePattern().getItemWrapper(active);
			if (itemWrapper != null) {
				return itemWrapper.getLore();
			}
		}
		return new ArrayList<>();
	}

	@Override
	public void leftClickMenu() {
		PatternWrapperApi squarePattern = this.getBlockPatterns().getSquarePattern();
		squarePattern.setFillBlocks(!squarePattern.isFillBlocks());
	}

	@Override
	public void rightClickMenu() {
		PatternWrapperApi squarePattern = this.getBlockPatterns().getSquarePattern();
		squarePattern.setFillBlocks(!squarePattern.isFillBlocks());
	}

	public BlockPatterns getBlockPatterns() {
		return settings.getSettingsData() != null ? settings.getSettingsData().getBlockPatterns() : null;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		final SquarePattern that = (SquarePattern) o;
		return Objects.equals(settings, that.settings);
	}
}
