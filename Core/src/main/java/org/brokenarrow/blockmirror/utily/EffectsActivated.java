package org.brokenarrow.blockmirror.utily;

import org.brokenarrow.blockmirror.BlockMirror;
import org.brokenarrow.blockmirror.api.builders.PlayerBuilder;
import org.brokenarrow.blockmirror.api.utility.BlockVisualizeAPI;
import org.brokenarrow.blockmirror.api.utility.BossBarAPI;
import org.brokenarrow.blockmirror.utily.effects.SpawnBorderEffects;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public class EffectsActivated {
    private static final BlockVisualizeAPI blockVisualize = BlockMirror.getPlugin().getBlockVisualize();
    private static final BossBarAPI bar = BlockMirror.getPlugin().getBar();

    private EffectsActivated() {

    }

    public static void setEffect(final Player player, final PlayerBuilder data, final PlayerBuilder.Builder builder) {
        setEffect(player, data, builder, "Mirror aktive");
    }

    public static void setEffect(final Player player, final PlayerBuilder data, final PlayerBuilder.Builder builder, boolean newLocation) {
        setEffect(player, data, builder, "Mirror aktive", newLocation);
    }


    public static void setEffect(Player player, PlayerBuilder data, PlayerBuilder.Builder builder, String message) {
        setEffect(player, data, builder, message, true);
    }

    public static void setEffect(Player player, PlayerBuilder data, PlayerBuilder.Builder builder, String message, boolean newLocation) {
        Location loc = data.getCenterLocation();
        if (loc != null) {
            blockVisualize.stopVisualizing(loc.getBlock());
            BlockMirror.getPlugin().getRunTask().setQueueTime(data.getEffectID(), -2);
        }
        if (newLocation)
            loc = player.getLocation();
        if (loc == null)
            return;
        blockVisualize.visulizeBlock(player, loc.getBlock(), true);
        SpawnBorderEffects spawnBorderEffects = new SpawnBorderEffects(null, player, loc, -1, 2.5);
        builder.setEffectID(spawnBorderEffects.getID());
        BlockMirror.getPlugin().getRunTask().addTask(spawnBorderEffects);
        bar.sendBossBar(player, message);

    }

    public static void removeEffect(final Player player, final PlayerBuilder data) {
        Location loc = data.getCenterLocation();
        if (loc == null) {
            loc = player.getLocation();
        }

        blockVisualize.stopVisualizing(loc.getBlock());
        BlockMirror.getPlugin().getRunTask().setQueueTime(data.getEffectID(), -2);
        bar.hideFromPlayer(player);
    }
    public static void removeEffect(final UUID uuid, final PlayerBuilder data) {
        Location loc = data.getCenterLocation();
        if (loc == null) {
          return;
        }
        blockVisualize.stopVisualizing(loc.getBlock());
        BlockMirror.getPlugin().getRunTask().setQueueTime(data.getEffectID(), -2);
        bar.hideFromPlayer(uuid);
    }

}
