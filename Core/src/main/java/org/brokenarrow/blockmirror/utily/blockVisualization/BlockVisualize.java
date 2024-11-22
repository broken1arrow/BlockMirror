package org.brokenarrow.blockmirror.utily.blockVisualization;


import org.broken.arrow.itemcreator.library.ItemCreator;
import org.broken.arrow.menu.library.utility.ServerVersion;
import org.broken.arrow.visualization.library.builders.VisualizeData;
import org.brokenarrow.blockmirror.BlockMirror;
import org.brokenarrow.blockmirror.api.BlockMirrorUtillity;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class BlockVisualize {

	private static final BlockMirror plugin = BlockMirror.getPlugin();
	private static final org.broken.arrow.visualization.library.BlockVisualize visualize = plugin.getBlockVisualize();

	public static void visulizeBlock(final Block block, final Location location, final boolean shallBeVisualize) {
		visulizeBlock(null, block, location, shallBeVisualize);
	}

	public static void visulizeBlock(final Player player, final Block block, final Location location, final boolean shallBeVisualize) {
		VisualizeData visualizeData = new VisualizeData(player, "", getMaterial());
		visualize.visualizeBlock(player, block, () -> visualizeData, shallBeVisualize);
	}

	public static Material getMaterial() {
		ItemCreator itemCreator = BlockMirrorUtillity.getInstance().getItemCreator();
		Material material = null;
		if (ServerVersion.olderThan(ServerVersion.V1_13))
			material = Material.matchMaterial("STONE");
		return material != null ? material : itemCreator.of("BONE_BLOCK").makeItemStack().getType();
	}

}
