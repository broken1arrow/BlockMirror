package org.brokenarrow.blockmirror.utily;

import org.brokenarrow.blockmirror.api.builders.BlockRotation;
import org.brokenarrow.blockmirror.api.builders.PlayerBuilder;
import org.brokenarrow.menu.library.utility.ServerVersion;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Orientable;
import org.bukkit.block.data.Rotatable;

public class BlockPlacements {

	
	public static void setDirection(PlayerBuilder data, final Block blockToPlace, final Block placedBlock) {
		if (ServerVersion.atLeast(ServerVersion.v1_12)) {
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
			if (blockData instanceof Directional) {
				Directional directional = (Directional) blockData;
				BlockRotation convertRotation = data.getBlockRotation() != null ? data.getBlockRotation().convertRotation(false) : null;
				directional.setFacing(convertRotation != null ? convertRotation.getBlockFace() : ((Directional) blockData).getFacing());

				setBlock(placedBlock, blockToPlace, directional);
			} else if (blockData instanceof Orientable) {
				Orientable directional = (Orientable) blockData;
				BlockRotation convertRotation = data.getBlockRotation() != null ? data.getBlockRotation().convertRotation(true) : null;
				directional.setAxis(convertRotation != null ? convertRotation.getAxis() : ((Orientable) blockData).getAxis());

				setBlock(placedBlock, blockToPlace, directional);
			} else if (blockData instanceof Rotatable) {
				Rotatable directional = (Rotatable) blockData;
				BlockRotation convertRotation = data.getBlockRotation() != null ? data.getBlockRotation().convertRotation(false) : null;
				directional.setRotation(convertRotation != null ? convertRotation.getBlockFace() : ((Rotatable) blockData).getRotation());

				setBlock(placedBlock, blockToPlace, directional);
			} else {
				blockToPlace.setType(placedBlock.getType());
			}
		}
	}

}
