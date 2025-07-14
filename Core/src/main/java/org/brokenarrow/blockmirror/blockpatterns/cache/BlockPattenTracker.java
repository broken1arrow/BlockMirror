package org.brokenarrow.blockmirror.blockpatterns.cache;

import org.brokenarrow.blockmirror.api.builders.ChunkLocationCollection;
import org.brokenarrow.blockmirror.api.builders.PatternTracker;
import org.brokenarrow.blockmirror.blockpatterns.cache.pattentracker.ChunkLocationCache;
import org.bukkit.Location;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BlockPattenTracker implements PatternTracker {

    private final Map<UUID, ChunkLocationCollection> patterns = new HashMap<>();

    @Override
    public Map<UUID, ChunkLocationCollection> getPatterns() {
        return patterns;
    }

    @Override
    public void addPattern(@Nonnull final Location location) {
        final UUID worldUid = getWorldUid(location);
        if (worldUid == null)
            return;
        patterns.computeIfAbsent(worldUid, uuid -> new ChunkLocationCache())
                .add(location);

    }

    @Override
    public void addPatterns(Collection<Location> locations) {
        final UUID worldUid = getWorldUidFromCollection(locations);
        if (worldUid == null)
            return;
        patterns.computeIfAbsent(worldUid, uuid -> new ChunkLocationCache())
                .addAll(locations);

    }

    @Override
    public boolean removePattern(@Nonnull final Collection<Location> locations) {
        final UUID worldUid = getWorldUidFromCollection(locations);
        ChunkLocationCollection chunkLocationCollection = patterns.get(worldUid);
        if (chunkLocationCollection == null)
            return false;
        return chunkLocationCollection.removeAll(locations);
    }

    @Override
    public boolean containsPattern(@Nonnull final Location location) {
        ChunkLocationCollection chunkLocationCollection = patterns.get(getWorldUid(location));
        if (chunkLocationCollection == null)
            return false;
        return chunkLocationCollection.contains(location);
    }

    @Override
    public boolean containsPatterns(@Nonnull final Collection<Location> locations) {
        return locations.stream().anyMatch(this::containsPattern);
    }

    @Nullable
    private UUID getWorldUid(@Nonnull Location location) {
        if (location.getWorld() == null)
            return null;
        return location.getWorld().getUID();
    }

    @Nullable
    private UUID getWorldUidFromCollection(@Nonnull Collection<Location> locations) {
        return locations.stream().findAny().map(location -> {
            if (location.getWorld() == null)
                return null;
            return location.getWorld().getUID();
        }).orElse(null);
    }
}
