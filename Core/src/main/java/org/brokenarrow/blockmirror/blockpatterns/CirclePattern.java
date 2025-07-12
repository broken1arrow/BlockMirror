package org.brokenarrow.blockmirror.blockpatterns;

import org.brokenarrow.blockmirror.BlockMirror;
import org.brokenarrow.blockmirror.api.PlayerCacheApi;
import org.brokenarrow.blockmirror.api.blockpattern.PatternData;
import org.brokenarrow.blockmirror.api.blockpattern.PatternSetting;
import org.brokenarrow.blockmirror.api.builders.ItemWrapper;
import org.brokenarrow.blockmirror.api.builders.PlayerBuilder;
import org.brokenarrow.blockmirror.api.builders.SettingsData;
import org.brokenarrow.blockmirror.api.builders.patterns.PatternDisplayItem;
import org.brokenarrow.blockmirror.api.utility.Actions;
import org.brokenarrow.blockmirror.utily.EffectsActivated;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.metadata.FixedMetadataValue;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class CirclePattern implements PatternData {
    private PatternDisplayItem settingsCircle;
    private boolean fillBlocks;

    public CirclePattern() {
        final SettingsData settings = BlockMirror.getPlugin().getSettings().getSettingsData();
        if (settings != null)
            this.settingsCircle = settings.getBlockPatterns().getCirclePattern();
        if (this.settingsCircle != null)
            this.fillBlocks = this.settingsCircle.isFillAllBlocks();
    }

    @Nonnull
    @Override
    public List<Location> whenPlace(final PlayerBuilder data, final Player player, final Location centerLocation, final Location blockplacedLoc, final int radius) {

        //if it shall fill all blocks inside the circle
        boolean fillAllBlocks = shallFillBlocks();

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

    @Override
    public boolean shallFillBlocks() {
        return this.fillBlocks;
    }

    @Override
    public void setFillBlocks(boolean fillBlocks) {
        this.fillBlocks = fillBlocks;
    }

    @Nonnull
    @Override
    public List<PatternSetting> getPatternSettings() {
        PatternDisplayItem blockPatterns = this.getPatternGlobalSettings();
        if (blockPatterns != null) {
            return Collections.unmodifiableList(blockPatterns.getPatternSettings());
        }
        return new ArrayList<>();
    }

    @Nonnull
    @Override
    public Material icon(boolean active) {
        PatternDisplayItem blockPatterns = this.getPatternGlobalSettings();
        if (blockPatterns != null) {
            ItemWrapper itemWrapper = blockPatterns.getItemWrapper(active);
            if (itemWrapper != null) {
                return itemWrapper.getMaterial();
            }
        }
        return Material.OAK_SIGN;
    }

    @Nonnull
    @Override
    public String displayName(boolean active) {
        PatternDisplayItem blockPatterns = this.getPatternGlobalSettings();
        if (blockPatterns != null) {
            ItemWrapper itemWrapper = blockPatterns.getItemWrapper(active);
            if (itemWrapper != null) {
                return itemWrapper.getDisplayName();
            }
        }
        return "";
    }


    @Nonnull
    @Override
    public List<String> lore(boolean active) {
        PatternDisplayItem blockPatterns = this.getPatternGlobalSettings();
        if (blockPatterns != null) {
            ItemWrapper itemWrapper = blockPatterns.getItemWrapper(active);
            if (itemWrapper != null) {
                return itemWrapper.getLore();
            }
        }
        return new ArrayList<>();
    }

    @Override
    public void clickMenu(@Nonnull final Player player, @Nonnull final ClickType click) {
        final BlockMirror plugin = BlockMirror.getPlugin();
        final PlayerCacheApi playerCache = plugin.getPlayerCache();
        final PlayerBuilder data = playerCache.getOrCreateData(player.getUniqueId());
        final PlayerBuilder.Builder builder = data.getBuilder();
        if (click.isLeftClick()) {
            player.setMetadata(Actions.pattern.name(), new FixedMetadataValue(BlockMirror.getPlugin(), this));
            if (data.getCenterLocation() == null) {
                builder.setCenterLocation(click.isLeftClick() ? player.getLocation() : null);
                EffectsActivated.setEffect(player, data, builder);
            }
        } else {
            EffectsActivated.removeEffect(player, data);
            builder.setCenterLocation(null);
            player.removeMetadata(Actions.pattern.name(), BlockMirror.getPlugin());
        }
        playerCache.setPlayerData(player.getUniqueId(), builder.build());
    }

    @Override
    public boolean hasPermission(Player player) {
        final BlockMirror plugin = BlockMirror.getPlugin();
        final String pluginName = plugin.getPluginName().toLowerCase();

        return plugin.hasPermission(player, pluginName + ".change.circle_pattern");
    }

    @Override
    public double reachMaxDistance(Player player, int distance) {
        return 30;
    }

    @Override
    public PatternDisplayItem getPatternGlobalSettings() {
        return this.settingsCircle;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        CirclePattern that = (CirclePattern) o;
        return fillBlocks == that.fillBlocks && Objects.equals(settingsCircle, that.settingsCircle);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(settingsCircle);
        result = 31 * result + Boolean.hashCode(fillBlocks);
        return result;
    }
}
