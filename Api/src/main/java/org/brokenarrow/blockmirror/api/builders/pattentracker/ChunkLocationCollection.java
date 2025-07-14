package org.brokenarrow.blockmirror.api.builders.pattentracker;

import org.bukkit.Location;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface ChunkLocationCollection {
    
    void add(Location location);

    void addAll(Collection<Location> locations);

    boolean remove(Location location);

    boolean removeAll(Collection<Location> location);

    boolean contains(Location location);

    boolean containsAny(Collection<Location> locations);

    Map<ChunkCoordinats, Set<BlockPositionData>> getData();

    boolean isEmpty();

    int size();

    Collection<BlockPositionData> all();
}
