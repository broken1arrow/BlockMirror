package org.brokenarrow.blockmirror.api.utility;

/**
 * Enum that handles facing in the opposite direction from where the player places the block.
 * The facing can be towards or away, depending on the block face set by the player or,
 * if not set, the direction in which the player places the block.
 */

public enum OppositeFacing {
	X_DIRECTION,
	Z_DIRECTION,
	BOTH_DIRECTIONS,
	NONE;


	public OppositeFacing next() {
		OppositeFacing[] oppositeFacings = values();
		int ordinal = this.ordinal();
		if (ordinal + 1 >= oppositeFacings.length)
			return X_DIRECTION;

		return oppositeFacings[ordinal + 1];
	}

	public OppositeFacing previous() {
		OppositeFacing[] oppositeFacings = values();
		int ordinal = this.ordinal();
		if (ordinal - 1 < 0)
			return NONE;
		return oppositeFacings[ordinal - 1];
	}
}
