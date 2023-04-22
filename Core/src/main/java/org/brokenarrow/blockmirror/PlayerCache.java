package org.brokenarrow.blockmirror;

import org.brokenarrow.blockmirror.api.PlayerCacheApi;
import org.brokenarrow.blockmirror.api.builders.PlayerBuilder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerCache implements PlayerCacheApi {

	private final Map<UUID, PlayerBuilder> playerCache = new HashMap<>();

	private Map<UUID, PlayerBuilder> getPlayerCache() {
		return playerCache;
	}

	@Override
	@Nullable
	public PlayerBuilder getData(UUID uuid) {
		return this.getPlayerCache().get(uuid);
	}

	/**
	 * Will create data if not exist, if it alredy
	 * exist data it will return that.
	 *
	 * @param uuid the player.
	 * @return new data if not alredy exist.
	 */
	@Nonnull
	@Override
	public PlayerBuilder getOrCreateData(UUID uuid) {
		PlayerBuilder playerBuilder = this.getPlayerCache().get(uuid);
		if (playerBuilder != null)
			return playerBuilder;
		return new PlayerBuilder.Builder().build();
	}

	@Override
	public void clearPlayerData(UUID uuid) {
		this.setPlayerData(uuid, new PlayerBuilder.Builder().build());
	}

	@Override
	public void setPlayerData(UUID uuid, PlayerBuilder playerBuilder) {
		this.getPlayerCache().put(uuid, playerBuilder);
	}
}
