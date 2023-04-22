package org.brokenarrow.blockmirror.api.builders;

import org.bukkit.Axis;
import org.bukkit.block.BlockFace;

import javax.annotation.Nonnull;

public class BlockRotation {

	private final String rotation;
	private Axis axis;
	private BlockFace blockFace;

	public BlockRotation(@Nonnull final String rotation) {
		this.rotation = rotation.toUpperCase();
	}

	public String getRotation() {
		return rotation;
	}

	public Axis getAxis() {
		return axis;
	}
	
	public BlockFace getBlockFace() {
		return blockFace;
	}

	public BlockRotation convertRotation(boolean useAxis) {

		if (useAxis) {
			if (this.getRotation().equals("WEST") || this.getRotation().equals("EAST"))
				this.axis = Axis.X;
			if (this.getRotation().equals("UP") || this.getRotation().equals("DOWN"))
				this.axis = Axis.Y;
			if (this.getRotation().equals("NORTH") || this.getRotation().equals("SOUTH"))
				this.axis = Axis.Z;
			else
				this.axis = Axis.Y;
		} else {
			BlockFace[] blockFaces = BlockFace.values();
			for (BlockFace blockFace : blockFaces) {
				if (this.getRotation().equals(blockFace.name())) {
					this.blockFace = blockFace;
				}
			}
		}
		return this;
	}

}
