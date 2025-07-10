package org.brokenarrow.blockmirror.utily;


import org.broken.arrow.library.itemcreator.ItemCreator;
import org.broken.arrow.library.menu.utility.ServerVersion;
import org.broken.arrow.library.visualization.builders.VisualizeData;
import org.brokenarrow.blockmirror.api.BlockMirrorUtillity;
import org.brokenarrow.blockmirror.api.utility.BlockVisualizeAPI;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class BlockVisualize implements BlockVisualizeAPI {
	private final org.broken.arrow.library.visualization.BlockVisualize blockVisualize;

	public BlockVisualize(Plugin plugin) {
		blockVisualize = new org.broken.arrow.library.visualization.BlockVisualize(plugin);
	}

	@Override
  public void visulizeBlock(final Block block, final Location location, final boolean shallBeVisualize) {
		visulizeBlock(null, block, location, shallBeVisualize);
	}

	@Override
  public void visulizeBlock(final Player player, final Block block, final Location location, final boolean shallBeVisualize) {
		VisualizeData visualizeData = new VisualizeData(player ,"", getMaterial());
		blockVisualize.visualizeBlock(player, block, () -> visualizeData, shallBeVisualize);
	}

	@Override
  public Material getMaterial() {
		ItemCreator itemCreator = BlockMirrorUtillity.getInstance().getItemCreator();
		Material material = null;
		if (ServerVersion.olderThan(ServerVersion.V1_13))
			material = Material.matchMaterial("STONE");
		return material != null ? material : itemCreator.of("BONE_BLOCK").makeItemStack().getType();
	}

}
