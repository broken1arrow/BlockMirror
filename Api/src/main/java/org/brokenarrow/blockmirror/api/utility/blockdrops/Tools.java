package org.brokenarrow.blockmirror.api.utility.blockdrops;

import org.bukkit.Material;

public enum Tools {

	AXE,
	HOE,
	SHOVEL,
	PICKAXE,
	SHEARS;


	public static Tools getTool(Material material) {
		if (material.name().endsWith("_PICKAXE"))
			return PICKAXE;
		if (material.name().endsWith("_SHOVEL"))
			return SHOVEL;
		if (material.name().endsWith("_HOE"))
			return HOE;
		if (material.name().endsWith("_AXE"))
			return AXE;
		if (material == Material.SHEARS)
			return SHEARS;
		return null;
	}
}
