package org.brokenarrow.blockmirror.api.eventscustom;

import org.brokenarrow.blockmirror.api.builders.Distance;
import org.brokenarrow.blockmirror.api.builders.player.PlayerMirrorDataApi;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import java.util.List;

public class PreBlockBreakCustom extends EventUtility {
	private static final HandlerList handlers = new HandlerList();

	private final List<Distance> distances;
	private final Player player;
	private final PlayerMirrorDataApi playerBuilder;

	private boolean cancelled;

	public PreBlockBreakCustom(Player player, PlayerMirrorDataApi playerBuilder, List<Distance> distances) {
		super(handlers);
		this.distances = distances;
		this.player = player;
		this.playerBuilder = playerBuilder;

		this.registerEvent();

	}

	public List<Distance> getDistances() {
		return distances;
	}

	public Player getPlayer() {
		return player;
	}

	public PlayerMirrorDataApi getPlayerBuilder() {
		return playerBuilder;
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
