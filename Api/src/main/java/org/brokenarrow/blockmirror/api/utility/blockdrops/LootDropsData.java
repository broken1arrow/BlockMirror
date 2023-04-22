package org.brokenarrow.blockmirror.api.utility.blockdrops;

import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

public class LootDropsData {

	private final Map<Material, DropData> lootDrops = new HashMap<>();

	public LootDropsData() {
	}

	public DropData getLootDrops(Material material) {
		return lootDrops.get(material);
	}

	public Map<Material, DropData> getLootDrops() {
		return lootDrops;
	}

	public LootDropsData setLootDrop(Material material, DropData dropData) {
		lootDrops.put(material, dropData);
		return this;
	}

}
