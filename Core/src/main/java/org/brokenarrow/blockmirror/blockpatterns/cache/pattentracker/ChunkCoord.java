package org.brokenarrow.blockmirror.blockpatterns.cache.pattentracker;

import org.brokenarrow.blockmirror.api.builders.pattentracker.ChunkCoordinats;
import org.bukkit.Location;

import javax.annotation.Nonnull;

public class ChunkCoord implements ChunkCoordinats {

    private final int x;
    private final int z;

    public ChunkCoord(final int x, final int z) {
        this.x = x;
        this.z = z;
    }

    public static ChunkCoord of(@Nonnull final Location location) {
        final int x = location.getBlockX();
        final int z = location.getBlockZ();
        return new ChunkCoord(x >> 4, z >> 4);
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        ChunkCoord chunkCoord = (ChunkCoord) o;
        return x == chunkCoord.x && z == chunkCoord.z;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + z;
        return result;
    }
}