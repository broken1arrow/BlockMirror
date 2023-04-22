package org.brokenarrow.blockmirror.api;

import org.brokenarrow.blockmirror.api.builders.PlayerBuilder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public interface PlayerCacheApi {

	@Nullable
	PlayerBuilder getData(UUID uuid);

	@Nonnull
	PlayerBuilder getOrCreateData(UUID uuid);

	void clearPlayerData(UUID uuid);

	void setPlayerData(UUID uuid, PlayerBuilder playerBuilder);
}
