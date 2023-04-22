package org.brokenarrow.blockmirror.blockpatterns;

import org.brokenarrow.blockmirror.BlockMirror;
import org.brokenarrow.blockmirror.api.blockpattern.PatternData;
import org.brokenarrow.blockmirror.api.blockpattern.PatternSettingsWrapperApi;
import org.brokenarrow.blockmirror.api.builders.ItemWrapper;
import org.brokenarrow.blockmirror.api.builders.PlayerBuilder;
import org.brokenarrow.blockmirror.api.builders.patterns.BlockPatterns;
import org.brokenarrow.blockmirror.settings.Settings;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CirclePattern implements PatternData {

	private final Settings settings = BlockMirror.getPlugin().getSettings();

	@Nonnull
	@Override
	public List<Location> whenPlace(final PlayerBuilder data, final Player player, final Location centerLocation, final Location blockplacedLoc, final int radius) {
		BlockPatterns blockPatterns = settings.getSettingsData() != null ? settings.getSettingsData().getBlockPatterns() : null;
		boolean fillAllBlocks = false;
		if (blockPatterns != null) {
			fillAllBlocks = blockPatterns.getCirclePattern().isFillBlocks();
		}

		// Determine the center of the circle
		int centerX = centerLocation.getBlockX();
		int centerY = centerLocation.getBlockZ();
		List<Location> locations = new ArrayList<>();
		// Iterate over the locations surrounding the center location
		for (int x = centerX - radius; x <= centerX + radius; x++) {
			for (int z = centerY - radius; z <= centerY + radius; z++) {
				// Calculate the distance between this cell and the center location
				double distance = Math.sqrt((x - centerX) * (x - centerX) + (z - centerY) * (z - centerY));
				Location location = null;
				// If the distance is less than or equal to the radius, mark this cell as inside the circle
				if (fillAllBlocks && distance <= radius) {
					location = new Location(centerLocation.getWorld(), x, blockplacedLoc.getBlockY(), z);
				}
				// Check whether this is on the edge of the circle or not.
				if (Math.abs(distance - radius) < 0.5) {
					// Set the location on the edge the circle.
					location = new Location(centerLocation.getWorld(), x, blockplacedLoc.getBlockY(), z);
				}
				if (location != null && !location.equals(blockplacedLoc)) locations.add(location);
			}
		}
		//locations.removeIf(location -> location.equals(blockplacedLoc));
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
			patternSettingsWrapers.add(blockPatterns.getCirclePattern().getPatternSettingsWrapperApi());
		}
		return patternSettingsWrapers;
	}

	@Nonnull
	@Override
	public Material icon(boolean active) {
		BlockPatterns blockPatterns = this.getBlockPatterns();
		if (blockPatterns != null) {
			ItemWrapper itemWrapper = blockPatterns.getCirclePattern().getItemWrapper(active);
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
			ItemWrapper itemWrapper = blockPatterns.getCirclePattern().getItemWrapper(active);
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
			ItemWrapper itemWrapper = blockPatterns.getCirclePattern().getItemWrapper(active);
			if (itemWrapper != null) {
				return itemWrapper.getLore();
			}
		}
		return new ArrayList<>();
	}

	public BlockPatterns getBlockPatterns() {
		return settings.getSettingsData() != null ? settings.getSettingsData().getBlockPatterns() : null;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		final CirclePattern that = (CirclePattern) o;
		return Objects.equals(settings, that.settings);
	}


}
