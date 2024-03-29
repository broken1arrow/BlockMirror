package org.brokenarrow.blockmirror.blockpatterns.utily;

import org.brokenarrow.blockmirror.api.BlockMirrorAPI;
import org.brokenarrow.blockmirror.api.BlockMirrorUtillity;
import org.brokenarrow.blockmirror.api.blockpattern.PatternData;
import org.brokenarrow.blockmirror.api.blockpattern.PatternSettingsWrapperApi;
import org.brokenarrow.blockmirror.api.builders.ItemWrapper;
import org.brokenarrow.blockmirror.api.builders.patterns.BlockPatterns;
import org.brokenarrow.blockmirror.api.settings.SettingsApi;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.List;

public class SetFillBlocksInPattern implements PatternSettingsWrapperApi {
	BlockMirrorAPI plugin = BlockMirrorUtillity.getInstance();
	private final SettingsApi settings = BlockMirrorUtillity.getInstance().getSettings();
	private final ItemWrapper passive;
	private final ItemWrapper active;

	public SetFillBlocksInPattern(@Nonnull final ItemWrapper passive, final ItemWrapper active) {
		this.passive = passive;
		this.active = active;
	}

	@Override
	public boolean hasPermission(final Player player) {
		String pluginName = plugin.getPluginName().toLowerCase();
		return plugin.hasPermission(player, pluginName + ".change.circle_pattern");
	}

	@Override
	public void leftClick(final PatternData patternData, final Player player) {
		BlockPatterns blockPatterns = this.getBlockPatterns();
		if (blockPatterns != null) {
			if (this.hasPermission(player)) {
				patternData.leftClickMenu();
			}
		}
	}

	@Override
	public void rightClick(final PatternData patternData, final Player player) {
		BlockPatterns blockPatterns = this.getBlockPatterns();
		if (blockPatterns != null) {
			if (this.hasPermission(player)) {
				patternData.rightClickMenu();
			}
		}
	}

	@Override
	public boolean isSettingSet(final Player player) {
		BlockPatterns blockPatterns = this.getBlockPatterns();
		return blockPatterns != null && blockPatterns.getCirclePattern().isFillBlocks();
	}

	@Nonnull
	@Override
	public Material icon(Player player, boolean active) {
		ItemWrapper itemWrapper = getItemWrapper(active);
		if (itemWrapper != null)
			return itemWrapper.getMaterial();
		return Material.OAK_SIGN;
	}

	@Nonnull
	@Override
	public String displayName(Player player, final boolean active) {
		ItemWrapper itemWrapper = getItemWrapper(active);
		if (itemWrapper != null)
			return itemWrapper.getDisplayName();
		return "";
	}

	@Nullable
	@Override
	public List<String> lore(Player player, final boolean active) {
		ItemWrapper itemWrapper = getItemWrapper(active);
		if (itemWrapper != null)
			return itemWrapper.getLore();
		return null;
	}

	@Nullable
	public ItemWrapper getItemWrapper(boolean active) {
		if (this.active == null)
			return this.passive;
		if (active)
			return this.active;
		return this.passive;
	}

	public BlockPatterns getBlockPatterns() {
		return settings.getSettingsData() != null ? settings.getSettingsData().getBlockPatterns() : null;
	}
}
