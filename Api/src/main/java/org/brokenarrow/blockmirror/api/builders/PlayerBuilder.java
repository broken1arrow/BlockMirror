package org.brokenarrow.blockmirror.api.builders;

import org.brokenarrow.blockmirror.api.utility.OppositeFacing;
import org.bukkit.Location;

import javax.annotation.Nullable;
import java.util.List;

public class PlayerBuilder {

	private final Location centerLocation;
	private final List<Distance> distances;
	private final BlockRotation blockRotation;
	private final MirrorLoc mirrorLoc;
	private final OppositeFacing flipFacing;
	private final int effectID;
	private final boolean replaceBlock;
	private final Builder builder;

	private PlayerBuilder(final Builder builder) {
		this.distances = builder.distances;
		this.centerLocation = builder.centerLocation;
		this.blockRotation = builder.blockRotation;
		this.mirrorLoc = builder.mirrorLoc;
		this.effectID = builder.effectID;
		this.replaceBlock = builder.replaceBlock;
		this.flipFacing = builder.flipFacing;
		
		this.builder = builder;
	}

	public List<Distance> getDistances() {
		return distances;
	}

	public Location getCenterLocation() {
		return centerLocation;
	}

	@Nullable
	public BlockRotation getBlockRotation() {
		return blockRotation;
	}

	public boolean isReplaceBlock() {
		return replaceBlock;
	}

	public OppositeFacing isFlipFacing() {
		return flipFacing;
	}

	public MirrorLoc getMirrorLoc() {
		return mirrorLoc;
	}

	public int getEffectID() {
		return effectID;
	}

	public Builder getBuilder() {
		return builder;
	}

	public static class Builder {
		private List<Distance> distances;
		private BlockRotation blockRotation;
		public MirrorLoc mirrorLoc;
		private Location centerLocation;
		private int effectID;
		public boolean replaceBlock;
		private OppositeFacing flipFacing = OppositeFacing.NONE;

		public Builder setDistances(final List<Distance> distances) {
			this.distances = distances;
			return this;
		}


		public Builder setBlockRotation(final BlockRotation blockRotation) {
			this.blockRotation = blockRotation;
			return this;
		}

		public Builder setMirrorLoc(final MirrorLoc mirrorLoc) {
			this.mirrorLoc = mirrorLoc;
			return this;
		}

		public Builder setCenterLocation(final Location centerLocation) {
			this.centerLocation = centerLocation;
			return this;
		}

		public Builder setEffectID(final int effectID) {
			this.effectID = effectID;
			return this;
		}

		public Builder setReplaceBlock(final boolean replaceBlock) {
			this.replaceBlock = replaceBlock;
			return this;
		}

		public void setFlipFacing(final OppositeFacing flipFacing) {
			this.flipFacing = flipFacing;
		}

		public PlayerBuilder build() {
			return new PlayerBuilder(this);
		}
	}
}
