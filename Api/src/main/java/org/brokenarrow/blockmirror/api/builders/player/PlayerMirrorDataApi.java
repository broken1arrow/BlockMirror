package org.brokenarrow.blockmirror.api.builders.player;

import org.brokenarrow.blockmirror.api.builders.BlockRotation;
import org.brokenarrow.blockmirror.api.builders.Distance;
import org.brokenarrow.blockmirror.api.builders.MirrorLoc;
import org.brokenarrow.blockmirror.api.utility.OppositeFacing;
import org.bukkit.Location;

import javax.annotation.Nullable;
import java.util.List;

public interface PlayerMirrorDataApi {
  List<Distance> getDistances();

  Location getCenterLocation();

  @Nullable
  BlockRotation getBlockRotation();

  boolean isReplaceBlock();

  OppositeFacing isFlipFacing();

  MirrorLoc getMirrorLoc();

  int getEffectID();

  PlayerMirrorBuilder getBuilder();
}
