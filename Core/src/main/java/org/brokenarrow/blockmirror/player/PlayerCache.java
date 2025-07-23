package org.brokenarrow.blockmirror.player;

import org.brokenarrow.blockmirror.api.PlayerCacheApi;
import org.brokenarrow.blockmirror.api.builders.player.PlayerMirrorDataApi;
import org.brokenarrow.blockmirror.utily.EffectsActivated;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerCache implements PlayerCacheApi {

    private final Map<UUID, PlayerMirrorDataApi> playerCache = new HashMap<>();

    private Map<UUID, PlayerMirrorDataApi> getPlayerCache() {
        return playerCache;
    }

    @Override
    @Nullable
    public PlayerMirrorDataApi getData(UUID uuid) {
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
    public PlayerMirrorDataApi getOrCreateData(UUID uuid) {
        PlayerMirrorDataApi playerBuilder = this.getPlayerCache().get(uuid);
        if (playerBuilder != null)
            return playerBuilder;
        return new PlayerMirrorData.Builder().build();
    }

    @Override
    public void clearPlayerData(UUID uuid) {
        PlayerMirrorDataApi playerData = this.getData(uuid);
        if (playerData != null) {
            EffectsActivated.removeEffect(uuid, playerData);
        }
        this.setPlayerData(uuid, new PlayerMirrorData.Builder().build());
    }

    @Override
    public void setPlayerData(UUID uuid, PlayerMirrorDataApi playerBuilder) {
        this.getPlayerCache().put(uuid, playerBuilder);
    }
}
