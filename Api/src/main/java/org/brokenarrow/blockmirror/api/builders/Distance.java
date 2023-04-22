package org.brokenarrow.blockmirror.api.builders;

public class Distance {

	private final int distanceZ;
	private final int distanceX;
	private final int distanceY;

	public Distance(final int distanceZ, final int distanceX, final int distanceY) {
		this.distanceZ = distanceZ;
		this.distanceX = distanceX;
		this.distanceY = distanceY;
	}

	public int getDistanceZ() {
		return distanceZ;
	}

	public int getDistanceX() {
		return distanceX;
	}

	public int getDistanceY() {
		return distanceY;
	}
}
