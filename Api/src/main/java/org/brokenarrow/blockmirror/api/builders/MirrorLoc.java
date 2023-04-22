package org.brokenarrow.blockmirror.api.builders;

import java.util.ArrayList;
import java.util.List;

public final class MirrorLoc {

	List<MirrorOption> options;
	private final boolean mirrorX;
	private final boolean mirrorY;
	private final boolean mirrorZ;

	private final boolean mirrorXY;
	private final boolean mirrorZY;
	private final boolean mirrorXZ;
	private final boolean mirrorZX;

	private final boolean rotateUp_90;
	private final boolean rotateClockWise_90;
	private final boolean rotateCounterClockWise_90;
	private final boolean rotate_180;

	private final Builder builder;

	private MirrorLoc(Builder builder) {
		this.mirrorX = builder.mirrorX;
		this.mirrorY = builder.mirrorY;
		this.mirrorZ = builder.mirrorZ;
		this.mirrorXY = builder.mirrorXY;
		this.mirrorZY = builder.mirrorZY;
		this.mirrorXZ = builder.mirrorXZ;
		this.mirrorZX = builder.mirrorZX;
		this.rotateUp_90 = builder.rotateUp_90;
		this.rotateClockWise_90 = builder.rotateClockWise_90;
		this.rotateCounterClockWise_90 = builder.rotateCounterClockWise_90;
		this.rotate_180 = builder.rotate_180;
		this.options = new ArrayList<>();
		if (mirrorX)
			this.options.add(MirrorOption.MIRROR_X);
		if (mirrorZ)
			this.options.add(MirrorOption.MIRROR_Z);
		if (mirrorY)
			this.options.add(MirrorOption.MIRROR_Y);
		if (mirrorXY)
			this.options.add(MirrorOption.MIRROR_XY);
		if (mirrorZY)
			this.options.add(MirrorOption.MIRROR_ZY);
		if (mirrorZX)
			this.options.add(MirrorOption.MIRROR_ZX);
		if (mirrorXZ)
			this.options.add(MirrorOption.MIRROR_XZ);
/*		if (rotateUp_90)
			this.options.add(MirrorOption.ROTATE_UP_90);
		if (rotateClockWise_90)
			this.options.add(MirrorOption.ROTATE_CLOCKWISE_90);
		if (rotateCounterClockWise_90)
			this.options.add(MirrorOption.ROTATE_COUNTERCLOCKWISE_90);
		if (rotate_180)
			this.options.add(MirrorOption.ROTATE_180);*/
		this.builder = builder;
	}

	public List<MirrorOption> getOptions() {
		return options;
	}

	public boolean isMirrorX() {
		return mirrorX;
	}

	public boolean isMirrorY() {
		return mirrorY;
	}

	public boolean isMirrorZ() {
		return mirrorZ;
	}

	public boolean isMirrorXY() {
		return mirrorXY;
	}

	public boolean isMirrorZY() {
		return mirrorZY;
	}

	public boolean isMirrorXZ() {
		return mirrorXZ;
	}

	public boolean isMirrorZX() {
		return mirrorZX;
	}

	public boolean isRotateUp_90() {
		return rotateUp_90;
	}

	public boolean isRotateClockWise_90() {
		return rotateClockWise_90;
	}

	public boolean isRotateCounterClockWise_90() {
		return rotateCounterClockWise_90;
	}

	public boolean isRotate_180() {
		return rotate_180;
	}

	public Builder getBuilder() {
		return builder;
	}

	public static class Builder {

		private boolean mirrorX;
		private boolean mirrorY;
		private boolean mirrorZ;

		private boolean mirrorXY;
		private boolean mirrorZY;
		private boolean mirrorXZ;
		private boolean mirrorZX;

		private boolean rotateUp_90;
		private boolean rotateClockWise_90;
		private boolean rotateCounterClockWise_90;
		private boolean rotate_180;

		public Builder setMirrorX(final boolean mirrorX) {
			this.mirrorX = mirrorX;
			return this;
		}

		public Builder setMirrorY(final boolean mirrorY) {
			this.mirrorY = mirrorY;
			return this;
		}

		public Builder setMirrorZ(final boolean mirrorZ) {
			this.mirrorZ = mirrorZ;
			return this;
		}

		public Builder setMirrorXY(final boolean mirrorXY) {
			this.mirrorXY = mirrorXY;
			return this;
		}

		public Builder setMirrorZY(final boolean mirrorZY) {
			this.mirrorZY = mirrorZY;
			return this;
		}

		public Builder setMirrorXZ(final boolean mirrorXZ) {
			this.mirrorXZ = mirrorXZ;
			return this;
		}

		public Builder setMirrorZX(final boolean mirrorZX) {
			this.mirrorZX = mirrorZX;
			return this;
		}

		public Builder setRotateUp_90(final boolean rotateUp_90) {
			this.rotateUp_90 = rotateUp_90;
			return this;
		}

		public Builder setRotateClockWise_90(final boolean rotateClockWise_90) {
			this.rotateClockWise_90 = rotateClockWise_90;
			return this;
		}

		public Builder setRotateCounterClockWise_90(final boolean rotateCounterClockWise_90) {
			this.rotateCounterClockWise_90 = rotateCounterClockWise_90;
			return this;
		}

		public Builder setRotate_180(final boolean rotate_180) {
			this.rotate_180 = rotate_180;
			return this;
		}

		public MirrorLoc build() {
			return new MirrorLoc(this);
		}
	}

	@Override
	public String toString() {
		return "MirrorLoc{" +
				"mirrorX=" + mirrorX +
				", mirrorY=" + mirrorY +
				", mirrorZ=" + mirrorZ +
				", mirrorXY=" + mirrorXY +
				", mirrorZY=" + mirrorZY +
				", mirrorXZ=" + mirrorXZ +
				", mirrorZX=" + mirrorZX +
				", rotateUp_90=" + rotateUp_90 +
				", rotateClockWise_90=" + rotateClockWise_90 +
				", rotateCounterClockWise_90=" + rotateCounterClockWise_90 +
				", rotate_180=" + rotate_180 +
				", builder=" + builder +
				'}';
	}
}
