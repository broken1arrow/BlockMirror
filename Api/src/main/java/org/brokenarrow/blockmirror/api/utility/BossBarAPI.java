package org.brokenarrow.blockmirror.api.utility;

import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.UUID;

public interface BossBarAPI {

    void sendBossBar(@Nonnull final Player player, final String mirrorAktive);

    void hideFromPlayer(@Nonnull final Player player);

    void hideFromPlayer(@Nonnull UUID uuid);
}
