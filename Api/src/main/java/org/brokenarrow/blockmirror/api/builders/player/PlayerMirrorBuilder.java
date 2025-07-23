package org.brokenarrow.blockmirror.api.builders.player;

import org.brokenarrow.blockmirror.api.builders.BlockRotation;
import org.brokenarrow.blockmirror.api.builders.Distance;
import org.brokenarrow.blockmirror.api.builders.MirrorLoc;
import org.brokenarrow.blockmirror.api.utility.OppositeFacing;
import org.bukkit.Location;

import java.util.List;

public interface PlayerMirrorBuilder {

  PlayerMirrorBuilder setCenterLocation(final Location location);

  PlayerMirrorBuilder setEffectID(final int id);

  PlayerMirrorBuilder setBlockRotation(final BlockRotation blockRotation);

  PlayerMirrorBuilder setDistances(final List<Distance> distances);

  PlayerMirrorBuilder setReplaceBlock(final boolean replaceBlock);

  PlayerMirrorBuilder setFlipFacing(final OppositeFacing facingMode);

  PlayerMirrorBuilder setMirrorLoc(final MirrorLoc mirrorLoc);

  PlayerMirrorDataApi build();


}
