package org.brokenarrow.blockmirror.api.builders;


import org.brokenarrow.blockmirror.api.filemanger.ConfigurationSerializeUtility;
import org.bukkit.Material;

import javax.annotation.Nonnull;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ItemWrapper implements ConfigurationSerializeUtility {

	private final Material material;
	private final String displayName;
	private final List<String> lore;
	private final boolean glow;
	private final Builder builder;

	private ItemWrapper(Builder builder) {
		this.material = builder.material;
		this.displayName = builder.displayName;
		this.lore = builder.lore;
		this.glow = builder.glow;

		this.builder = builder;
	}

	public Material getMaterial() {
		return material;
	}

	public String getDisplayName() {
		return displayName;
	}

	public List<String> getLore() {
		return lore;
	}

	public boolean isGlow() {
		return glow;
	}

	public Builder getBuilder() {
		return builder;
	}

	public static class Builder {
		private Material material;
		private String displayName;
		private List<String> lore;
		private boolean glow;

		public Builder setMaterial(final Material material) {
			this.material = material;
			return this;
		}

		public Builder setDisplayName(final String displayName) {
			this.displayName = displayName;
			return this;
		}

		public Builder setLore(final List<String> lore) {
			this.lore = lore;
			return this;
		}

		public Builder setGlow(final boolean glow) {
			this.glow = glow;
			return this;
		}

		public ItemWrapper build() {
			return new ItemWrapper(this);
		}
	}

	@Nonnull
	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("Material", material + "");
		map.put("Display_name", displayName);
		map.put("Lore", lore);
		return map;
	}

	public static ItemWrapper deserialize(Map<String, Object> map) {
		String name = (String) map.getOrDefault("Material", null);
		Material material = Material.ACACIA_SLAB;
		if (name != null) material = Material.valueOf(name);

		String displayName = (String) map.get("Display_name");
		List<String> lore = (List<String>) map.get("Lore");
		boolean isGlow = (boolean) map.getOrDefault("Glow", false);
		Builder builder = new Builder()
				.setMaterial(material)
				.setDisplayName(displayName)
				.setLore(lore)
				.setGlow(isGlow);

		return new ItemWrapper(builder);
	}

	@Override
	public String toString() {
		return "ItemWrapper{" + "material=" + material + ", display_name='" + displayName + '\'' + ", lore=" + lore + ", builder=" + builder + '}';
	}
}