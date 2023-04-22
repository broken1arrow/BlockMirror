package org.brokenarrow.blockmirror.api;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public interface blockListener {

	default void onBlockPlace(BlockPlaceEvent event) {
	}

	default void onBlockBreak(BlockBreakEvent event) {
	}

	default void onBlockInteract(PlayerInteractEvent event) {
	}

	default void onSwichSlot(PlayerItemHeldEvent event) {
	}

	default void onDropItem(PlayerDropItemEvent event) {
	}

	default void onPlayerJoin(PlayerJoinEvent event) {
	}

	default void onPlayerQuit(PlayerQuitEvent event) {
	}

}
