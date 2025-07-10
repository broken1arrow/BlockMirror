package org.brokenarrow.blockmirror.api.utility;


import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public interface BlockVisualizeAPI {

    void visulizeBlock(final Block block, final Location location, final boolean shallBeVisualize);

    void visulizeBlock(final Player player, final Block block, final Location location, final boolean shallBeVisualize);

    Material getMaterial();

}
