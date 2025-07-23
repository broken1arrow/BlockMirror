package org.brokenarrow.blockmirror.player;

import org.brokenarrow.blockmirror.api.builders.BlockRotation;
import org.brokenarrow.blockmirror.api.builders.Distance;
import org.brokenarrow.blockmirror.api.builders.MirrorLoc;
import org.brokenarrow.blockmirror.api.builders.player.PlayerMirrorBuilder;
import org.brokenarrow.blockmirror.api.builders.player.PlayerMirrorDataApi;
import org.brokenarrow.blockmirror.api.utility.OppositeFacing;
import org.bukkit.Location;

import javax.annotation.Nullable;
import java.util.List;

public class PlayerMirrorData implements PlayerMirrorDataApi {

  private final Location centerLocation;
  private final List<Distance> distances;
  private final BlockRotation blockRotation;
  private final MirrorLoc mirrorLoc;
  private final OppositeFacing flipFacing;
  private final int effectID;
  private final boolean replaceBlock;
  private final Builder builder;

  private PlayerMirrorData(final Builder builder) {
    this.distances = builder.distances;
    this.centerLocation = builder.centerLocation;
    this.blockRotation = builder.blockRotation;
    this.mirrorLoc = builder.mirrorLoc;
    this.effectID = builder.effectID;
    this.replaceBlock = builder.replaceBlock;
    this.flipFacing = builder.flipFacing;

    this.builder = builder;
  }

  @Override
  public List<Distance> getDistances() {
    return distances;
  }

  @Override
  public Location getCenterLocation() {
    return centerLocation;
  }

  @Nullable
  @Override
  public BlockRotation getBlockRotation() {
    return blockRotation;
  }

  @Override
  public boolean isReplaceBlock() {
    return replaceBlock;
  }

  @Override
  public OppositeFacing isFlipFacing() {
    return flipFacing;
  }

  @Override
  public MirrorLoc getMirrorLoc() {
    return mirrorLoc;
  }

  @Override
  public int getEffectID() {
    return effectID;
  }

  @Override
  public PlayerMirrorBuilder getBuilder() {
    return builder;
  }

  public static class Builder implements PlayerMirrorBuilder {
    public MirrorLoc mirrorLoc;
    public boolean replaceBlock;
    private List<Distance> distances;
    private BlockRotation blockRotation;
    private Location centerLocation;
    private int effectID;
    private OppositeFacing flipFacing = OppositeFacing.NONE;

    @Override
    public Builder setDistances(final List<Distance> distances) {
      this.distances = distances;
      return this;
    }

    @Override
    public Builder setBlockRotation(final BlockRotation blockRotation) {
      this.blockRotation = blockRotation;
      return this;
    }

    @Override
    public Builder setMirrorLoc(final MirrorLoc mirrorLoc) {
      this.mirrorLoc = mirrorLoc;
      return this;
    }

    @Override
    public Builder setCenterLocation(final Location centerLocation) {
      this.centerLocation = centerLocation;
      return this;
    }

    @Override
    public Builder setEffectID(final int effectID) {
      this.effectID = effectID;
      return this;
    }

    @Override
    public Builder setReplaceBlock(final boolean replaceBlock) {
      this.replaceBlock = replaceBlock;
      return this;
    }

    @Override
    public Builder setFlipFacing(final OppositeFacing flipFacing) {
      this.flipFacing = flipFacing;
      return this;
    }

    @Override
    public PlayerMirrorDataApi build() {
      return new PlayerMirrorData(this);
    }

  }
}