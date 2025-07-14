package org.brokenarrow.blockmirror.blockpatterns.cache.pattentracker;

import org.brokenarrow.blockmirror.api.builders.ChunkLocationCollection;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ChunkLocationCache implements ChunkLocationCollection {

    private final Map<ChunkCoord, Set<BlockPosition>> data = new HashMap<>();

    @Override
    public void add(Location location) {
        if (location.getBlock().getType() != Material.AIR)
            return;
        BlockPosition blockPosition = new BlockPosition(location);
        data.computeIfAbsent(blockPosition.getChunkCoord(), chunkCoord -> new HashSet<>())
                .add(blockPosition);
    }

    @Override
    public void addAll(Collection<Location> locations) {
        locations.forEach(location -> {
            if (location.getBlock().getType() != Material.AIR)
                return;
            BlockPosition blockPosition = new BlockPosition(location);
            data.computeIfAbsent(blockPosition.getChunkCoord(), chunkCoord -> new HashSet<>())
                    .add(blockPosition);
        });
    }

    @Override
    public boolean remove(Location location) {
        final BlockPosition blockPosition = new BlockPosition(location);
        final ChunkCoord chunkCoord = blockPosition.getChunkCoord();
        Set<BlockPosition> blockPositions = data.get(chunkCoord);
        if (blockPositions == null) return false;

        boolean removed = blockPositions.remove(blockPosition);
        if (blockPositions.isEmpty()) {
            data.remove(chunkCoord);
        }
        return removed;
    }


    @Override
    public boolean removeAll(Collection<Location> locations) {
        boolean changed = false;

        for (Location location : locations) {
            final BlockPosition blockPosition = new BlockPosition(location);
            final ChunkCoord chunkCoord = blockPosition.getChunkCoord();

            Set<BlockPosition> blockPositions = data.get(chunkCoord);
            if (blockPositions != null && blockPositions.remove(blockPosition)) {
                changed = true;
                if (blockPositions.isEmpty()) {
                    data.remove(chunkCoord);
                }
            }
        }
        return changed;
    }

    @Override
    public boolean contains(Location location) {
        final BlockPosition blockPosition = new BlockPosition(location);
        final ChunkCoord chunkCoord = blockPosition.getChunkCoord();
        final Set<BlockPosition> blockPositions = data.get(chunkCoord);
        if (blockPositions == null) return false;

        return blockPositions.contains(blockPosition);
    }

    @Override
    public boolean containsAny(final Collection<Location> locations) {
        return locations.stream().anyMatch(this::contains);
    }

    public Map<ChunkCoord, Set<BlockPosition>> getData() {
        return Collections.unmodifiableMap(data);
    }

    public boolean isEmpty() {
        return data.isEmpty();
    }

    public int size() {
        return data.size();
    }

    public Collection<BlockPosition> all() {
        return data.values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }
}
