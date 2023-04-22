package org.brokenarrow.blockmirror.api;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public interface CustomPlacementsApi {

	void onBlockPlace(BlockPlaceEvent event);

	void onBlockBreak(BlockBreakEvent event);

	default void onBlockInteract(PlayerInteractEvent event) {
	}
}
