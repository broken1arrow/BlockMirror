package org.brokenarrow.blockmirror.api.utility;


import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Set;

public interface BlockVisualizeAPI {

    void visulizeBlock(@Nonnull final Set<Player> playersAllowed, final Block block, final boolean shallBeVisualize);

    void visulizeBlock(@Nonnull final Player player, final Block block, final boolean shallBeVisualize);

    Material getMaterial();

}
