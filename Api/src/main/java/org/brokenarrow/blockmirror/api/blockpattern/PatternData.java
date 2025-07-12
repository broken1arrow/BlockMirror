package org.brokenarrow.blockmirror.api.blockpattern;

import org.brokenarrow.blockmirror.api.builders.PlayerBuilder;
import org.brokenarrow.blockmirror.api.builders.patterns.PatternDisplayItem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

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
     * Check if it shall set border only or
     * fill also all blocks inside border.
     *
     * @return true if it shall fill all blocks.
     */
    default boolean shallFillBlocks() {
        return false;
    }

    /**
     * Set if it shall fill in all blocks inside the pattern.
     *
     * @param fillBlocks
     * @return this class.
     */
    default void setFillBlocks(boolean fillBlocks) {
    }


    /**
     * The list of settings for this pattern.
     *
     * @return the list of patterns or empty if not used.
     */
    @Nonnull
    default List<PatternSetting> getPatternSettings() {
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
     * When player clicking on a pattern.
     *
     * @param player the player that clicked.
     * @param click  the type of click player did.
     */
    void clickMenu(@Nonnull final Player player, @Nonnull final ClickType click);

    /**
     * Check if player have correct permission for the pattern when player
     * set or remove blocks.
     *
     * @param player the player to check
     * @return true if player has the permission.
     */
    boolean hasPermission(Player player);

    /**
     * Set the max distance where player is not allowed
     * to place block.
     *
     * @param player the player placed the block
     * @param distance the current distance from center.
     * @return the max distance player is allowed to build from the center.
     */
    double reachMaxDistance(Player player, int distance);

    @Nullable
    PatternDisplayItem getPatternGlobalSettings();

    /**
     * To check if the class match.
     *
     * @param o the class instance to compere with.
     * @return true if the two classes match.
     */
    @Override
    boolean equals(final Object o);

}
