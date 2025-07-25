package org.brokenarrow.blockmirror.blockpatterns.utily;

import org.brokenarrow.blockmirror.api.BlockMirrorAPI;
import org.brokenarrow.blockmirror.api.BlockMirrorUtility;
import org.brokenarrow.blockmirror.api.blockpattern.PatternData;
import org.brokenarrow.blockmirror.api.blockpattern.PatternSetting;
import org.brokenarrow.blockmirror.api.builders.ItemWrapperApi;
import org.brokenarrow.blockmirror.api.builders.language.PlaceholderText;
import org.brokenarrow.blockmirror.utily.TextConvertPlaceholders;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.List;

public class SetFillBlocksInPattern implements PatternSetting {
    private final BlockMirrorAPI plugin = BlockMirrorUtility.getInstance();
    @Nullable
    private final String permission;
    private final ItemWrapperApi passive;
    private final ItemWrapperApi active;

    public SetFillBlocksInPattern(@Nullable String permission, @Nonnull final ItemWrapperApi passive, final ItemWrapperApi active) {
        this.permission = permission;
        this.passive = passive;
        this.active = active;
    }

    @Nonnull
    @Override
    public String getType() {
        return "Set_fill_in_blocks";
    }

    @Override
    public boolean hasPermission(final Player player) {
        if (permission != null && !permission.isEmpty())
            return player.hasPermission(permission);
        return true;
    }

    @Override
    public void leftClick(final PatternData patternData, @Nonnull final Player player) {
        patternData.setFillBlocks(true);
    }

    @Override
    public void rightClick(final PatternData patternData, final Player player) {
        patternData.setFillBlocks(false);
    }

    @Override
    public boolean isSettingSet(@Nonnull PatternData patternData, @Nonnull final Player player) {
        return patternData.shallFillBlocks();
    }

    @Nonnull
    @Override
    public Material icon(Player player, boolean active) {
        ItemWrapperApi itemWrapper = getItemWrapper(active);
        if (itemWrapper != null)
            return itemWrapper.getMaterial();
        return Material.OAK_SIGN;
    }

    @Nonnull
    @Override
    public String displayName(@Nonnull final PatternData patternData, @Nonnull final Player player, final boolean active) {
        ItemWrapperApi itemWrapper = getItemWrapper(active);
        if (itemWrapper != null)
            return fixPlaceholders(patternData, player, itemWrapper.getDisplayName());
        return "";
    }

    @Nullable
    @Override
    public List<String> lore(@Nonnull final PatternData patternData, @Nonnull final Player player, final boolean active) {
        ItemWrapperApi itemWrapper = getItemWrapper(active);
        if (itemWrapper != null)
            return fixPlaceholdersLore(patternData, player, itemWrapper.getLore());
        return null;
    }

    @Nullable
    public ItemWrapperApi getItemWrapper(boolean active) {
        if (this.active == null)
            return this.passive;
        if (active)
            return this.active;
        return this.passive;
    }

    public String fixPlaceholders(PatternData patternData, Player player, String text) {
        PlaceholderText placeholderText = plugin.getLanguageCache().getLanguage().getPlaceholderText();
        boolean fillBlocks = patternData.shallFillBlocks();
        String fillBlockSet = fillBlocks + "";
        if (placeholderText != null)
            fillBlockSet = fillBlocks ? placeholderText.getPlaceholder("fill_blocks_set") : placeholderText.getPlaceholder("fill_blocks_not_set");

        text = TextConvertPlaceholders.translatePlaceholders(text, fillBlockSet);
        return text;
    }

    public List<String> fixPlaceholdersLore(PatternData patternData, Player player, List<String> lore) {
        PlaceholderText placeholderText = plugin.getLanguageCache().getLanguage().getPlaceholderText();

        boolean fillBlocks = patternData.shallFillBlocks();
        String fillBlockSet = fillBlocks + "";
        if (placeholderText != null)
            fillBlockSet = fillBlocks ? placeholderText.getPlaceholder("fill_blocks_set") : placeholderText.getPlaceholder("fill_blocks_not_set");

        if (lore != null)
            lore = TextConvertPlaceholders.translatePlaceholders(lore, fillBlockSet);
        return lore;
    }
}
