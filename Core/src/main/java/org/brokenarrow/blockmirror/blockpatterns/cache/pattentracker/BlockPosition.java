package org.brokenarrow.blockmirror.blockpatterns.cache.pattentracker;

import org.brokenarrow.blockmirror.api.builders.pattentracker.BlockPositionData;
import org.brokenarrow.blockmirror.api.builders.pattentracker.ChunkCoordinats;
import org.bukkit.Location;

import javax.annotation.Nonnull;

public class BlockPosition implements BlockPositionData {
    private final int x;
    private final int y;
    private final int z;
    private final ChunkCoordinats cachedChunkCoord;

    public BlockPosition(@Nonnull final Location location) {
        this(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    public BlockPosition(final int x, final int y, final int z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.cachedChunkCoord = new ChunkCoord(x >> 4, z >> 4);
    }


    public static BlockPosition from(Location loc) {
        return new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    }

    public int getBlockX() {
        return x;
    }

    public int getBlockY() {
        return y;
    }

    public int getBlockZ() {
        return z;
    }

    public ChunkCoordinats getChunkCoord() {
        return cachedChunkCoord;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        BlockPosition that = (BlockPosition) o;
        return x == that.x && y == that.y && z == that.z;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        result = 31 * result + z;
        return result;
    }
}