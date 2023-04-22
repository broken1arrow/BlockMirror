package org.brokenarrow.blockmirror.api.builders;

import org.bukkit.Location;

import java.util.List;

public class PlayerBuilder {

	private final Location centerLocation;
	private final List<Distance> distances;
	private final BlockRotation blockRotation;
	private final MirrorLoc mirrorLoc;
	private final int effectID;
	private final boolean patternFill;
	private final Builder builder;

	private PlayerBuilder(final Builder builder) {
		this.distances = builder.distances;
		this.centerLocation = builder.centerLocation;
		this.blockRotation = builder.blockRotation;
		this.mirrorLoc = builder.mirrorLoc;
		this.effectID = builder.effectID;
		this.patternFill = builder.patternFill;

		this.builder = builder;
	}

	public List<Distance> getDistances() {
		return distances;
	}

	public Location getCenterLocation() {
		return centerLocation;
	}

	public BlockRotation getBlockRotation() {
		return blockRotation;
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
		public boolean patternFill;

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

		public Builder setPatternFill(final boolean patternFill) {
			this.patternFill = patternFill;
			return this;
		}

		public PlayerBuilder build() {
			return new PlayerBuilder(this);
		}
	}
}
