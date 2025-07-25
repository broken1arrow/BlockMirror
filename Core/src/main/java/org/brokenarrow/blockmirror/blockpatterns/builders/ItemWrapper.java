package org.brokenarrow.blockmirror.blockpatterns.builders;

import org.brokenarrow.blockmirror.api.BlockMirrorUtility;
import org.brokenarrow.blockmirror.api.builders.ItemWrapperApi;
import org.brokenarrow.blockmirror.api.builders.ItemWrapperBuilder;
import org.brokenarrow.blockmirror.api.filemanger.ConfigurationSerializeUtility;
import org.bukkit.Material;

import javax.annotation.Nonnull;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ItemWrapper implements ItemWrapperApi, ConfigurationSerializeUtility {

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

	@Override
  public Material getMaterial() {
		return material;
	}

	@Override
  public String getDisplayName() {
		return displayName;
	}

	@Override
  public List<String> getLore() {
		return lore;
	}

	@Override
  public boolean isGlow() {
		return glow;
	}

	@Override
  public ItemWrapperBuilder getBuilder() {
		return builder;
	}

	public static class Builder implements ItemWrapperBuilder {
		private Material material;
		private String displayName;
		private List<String> lore;
		private boolean glow;

		@Override
    public Builder setMaterial(final Material material) {
			this.material = material;
			return this;
		}

		@Override
    public Builder setDisplayName(final String displayName) {
			this.displayName = displayName;
			return this;
		}

		@Override
    public Builder setLore(final List<String> lore) {
			this.lore = lore;
			return this;
		}

		@Override
    public Builder setGlow(final boolean glow) {
			this.glow = glow;
			return this;
		}

		@Override
		public ItemWrapperApi build() {
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
		org.broken.arrow.library.itemcreator.ItemCreator itemCreator = BlockMirrorUtility.getInstance().getItemCreator();
		String name = (String) map.getOrDefault("Material", null);
		Material material = itemCreator.of("OAK_SLAB").makeItemStack().getType();
		if (name != null) material = itemCreator.of(name).makeItemStack().getType();

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