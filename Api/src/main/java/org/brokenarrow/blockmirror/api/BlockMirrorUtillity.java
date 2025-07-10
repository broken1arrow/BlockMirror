package org.brokenarrow.blockmirror.api;

import org.bukkit.plugin.java.JavaPlugin;

public class BlockMirrorUtillity extends JavaPlugin {
	protected static BlockMirrorUtillity plugin;

	public static BlockMirrorAPI getInstance() {
		return (BlockMirrorAPI) plugin;
	}

}
