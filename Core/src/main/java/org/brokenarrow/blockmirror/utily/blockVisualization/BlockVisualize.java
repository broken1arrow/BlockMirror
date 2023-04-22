package org.brokenarrow.blockmirror.utily.blockVisualization;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class BlockVisualize {
	private static BlockVisualizerCache blockVisualizerCache;

	public static void visulizeBlock(final Block block, final Location location, final boolean shallBeVisualize) {
		visulizeBlock(null, block, location, shallBeVisualize);
	}

	public static void visulizeBlock(final Player player, final Block block, final Location location, final boolean shallBeVisualize) {
		if (blockVisualizerCache == null)
			blockVisualizerCache = new BlockVisualizerCache();
		if (!blockVisualizerCache.isVisualized(block) && shallBeVisualize)
			blockVisualizerCache.visualize(player, block,
					getMaterial(), getText());

		else if (blockVisualizerCache.isVisualized(block) && shallBeVisualize) {
			blockVisualizerCache.stopVisualizing(block);
			blockVisualizerCache.visualize(player, block,
					getMaterial(), getText());

		} else if (blockVisualizerCache.isVisualized(block)) {
			blockVisualizerCache.stopVisualizing(block);
		}
		blockVisualizerCache.getVisualTask().start();
	}

	public static boolean stopVisualizing(final Block block) {
		if (blockVisualizerCache.isVisualized(block)) {
			blockVisualizerCache.stopVisualizing(block);
			return true;
		}
		return false;
	}

	public static Material getMaterial() {

		final Material material = null;
		return material != null ? material : Material.BONE_BLOCK;
	}

	public static String getText() {
		String message = "";
		if (message != null && !message.equals(""))
			return message;
		return "";
	}
}
