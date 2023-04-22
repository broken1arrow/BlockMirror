package org.brokenarrow.blockmirror.api.builders.menu;

import org.brokenarrow.blockmirror.api.filemanger.ConfigurationSerializeUtility;
import org.brokenarrow.menu.library.utility.Item.CreateItemStack;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MenuButton implements ConfigurationSerializeUtility {

	private final ItemStack itemStack;
	private final DyeColor color;
	private final Material material;
	private final String displayName;
	private final List<String> lore;
	private final boolean glow;
	private final ButtonType buttonType;

	public MenuButton(final Builder builder) {
		this.itemStack = builder.itemStack;
		this.color = builder.color;
		this.material = builder.material;
		this.displayName = builder.displayName;
		this.lore = builder.lore;
		this.glow = builder.glow;
		this.buttonType = builder.buttonType;
	}

	public ItemStack getItemStack() {
		return itemStack;
	}

	public DyeColor getColor() {
		return color;
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

	public ButtonType getButtonType() {
		return buttonType;
	}

	public static class Builder {

		public ItemStack itemStack;
		private DyeColor color;
		private Material material;
		private String displayName;
		private List<String> lore;
		private boolean glow;
		private ButtonType buttonType;

		public Builder setItemStack(final ItemStack itemStack) {
			this.itemStack = itemStack;
			return this;
		}

		public Builder setColor(final DyeColor color) {
			this.color = color;
			return this;
		}

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

		public Builder setButtonType(final ButtonType buttonType) {
			this.buttonType = buttonType;
			return this;
		}

		public MenuButton build() {
			return new MenuButton(this);
		}
	}

	@Nonnull
	@Override
	public Map<String, Object> serialize() {
		final Map<String, Object> map = new LinkedHashMap<>();
		map.put("color", color);
		map.put("material", material + "");
		map.put("name", displayName);
		map.put("lore", lore);
		map.put("glow", glow);
		if (buttonType != null) map.put("buttonType", buttonType);
		return map;
	}

	public static MenuButton deserialize(final Map<String, Object> map) {
		final String color = (String) map.get("color");
		final String material = (String) map.get("material");
		final String displayName = (String) map.get("name");
		final List<String> lore = (List<String>) map.get("lore");
		final boolean glow = (boolean) map.getOrDefault("glow", false);
		final String buttonType = (String) map.get("buttonType");
		ItemStack itemStack;
		if (color != null)
			itemStack = CreateItemStack.of(material, color, displayName, lore).setGlow(glow).makeItemStack();
		else
			itemStack = CreateItemStack.of(material, displayName, lore).setGlow(glow).makeItemStack();

		final Builder builder = new Builder();
		builder.setButtonType(ButtonType.valueOfType(buttonType))
				.setItemStack(itemStack).setColor(dyeColor(color))
				.setDisplayName(displayName)
				.setMaterial(Material.getMaterial(material))
				.setGlow(glow)
				.setLore(lore);
		return builder.build();
	}

	@Override
	public String toString() {
		return "MenuButton{" + "itemStack=" + itemStack +
				", color=" + color +
				", material=" + material +
				", displayName='" + displayName + '\'' +
				", lore=" + lore +
				", glow=" + glow +
				", buttonType=" + buttonType + '}';
	}

	@Nullable
	public static DyeColor dyeColor(final String dyeColor) {
		final DyeColor[] dyeColors = DyeColor.values();

		for (final DyeColor color : dyeColors) {
			if (color.name().equalsIgnoreCase(dyeColor)) {
				return color;
			}
		}
		return null;
	}
}