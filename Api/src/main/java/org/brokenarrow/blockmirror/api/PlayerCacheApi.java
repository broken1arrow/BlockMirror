package org.brokenarrow.blockmirror.api;

import org.brokenarrow.blockmirror.api.builders.player.PlayerMirrorDataApi;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public interface PlayerCacheApi {

	@Nullable
	PlayerMirrorDataApi getData(UUID uuid);

	@Nonnull
	PlayerMirrorDataApi  getOrCreateData(UUID uuid);

	void clearPlayerData(UUID uuid);

	void setPlayerData(UUID uuid, PlayerMirrorDataApi  PlayerMirrorDataApi);
}
