package org.brokenarrow.blockmirror;

import org.brokenarrow.blockmirror.api.BlockListener;
import org.brokenarrow.blockmirror.api.BlockMirrorListenersApi;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import javax.annotation.Nonnull;
import java.util.LinkedHashSet;
import java.util.Set;

public class BlockMirrorListeners implements BlockMirrorListenersApi, Listener {

	private final Set<BlockListener> blockListeners = new LinkedHashSet<>();

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		this.getBlockLissners().forEach(task -> task.onBlockPlace(event));
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		this.getBlockLissners().forEach(task -> task.onBlockBreak(event));
	}

	@EventHandler
	public void onBlockInteract(PlayerInteractEvent event) {
		this.getBlockLissners().forEach(task -> task.onBlockInteract(event));
	}

	@EventHandler
	public void dropItem(PlayerDropItemEvent event) {
		this.getBlockLissners().forEach(task -> task.onDropItem(event));
	}

	@EventHandler
	public void swichSlot(PlayerItemHeldEvent event) {
		this.getBlockLissners().forEach(task -> task.onSwichSlot(event));
	}

	@EventHandler
	public void playerJoin(PlayerJoinEvent event) {
		this.getBlockLissners().forEach(task -> task.onPlayerJoin(event));
	}

	@EventHandler
	public void playerQuit(PlayerQuitEvent event) {
		this.getBlockLissners().forEach(task -> task.onPlayerQuit(event));
	}

	@Override
	public void addLissner(@Nonnull BlockListener listener) {
		this.blockListeners.add(listener);
	}

	public Set<BlockListener> getBlockLissners() {
		return blockListeners;
	}
}
