package org.brokenarrow.blockmirror.blockpatterns.utily;

import org.brokenarrow.blockmirror.api.BlockMirrorAPI;
import org.brokenarrow.blockmirror.api.BlockMirrorUtility;
import org.brokenarrow.blockmirror.api.PlayerCacheApi;
import org.brokenarrow.blockmirror.api.blockpattern.PatternData;
import org.brokenarrow.blockmirror.api.blockpattern.PatternSetting;
import org.brokenarrow.blockmirror.api.builders.ItemWrapperApi;
import org.brokenarrow.blockmirror.api.builders.language.PlaceholderText;
import org.brokenarrow.blockmirror.api.builders.player.PlayerMirrorDataApi;
import org.brokenarrow.blockmirror.menus.SetBlockFace;
import org.brokenarrow.blockmirror.utily.TextConvertPlaceholders;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.List;

import static org.brokenarrow.blockmirror.menus.type.MenuType.Pattern_Settings;

public class ChangeFacingToPattern implements PatternSetting {
    private final PlayerCacheApi settings = BlockMirrorUtility.getInstance().getPlayerCache();
    @Nullable
    private final String permission;
    private final ItemWrapperApi passive;
    private final ItemWrapperApi active;
    BlockMirrorAPI plugin = BlockMirrorUtility.getInstance();

    public ChangeFacingToPattern(@Nullable String permission, @Nonnull final ItemWrapperApi passive, final ItemWrapperApi active) {
        this.permission = permission;
        this.passive = passive;
        this.active = active;
    }

    @Nonnull
    @Override
    public String getType() {
        return "Change_facing";
    }

    @Override
    public boolean hasPermission(final Player player) {
        if (permission != null && !permission.isEmpty())
            return player.hasPermission(permission);
        return true;
    }

    @Override
    public void leftClick(final PatternData patternData, final Player player) {
        PlayerMirrorDataApi playerData = this.getPlayerData(player);
        new SetBlockFace(playerData, patternData, Pattern_Settings, "set_blockface").menuOpen(player);
    }

    @Override
    public void rightClick(final PatternData patternData, final Player player) {
        PlayerMirrorDataApi playerData = this.getPlayerData(player);
        if (playerData != null)
            this.settings.setPlayerData(player.getUniqueId(), playerData.getBuilder().setBlockRotation(null).build());
    }

    @Override
    public boolean isSettingSet(@Nonnull PatternData patternData, @Nonnull final Player player) {
        PlayerMirrorDataApi playerData = this.getPlayerData(player);
        return playerData != null && playerData.getBlockRotation() != null;
    }

    @Nonnull
    @Override
    public Material icon(Player player, boolean active) {
        ItemWrapperApi itemWrapper = getItemWrapper(active);
        if (itemWrapper != null)
            return itemWrapper.getMaterial();
        return Material.ACACIA_FENCE;
    }

    @Nonnull
    @Override
    public String displayName(@Nonnull final PatternData patternData, @Nonnull final Player player, final boolean active) {
        ItemWrapperApi itemWrapper = getItemWrapper(active);
        if (itemWrapper != null) {
            return fixPlaceholders(player, itemWrapper.getDisplayName());
        }
        return "";
    }

    @Nullable
    @Override
    public List<String> lore(@Nonnull final PatternData patternData, @Nonnull final Player player, final boolean active) {
        ItemWrapperApi itemWrapper = getItemWrapper(active);
        if (itemWrapper != null) {
            return fixPlaceholdersLore(player, itemWrapper.getLore());
        }
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

    public String fixPlaceholders(Player player, String text) {
        PlaceholderText placeholderText = plugin.getLanguageCache().getLanguage().getPlaceholderText();
        String blockFaceNotSet = placeholderText != null ? placeholderText.getBlockFaceNotSet() : "";
        PlayerMirrorDataApi data = getPlayerData(player);
        text = TextConvertPlaceholders.translatePlaceholders(text, player.getName(), data != null && data.getBlockRotation() != null ? data.getBlockRotation().getRotation() : blockFaceNotSet);
        return text;
    }

    public List<String> fixPlaceholdersLore(Player player, List<String> lore) {
        PlaceholderText placeholderText = plugin.getLanguageCache().getLanguage().getPlaceholderText();
        String blockFaceNotSet = placeholderText != null ? placeholderText.getBlockFaceNotSet() : "";
        PlayerMirrorDataApi data = getPlayerData(player);
        if (lore != null)
            lore = TextConvertPlaceholders.translatePlaceholders(lore, player.getName(), data != null && data.getBlockRotation() != null ? data.getBlockRotation().getRotation() : blockFaceNotSet);
        return lore;
    }

    public PlayerMirrorDataApi getPlayerData(Player player) {
        return settings.getData(player.getUniqueId());
    }
}
