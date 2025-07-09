package org.brokenarrow.blockmirror.api;

import org.broken.arrow.library.visualization.BlockVisualize;
import org.bukkit.plugin.java.JavaPlugin;

public class BlockMirrorUtillity extends JavaPlugin {
	protected static BlockMirrorUtillity plugin;
	private static BlockVisualize blockVisualize;

	public static BlockMirrorAPI getInstance() {
		return (BlockMirrorAPI) plugin;
	}

	public BlockMirrorUtillity() {
		blockVisualize =  new org.broken.arrow.library.visualization.BlockVisualize(this);
	}

	public static org.broken.arrow.library.visualization.BlockVisualize getBlockVisualizeAPI() {
		return blockVisualize;
	}
}
