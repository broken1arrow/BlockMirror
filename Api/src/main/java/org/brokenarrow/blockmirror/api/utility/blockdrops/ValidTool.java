package org.brokenarrow.blockmirror.api.utility.blockdrops;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Snow;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ValidTool {

	private final Map<Tools, List<Material>> tools = new HashMap<>();
	private final RandomDrops randomDrops = new RandomDrops();

	public ValidTool() {
		this.addTools();
	}

	@Nonnull
	public ItemStack[] validateItem(boolean alwaysSilk, ItemStack tool, Block minedBlock) {
		if (alwaysSilk) return new ItemStack[]{new ItemStack(minedBlock.getType())};

		return minedBlock.getDrops(tool).toArray(new ItemStack[0]);

		//todo Check so this works in any version. If not works, I need find easy way to check so players gets wrong item with wrong tool.
/*		if (!minedBlock.isPreferredTool(tool)) return new ItemStack[0];
		Material material = minedBlock.getType();

		if (material == Material.SNOW || minedBlock.getType() == Material.SNOW_BLOCK) {
			return new ItemStack[]{isSnow(tool, minedBlock)};
		}
		if (material == Material.GRASS_BLOCK) {
			return new ItemStack[]{isGras(tool)};
		}
		if (material == Material.PODZOL) {
			return new ItemStack[]{isPodzol(tool)};
		}
		if (material == Material.STONE) {
			return new ItemStack[]{isStone(tool)};
		}
		if (material == Material.CRIMSON_NYLIUM || material == Material.WARPED_NYLIUM) {
			return new ItemStack[]{isNylium(tool, new ItemStack(material))};
		}
		if (material.name().endsWith("_LEAVES")) {
			return isLeaves(tool, new ItemStack(material), minedBlock);
		}
		if (material == Material.matchMaterial("TWISTING_VINES") || material == Material.matchMaterial("WEEPING_VINES") ||
				material == Material.VINE || material == Material.matchMaterial("GLOW_LICHEN")) {
			return isVines(tool, new ItemStack(material), minedBlock);
		}
		if (material == Material.SEAGRASS || material == Material.TALL_SEAGRASS ||
				material == Material.GRASS || material == Material.TALL_GRASS ||
				material == Material.FERN || material == Material.LARGE_FERN ||
				material == Material.DEAD_BUSH) {
			return isGrassTypes(tool, new ItemStack(material));
		}
		ItemStack itemStack = new ItemStack(material);
		//itemStack.setData(minedBlock.getState().getData());
		return new ItemStack[]{itemStack};*/
	}

	public ItemStack[] isGrassTypes(ItemStack tool, ItemStack minedItem) {
		Material itemType = minedItem.getType();
		if (tool != null && containsAnyTool(tool.getType())) {
			if (containsTool(Tools.SHEARS, tool.getType())) {
				ItemStack itemStack = new ItemStack(itemType);
				if (itemType == Material.TALL_GRASS) {
					itemStack = new ItemStack(Material.GRASS);
					itemStack.setAmount(2);
				}
				if (itemType == Material.LARGE_FERN) {
					itemStack = new ItemStack(Material.FERN);
					itemStack.setAmount(2);
				}
				if (itemType == Material.TALL_SEAGRASS) {
					itemStack = new ItemStack(Material.SEAGRASS);
					itemStack.setAmount(2);
				}
				return new ItemStack[]{itemStack};
			}
		}
		return randomDrops.seedDrop(tool, minedItem);
	}

	public ItemStack[] isVines(@Nullable ItemStack tool, @Nonnull ItemStack minedItem, @Nonnull Block minedBlock) {
		Material itemType = minedItem.getType();
		if (tool != null && containsAnyTool(tool.getType())) {
			if (containsTool(Tools.SHEARS, tool.getType())) {
				return new ItemStack[]{new ItemStack(itemType)};
			}
			if (itemType == Material.VINE || itemType == Material.GLOW_LICHEN)
				return null;
			ItemMeta toolMeta = tool.getItemMeta();
			if (toolMeta != null) {
				if (toolMeta.hasEnchant(Enchantment.SILK_TOUCH)) {
					return new ItemStack[]{new ItemStack(itemType)};
				}
			}
		}
		return randomDrops.getDrops(tool, minedBlock);
	}

	public ItemStack[] isLeaves(ItemStack tool, ItemStack minedItem, Block minedBlock) {
		if (tool != null && containsAnyTool(tool.getType())) {
			ItemMeta toolMeta = tool.getItemMeta();
			if (toolMeta != null) {
				if (toolMeta.hasEnchant(Enchantment.SILK_TOUCH)) {
					return new ItemStack[]{new ItemStack(minedItem.getType())};
				}
			}
		}
		return randomDrops.getDrops(tool, minedBlock);
	}

	public ItemStack isNylium(ItemStack tool, ItemStack minedItem) {
		if (tool != null && containsTool(Tools.PICKAXE, tool.getType())) {
			ItemMeta toolMeta = tool.getItemMeta();
			if (toolMeta != null) {
				if (toolMeta.hasEnchant(Enchantment.SILK_TOUCH)) {
					return new ItemStack(minedItem.getType());
				}
			}
			return new ItemStack(Material.NETHERRACK);
		}
		return null;
	}

	public ItemStack isStone(ItemStack tool) {
		if (tool != null && containsTool(Tools.PICKAXE, tool.getType())) {
			ItemMeta toolMeta = tool.getItemMeta();
			if (toolMeta != null) {
				if (toolMeta.hasEnchant(Enchantment.SILK_TOUCH)) {
					return new ItemStack(Material.STONE);
				}
			}
			return new ItemStack(Material.COBBLESTONE);
		}
		return null;
	}

	public ItemStack isPodzol(ItemStack tool) {
		if (tool != null && containsAnyTool(tool.getType())) {
			ItemMeta toolMeta = tool.getItemMeta();
			if (toolMeta != null) {
				if (toolMeta.hasEnchant(Enchantment.SILK_TOUCH)) {
					return new ItemStack(Material.PODZOL);
				}
			}
		}
		return new ItemStack(Material.DIRT);
	}

	public ItemStack isGras(ItemStack tool) {
		if (tool != null && containsAnyTool(tool.getType())) {
			ItemMeta toolMeta = tool.getItemMeta();
			if (toolMeta != null) {
				if (toolMeta.hasEnchant(Enchantment.SILK_TOUCH)) {
					return new ItemStack(Material.GRASS_BLOCK);
				}
			}
		}
		return new ItemStack(Material.DIRT);
	}

	public ItemStack isSnow(ItemStack tool, Block minedBlock) {
		BlockData meta = minedBlock.getBlockData();
		int amountOfSnowBalls = 4;

		boolean isSnow = meta instanceof Snow;
		if (isSnow) {
			amountOfSnowBalls = ((Snow) meta).getLayers();
		}
		if (containsTool(Tools.SHOVEL, tool.getType())) {
			ItemMeta toolMeta = tool.getItemMeta();
			if (toolMeta != null) {
				if (toolMeta.hasEnchant(Enchantment.SILK_TOUCH)) {
					if (isSnow) {
						ItemStack finishItem = new ItemStack(Material.SNOW);
						finishItem.setAmount(amountOfSnowBalls);
						return finishItem;
					}
					return new ItemStack(Material.SNOW_BLOCK);
				}
			}
			if (amountOfSnowBalls > 0) {
				ItemStack finishItem = new ItemStack(Material.SNOWBALL);
				finishItem.setAmount(amountOfSnowBalls);
				return finishItem;
			}
		}
		return null;
	}

	public boolean containsTool(Tools key, Material tool) {
		List<Material> tools = this.tools.get(key);
		if (tools != null) return tools.contains(tool);
		return true;
	}

	public boolean containsAnyTool(Material tool) {
		Collection<List<Material>> tools = this.tools.values();
		for (List<Material> value : tools) {
			for (Material chachedTool : value) {
				if (chachedTool == tool) {
					return true;
				}
			}
		}
		return false;
	}

	public void addTools() {
		Material[] matrials = Material.values();
		for (Material material : matrials) {
			Tools tool = Tools.getTool(material);
			if (tool == null) continue;

			List<Material> tools = this.tools.get(tool);
			if (tools == null) tools = new ArrayList<>();
			tools.add(material);
			this.tools.put(tool, tools);
		}
	}
}
