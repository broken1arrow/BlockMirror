package org.brokenarrow.blockmirror.api.utility;

public enum Rotation {
	DOWN((byte) 0),
	UP((byte) 1),
	NORTH((byte) 2),
	SOUTH((byte) 3),
	WEST((byte) 4),
	EAST((byte) 5);

	private final byte value;

	Rotation(byte value) {
		this.value = value;
	}

	public byte getValue() {
		return value;
	}

	public static Rotation getType(String type) {
		if (type == null) return UP;
		type = type.toUpperCase();
		for (Rotation rotation : Rotation.values()) {
			if (type.equals(rotation.name()))
				return rotation;
		}
		return UP;
	}

}