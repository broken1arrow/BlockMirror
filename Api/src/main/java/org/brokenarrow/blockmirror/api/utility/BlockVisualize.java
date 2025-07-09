package org.brokenarrow.blockmirror.api.utility;


import org.broken.arrow.library.itemcreator.ItemCreator;
import org.broken.arrow.library.menu.utility.ServerVersion;
import org.broken.arrow.library.visualization.builders.VisualizeData;
import org.brokenarrow.blockmirror.api.BlockMirrorAPI;
import org.brokenarrow.blockmirror.api.BlockMirrorUtillity;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class BlockVisualize {
	private static final BlockMirrorAPI plugin = BlockMirrorUtillity.getInstance();
	private static final org.broken.arrow.library.visualization.BlockVisualize blockVisualize = BlockMirrorUtillity.getBlockVisualizeAPI();
	public static void visulizeBlock(final Block block, final Location location, final boolean shallBeVisualize) {
		visulizeBlock(null, block, location, shallBeVisualize);
	}

	public static void visulizeBlock(final Player player, final Block block, final Location location, final boolean shallBeVisualize) {
		VisualizeData visualizeData = new VisualizeData(player, "", getMaterial());
		blockVisualize.visualizeBlock(player, block, () -> visualizeData, shallBeVisualize);
	}

	public static Material getMaterial() {
		ItemCreator itemCreator = BlockMirrorUtillity.getInstance().getItemCreator();
		Material material = null;
		if (ServerVersion.olderThan(ServerVersion.V1_13))
			material = Material.matchMaterial("STONE");
		return material != null ? material : itemCreator.of("BONE_BLOCK").makeItemStack().getType();
	}

}
