package org.brokenarrow.blockmirror.blockpatterns;

import org.brokenarrow.blockmirror.BlockMirror;
import org.brokenarrow.blockmirror.api.PlayerCacheApi;
import org.brokenarrow.blockmirror.api.blockpattern.PatternData;
import org.brokenarrow.blockmirror.api.blockpattern.PatternSetting;
import org.brokenarrow.blockmirror.api.builders.ItemWrapperApi;
import org.brokenarrow.blockmirror.api.builders.patterns.PatternDisplayItem;
import org.brokenarrow.blockmirror.api.builders.player.PlayerMirrorBuilder;
import org.brokenarrow.blockmirror.api.builders.player.PlayerMirrorDataApi;
import org.brokenarrow.blockmirror.api.settings.SettingsDataApi;
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

public class SquarePattern implements PatternData {
    private PatternDisplayItem settingsSquare;
    private boolean fillBlocks;
    private boolean dynamicRectangle = true;

    public SquarePattern() {
        final SettingsDataApi settings = BlockMirror.getPlugin().getSettings().getSettingsData();
        if (settings != null)
            this.settingsSquare = settings.getBlockPatterns().getSquarePattern();
        if (this.settingsSquare != null)
            this.fillBlocks = this.settingsSquare.isFillAllBlocks();
    }

    @Nonnull
    @Override
    public List<Location> whenPlace(final PlayerMirrorDataApi data, final Player player, final Location centerLocation, final Location blockplacedLoc, final int radius) {
        if (dynamicRectangle) {
            return rectanglePlacement(data, player, centerLocation, blockplacedLoc, radius);
        }

        //if it shall fill all blocks inside the square
        boolean fillAllBlocks = this.shallFillBlocks();

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
    public List<Location> whenBreak(final PlayerMirrorDataApi data, final Player player, final Location location, final Location blockplacedLoc, final int center) {
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

    public boolean isDynamicRectangle() {
        return dynamicRectangle;
    }

    public SquarePattern setDynamicRectangle(boolean dynamicRectangle) {
        this.dynamicRectangle = dynamicRectangle;
        return this;
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
            ItemWrapperApi itemWrapper = blockPatterns.getItemWrapper(active);
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
            ItemWrapperApi itemWrapper = blockPatterns.getItemWrapper(active);
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
            ItemWrapperApi itemWrapper = blockPatterns.getItemWrapper(active);
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
        final PlayerMirrorDataApi data = playerCache.getOrCreateData(player.getUniqueId());
        final PlayerMirrorBuilder builder = data.getBuilder();
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
        return  settingsSquare;
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(settingsSquare);
        result = 31 * result + Boolean.hashCode(fillBlocks);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        SquarePattern that = (SquarePattern) o;
        return fillBlocks == that.fillBlocks && Objects.equals(settingsSquare, that.settingsSquare);
    }

    public List<Location> rectanglePlacement(final PlayerMirrorDataApi data, final Player player, final Location centerLocation, final Location blockplacedLoc, final int radius) {
        boolean fillAllBlocks = this.shallFillBlocks();

        List<Location> locations = new ArrayList<>();

        int centerX = centerLocation.getBlockX();
        int centerZ = centerLocation.getBlockZ();

        int placedX = blockplacedLoc.getBlockX();
        int placedZ = blockplacedLoc.getBlockZ();

        int deltaX = Math.abs(placedX - centerX);
        int deltaZ = Math.abs(placedZ - centerZ);

        int minX, maxX, minZ, maxZ;

        if (deltaX >= deltaZ) {
            // Player is more offset in X direction → fix X to radius, adjust Z
            minX = centerX - radius;
            maxX = centerX + radius;
            int halfZ = Math.abs(placedZ - centerZ);
            minZ = centerZ - halfZ;
            maxZ = centerZ + halfZ;
        } else {
            // Player is more offset in Z direction → fix Z to radius, adjust X
            minZ = centerZ - radius;
            maxZ = centerZ + radius;
            int halfX = Math.abs(placedX - centerX);
            minX = centerX - halfX;
            maxX = centerX + halfX;
        }

        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                boolean isOnBorder = x == minX || x == maxX || z == minZ || z == maxZ;
                Location location = new Location(centerLocation.getWorld(), x, blockplacedLoc.getBlockY(), z);
                if (isOnBorder || fillAllBlocks) {
                    locations.add(location);
                }
            }
        }
        return locations;
    }

}
