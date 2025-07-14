package org.brokenarrow.blockmirror.api.builders;

import org.bukkit.Location;

import java.util.Collection;

public interface ChunkLocationCollection {
    
    void add(Location location);

    void addAll(Collection<Location> locations);
    boolean remove(Location location);
    boolean removeAll(Collection<Location> location);

    boolean contains(Location location);

    boolean containsAny(Collection<Location> locations);
}
