package org.brokenarrow.blockmirror.api.builders;

import org.brokenarrow.blockmirror.api.utility.Rotation;
import org.bukkit.Axis;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Slab.Type;

import javax.annotation.Nonnull;

public class BlockRotation {

	private final String rotation;
	private Axis axis;
	private BlockFace blockFace;
	private Type blockType;

	public BlockRotation(@Nonnull final String rotation) {
		this.rotation = rotation.toUpperCase();
	}

	public static BlockRotation of(final Axis axis) {
		BlockRotation rotation = new BlockRotation(axis.name());
		rotation.axis = axis;
		return rotation;
	}

	public static BlockRotation of(final BlockFace faceing) {
		BlockRotation blockRotation = new BlockRotation(faceing.name());
		blockRotation.blockFace = faceing;
		return blockRotation;
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

	public Type getHalfBlockHight() {
		return blockType;
	}

	public BlockRotation convertRotation(boolean useAxis) {
		return convertRotation(useAxis, false);
	}

	public BlockRotation convertRotation(boolean useAxis, boolean isSlab) {
		if (useAxis) {
			Rotation rotation = Rotation.getType(this.getRotation());
			switch (rotation) {
				case WEST:
				case EAST:
					this.axis = Axis.X;
					break;
				case NORTH:
				case SOUTH:
					this.axis = Axis.Z;
					break;
				case UP:
				case DOWN:
				default:
					this.axis = Axis.Y;
					break;
			}
		} else {
			if (isSlab) {
				convertSlabRotation();
			} else {
				BlockFace[] blockFaces = BlockFace.values();
				for (BlockFace blockFace : blockFaces) {
					if (this.getRotation().equals(blockFace.name())) {
						this.blockFace = blockFace;
					}
				}
			}
		}
		return this;
	}

	public BlockRotation convertSlabRotation() {
		Rotation rotation = Rotation.getType(this.getRotation());
		switch (rotation) {
			case DOWN:
				this.blockType = Type.BOTTOM;
				break;
			case WEST:
			case EAST:
			case NORTH:
			case SOUTH:
			case UP:
			default:
				this.blockType = Type.TOP;
				break;
		}
		return this;
	}

	//dropper down 0
	//dropper up 1
	//dropper north 2
	//dropper south 3
	//dropper west 4
	//dropper east 5
	//log y 0
	//log x 4
	//log z 8
	//torch wall east 1
	//torch wall west 2
	//torch wall south 3
	//torch wall north 4
	//torch block 5

	/**
	 * This is uesd for old mincraft versions that use byte insted of blockfaces.
	 *
	 * @param material the matrial to check.
	 * @return return the byte number
	 */
	public byte convertRotation(Material material) {
		if (material == null) {
			return 0;
		}
		String name = material.name();
		if (name.equals("DROPPER") || name.equals("DISPENSER") || name.startsWith("PISTON")) {
			Rotation rotation = Rotation.getType(this.getRotation());
			return rotation.getValue();
		}
		if (name.contains("LOG")) {
			Rotation rotation = Rotation.getType(this.getRotation());
			switch (rotation) {
				case WEST:
				case EAST:
					return 4;
				case NORTH:
				case SOUTH:
					return 8;
				case UP:
				case DOWN:
				default:
					return 0;
			}
		}
		if (name.contains("TORCH")) {
			Rotation rotation = Rotation.getType(this.getRotation());
			switch (rotation) {
				case EAST:
					return 1;
				case WEST:
					return 2;
				case SOUTH:
					return 3;
				case NORTH:
					return 4;
				default:
					return 5;
			}
		}
		return 0;
	}

}
