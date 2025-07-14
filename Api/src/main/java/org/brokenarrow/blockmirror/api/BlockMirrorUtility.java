package org.brokenarrow.blockmirror.api;

import org.bukkit.plugin.java.JavaPlugin;

public class BlockMirrorUtility extends JavaPlugin {
	protected static BlockMirrorUtility plugin;

	public static BlockMirrorAPI getInstance() {
		return (BlockMirrorAPI) plugin;
	}

}
