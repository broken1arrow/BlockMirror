package org.brokenarrow.blockmirror.api.builders;

import org.brokenarrow.blockmirror.api.builders.pattentracker.ChunkLocationCollection;
import org.bukkit.Location;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

public interface PatternTracker {
    Map<UUID, ChunkLocationCollection> getPatterns();

    void addPattern(@Nonnull final Location location);

    void addPatterns(Collection<Location> locations);

    boolean removePattern(@Nonnull final Collection<Location> location);

    boolean containsPattern(@Nonnull final  Location location);

    boolean containsPatterns(@Nonnull final Collection<Location> locations);

}
