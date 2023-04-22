package org.brokenarrow.blockmirror;

import org.brokenarrow.blockmirror.api.BlockMirrorLissnersApi;
import org.brokenarrow.blockmirror.api.CustomPlacementsApi;
import org.brokenarrow.blockmirror.listeners.ClassicPlacement;
import org.brokenarrow.blockmirror.listeners.CustomPlacements;
import org.brokenarrow.blockmirror.listeners.ItemRemove;
import org.brokenarrow.blockmirror.listeners.PatternPlacements;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;

public class BlockMirrorLissners implements BlockMirrorLissnersApi, Listener {

	private final CustomPlacementsApi customPlacements = new CustomPlacements();
	private final CustomPlacementsApi classicPlacement = new ClassicPlacement();
	private final CustomPlacementsApi patternPlacements = new PatternPlacements();
	private final ItemRemove itemRemove = new ItemRemove();

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		customPlacements.onBlockPlace(event);
		classicPlacement.onBlockPlace(event);
		patternPlacements.onBlockPlace(event);
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		customPlacements.onBlockBreak(event);
		classicPlacement.onBlockBreak(event);
		patternPlacements.onBlockBreak(event);
	}

	@EventHandler
	public void onBlockInteract(PlayerInteractEvent event) {
		customPlacements.onBlockInteract(event);
	}

	@EventHandler
	public void dropItem(PlayerDropItemEvent event) {
		itemRemove.dropItem(event);
	}

	@EventHandler
	public void swichSlot(PlayerItemHeldEvent event) {
		itemRemove.swichSlot(event);
	}

	@Override
	public CustomPlacementsApi getCustomPlacements() {
		return customPlacements;
	}
}
