package org.brokenarrow.blockmirror.api.eventscustom;

import org.brokenarrow.blockmirror.api.builders.PlayerBuilder;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import java.util.Set;

public class PreBlockPlaceClassic extends EventUtility {
	private static final HandlerList handlers = new HandlerList();

	private final Set<Location> blockMirrorLoc;
	private final Player player;
	private final PlayerBuilder playerBuilder;
	private final boolean hasNeededItems;
	private boolean cancelled;

	public PreBlockPlaceClassic(Player player, PlayerBuilder playerBuilder, Set<Location> blockMirrorLoc, boolean hasNeededItems) {
		super(handlers);
		this.blockMirrorLoc = blockMirrorLoc;
		this.player = player;
		this.playerBuilder = playerBuilder;
		this.hasNeededItems = hasNeededItems;

		this.registerEvent();

	}

	public Set<Location> getBlockMirrorLoc() {
		return blockMirrorLoc;
	}

	public Player getPlayer() {
		return player;
	}

	public PlayerBuilder getPlayerBuilder() {
		return playerBuilder;
	}

	public boolean isHasNeededItems() {
		return hasNeededItems;
	}

	@Override
	public void setCancelled(final boolean cancel) {
		this.cancelled = cancel;
	}

	@Override
	public boolean isCancelled() {
		return this.cancelled;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
