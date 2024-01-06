package org.brokenarrow.blockmirror.utily;

import org.broken.arrow.menu.library.utility.ServerVersion;
import org.brokenarrow.blockmirror.api.builders.BlockRotation;
import org.brokenarrow.blockmirror.api.builders.PlayerBuilder;
import org.brokenarrow.blockmirror.api.utility.OppositeFacing;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Orientable;
import org.bukkit.block.data.Rotatable;
import org.bukkit.block.data.type.Slab;

public class BlockPlacements {

	public static void setDirection(PlayerBuilder data, final Block blockToPlace, final Block placedBlock) {
		if (ServerVersion.atLeast(ServerVersion.V1_12)) {
			new BlockPlace(data).setDirection(blockToPlace, placedBlock);
		} else {
			byte bytes = data.getBlockRotation() != null ? data.getBlockRotation().convertRotation(placedBlock.getType()) : placedBlock.getData();
			blockToPlace.setType(placedBlock.getType());
			BlockState directional = blockToPlace.getState();
			directional.setRawData(bytes);
			directional.update(true);
		}
	}

	public static void setBlock(Block placedBlock, final Block blockToPlace, final BlockData blockData) {
		blockToPlace.setType(placedBlock.getType());
		blockToPlace.setBlockData(blockData);
	}

	public static class BlockPlace {
		private final PlayerBuilder data;

		public BlockPlace(PlayerBuilder data) {
			this.data = data;

		}

		public void setDirection(final Block blockToPlace, final Block placedBlock) {
			BlockData blockData = placedBlock.getBlockData();
			Location loc = blockToPlace.getLocation();
			Location locPlace = placedBlock.getLocation();
			if (blockData instanceof Directional) {
				Directional directional = (Directional) blockData;
				BlockRotation convertRotation = data.getBlockRotation() != null ? data.getBlockRotation().convertRotation(false) : null;
				BlockFace facing = convertRotation != null ? convertRotation.getBlockFace() : directional.getFacing();

				/*		if (data.isFlipPlacement())*/
				// x direction.
				if (data.isFlipFacing() == OppositeFacing.X_DIRECTION || data.isFlipFacing() == OppositeFacing.BOTH_DIRECTIONS) {
					if (loc.getBlockZ() == locPlace.getBlockZ() && loc.getBlockX() != locPlace.getBlockX()) //||(loc.getBlockZ() != locPlace.getBlockZ() && loc.getBlockX() != locPlace.getBlockX()))
						facing = facing.getOppositeFace();
				}
				// z direction.
				if (data.isFlipFacing() == OppositeFacing.Z_DIRECTION || data.isFlipFacing() == OppositeFacing.BOTH_DIRECTIONS) {
					if (loc.getBlockZ() != locPlace.getBlockZ() && loc.getBlockX() != locPlace.getBlockX())
						/*	if ((loc.getBlockZ() != locPlace.getBlockZ() && loc.getBlockX() == locPlace.getBlockX()) || (loc.getBlockZ() != locPlace.getBlockZ() && loc.getBlockX() != locPlace.getBlockX()))*/
						facing = facing.getOppositeFace();
				}

				directional.setFacing(facing);
				setBlock(placedBlock, blockToPlace, directional);
			} else if (blockData instanceof Orientable) {
				Orientable directional = (Orientable) blockData;
				BlockRotation convertRotation = data.getBlockRotation() != null ? data.getBlockRotation().convertRotation(true) : null;
				directional.setAxis(convertRotation != null ? convertRotation.getAxis() : directional.getAxis());

				setBlock(placedBlock, blockToPlace, directional);
			} else if (blockData instanceof Rotatable) {
				Rotatable rotatable = (Rotatable) blockData;
				BlockRotation convertRotation = data.getBlockRotation() != null ? data.getBlockRotation().convertRotation(false) : null;
				rotatable.setRotation(convertRotation != null ? convertRotation.getBlockFace() : rotatable.getRotation());

				setBlock(placedBlock, blockToPlace, rotatable);
			} else if (blockData instanceof Slab) {
				Slab slabData = (Slab) blockData;
				BlockRotation convertRotation = data.getBlockRotation() != null ? data.getBlockRotation().convertRotation(false, true) : null;
				slabData.setType(convertRotation != null ? convertRotation.getHalfBlockHight() : slabData.getType());
				setBlock(placedBlock, blockToPlace, slabData);
			} else {
				blockToPlace.setType(placedBlock.getType());
			}
		}
	}

}
