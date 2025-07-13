package org.brokenarrow.blockmirror.blockpatterns.utily;

import org.brokenarrow.blockmirror.api.BlockMirrorAPI;
import org.brokenarrow.blockmirror.api.BlockMirrorUtillity;
import org.brokenarrow.blockmirror.api.blockpattern.PatternData;
import org.brokenarrow.blockmirror.api.blockpattern.PatternSetting;
import org.brokenarrow.blockmirror.api.builders.ItemWrapper;
import org.brokenarrow.blockmirror.api.builders.language.PlaceholderText;
import org.brokenarrow.blockmirror.blockpatterns.SquarePattern;
import org.brokenarrow.blockmirror.utily.TextConvertPlaceholders;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

public class DynamicRectangle implements PatternSetting {
    private final BlockMirrorAPI plugin = BlockMirrorUtillity.getInstance();
    private final String permission;
    private final ItemWrapper passive;
    private final ItemWrapper active;

    public DynamicRectangle(String permission, ItemWrapper passive, ItemWrapper active) {
        this.permission = permission;
        this.passive = passive;
        this.active = active;
    }

    @Nonnull
    @Override
    public String getType() {
        return "Dynamic_rectangle";
    }

    @Override
    public boolean hasPermission(Player player) {
        if (permission != null && !permission.isEmpty())
            return player.hasPermission(permission);
        return true;
    }

    @Override
    public void leftClick(PatternData patternData, Player player) {
        if (patternData instanceof SquarePattern) {
            SquarePattern squarePattern = (SquarePattern) patternData;
            squarePattern.setDynamicRectangle(true);
        }
    }

    @Override
    public void rightClick(PatternData patternData, Player player) {
        if (patternData instanceof SquarePattern) {
            SquarePattern squarePattern = (SquarePattern) patternData;
            squarePattern.setDynamicRectangle(false);
        }
    }

    @Override
    public boolean isSettingSet(@Nonnull PatternData patternData, @Nonnull Player player) {
        if (patternData instanceof SquarePattern) {
            SquarePattern squarePattern = (SquarePattern) patternData;
            return squarePattern.isDynamicRectangle();
        }
        return false;
    }

    @Nonnull
    @Override
    public Material icon(Player player, boolean active) {
        ItemWrapper itemWrapper = getItemWrapper(active);
        if (itemWrapper != null)
            return itemWrapper.getMaterial();
        return Material.ACTIVATOR_RAIL;
    }

    @Nonnull
    @Override
    public String displayName(@Nonnull PatternData patternData, @Nonnull Player player, boolean active) {
        ItemWrapper itemWrapper = getItemWrapper(active);
        if (itemWrapper != null)
            return fixPlaceholders(patternData, player, itemWrapper.getDisplayName());
        return "Dynamic rectangle";
    }

    @Override
    public @Nullable List<String> lore(@Nonnull PatternData patternData, @Nonnull Player player, boolean active) {
        ItemWrapper itemWrapper = getItemWrapper(active);
        if (itemWrapper != null)
            return fixPlaceholdersLore(patternData, player, itemWrapper.getLore());
        boolean isSet = false;
        if (patternData instanceof SquarePattern) {
            SquarePattern squarePattern = (SquarePattern) patternData;
            isSet =  squarePattern.isDynamicRectangle();
        }
        return Arrays.asList(
                " ",
                "&fDynamically adjusts the size in one direction from",
                "&fthe center. This means in either the X or Z direction,",
                "&fit will respect the player’s block placement.",
                "&fOn the opposite side of your placement, it will",
                "&fmirror the distance from the center.",
                " ",
                "&aCurrently set: &6" + isSet
        );
    }

    @Nullable
    public ItemWrapper getItemWrapper(boolean active) {
        if (this.active == null)
            return this.passive;
        if (active)
            return this.active;
        return this.passive;
    }

    public String fixPlaceholders(PatternData patternData, Player player, String text) {
        PlaceholderText placeholderText = plugin.getLanguageCache().getLanguage().getPlaceholderText();
        boolean dynamicRec = false;
        if (patternData instanceof SquarePattern) {
            SquarePattern squarePattern = (SquarePattern) patternData;
            dynamicRec =  squarePattern.isDynamicRectangle();
        }
        String fillBlockSet = dynamicRec + "";
        if (placeholderText != null)
            fillBlockSet = dynamicRec ? placeholderText.getPlaceholder("fill_blocks_set") : placeholderText.getPlaceholder("fill_blocks_not_set");

        text = TextConvertPlaceholders.translatePlaceholders(text, fillBlockSet);
        return text;
    }

    public List<String> fixPlaceholdersLore(PatternData patternData, Player player, List<String> lore) {
        PlaceholderText placeholderText = plugin.getLanguageCache().getLanguage().getPlaceholderText();

        boolean dynamicRec = false;
        if (patternData instanceof SquarePattern) {
            SquarePattern squarePattern = (SquarePattern) patternData;
            dynamicRec =  squarePattern.isDynamicRectangle();
        }
        String fillBlockSet = dynamicRec + "";
        if (placeholderText != null)
            fillBlockSet = dynamicRec ? placeholderText.getPlaceholder("fill_blocks_set") : placeholderText.getPlaceholder("fill_blocks_not_set");

        if (lore != null)
            lore = TextConvertPlaceholders.translatePlaceholders(lore, fillBlockSet);
        return lore;
    }
}
