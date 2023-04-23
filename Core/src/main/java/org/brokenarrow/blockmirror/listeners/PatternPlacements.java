package org.brokenarrow.blockmirror.listeners;

import org.brokenarrow.blockmirror.BlockMirror;
import org.brokenarrow.blockmirror.PlayerCache;
import org.brokenarrow.blockmirror.api.BlockListener;
import org.brokenarrow.blockmirror.api.blockpattern.PatternData;
import org.brokenarrow.blockmirror.api.builders.PlayerBuilder;
import org.brokenarrow.blockmirror.api.builders.SettingsData;
import org.brokenarrow.blockmirror.api.builders.language.Language;
import org.brokenarrow.blockmirror.api.utility.Actions;
import org.brokenarrow.blockmirror.api.utility.blockdrops.ValidTool;
import org.brokenarrow.blockmirror.utily.BlockPlacements;
import org.brokenarrow.blockmirror.utily.InventoyUtility;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.brokenarrow.blockmirror.BlockMirror.getPlugin;

public class PatternPlacements implements BlockListener {
	private final ValidTool validTool = new ValidTool();

	@Override
	public void onBlockPlace(BlockPlaceEvent event) {
		Block block = event.getBlock();
		PlayerCache playerCache = BlockMirror.getPlugin().getPlayerCache();
		Language language = BlockMirror.getPlugin().getLanguageCache().getLanguage();
		Player player = event.getPlayer();
		Material material = block.getType();
		if (player.hasMetadata(Actions.pattern.name()) && material != Material.AIR) {
			List<MetadataValue> metadata = player.getMetadata(Actions.pattern.name());
			if (metadata.size() < 1) return;
			Object value = metadata.get(0).value();
			if (!(value instanceof PatternData)) return;
			PatternData patternData = (PatternData) value;
			PlayerBuilder data = playerCache.getOrCreateData(player.getUniqueId());
			if (data.getCenterLocation() == null) return;

			int distance = calcualateDistance(block.getLocation(), data.getCenterLocation());
			List<Location> locations = patternData.whenPlace(data, player, data.getCenterLocation(), block.getLocation(), distance);

			int amountOfItems = InventoyUtility.getAmountOfItems(player, block.getType(), locations.size() + 1);
			int amountNeeded = checkLocations(locations, material, patternData.replaceOnlyAir());
			boolean hasNeededItems = amountOfItems >= amountNeeded;

			if (hasNeededItems) {
				for (Location location : locations) {
					Material blockMaterial = location.getBlock().getType();
					if (blockMaterial == material)
						continue;
					if (patternData.replaceOnlyAir() && blockMaterial != Material.AIR)
						continue;
					location.getBlock().setType(material);
					BlockPlacements.setDirection(data, location.getBlock(), block);
				}
				BlockPlacements.setDirection(data, block, block);
				Bukkit.getScheduler().runTaskLater(BlockMirror.getPlugin(), () -> InventoyUtility.removeItemFromInventory(player, event.getItemInHand(), amountNeeded), 1);
			} else {
				if (language.getPluginMessages() != null)
					BlockMirror.getPlugin().sendMessage(player, "Not_enough_blocks", amountOfItems, (amountNeeded - amountOfItems), amountNeeded);
			}
		}
	}


	@Override
	public void onBlockBreak(BlockBreakEvent event) {
		Block block = event.getBlock();
		PlayerCache playerCache = BlockMirror.getPlugin().getPlayerCache();
		Player player = event.getPlayer();
		if (player.hasMetadata(Actions.pattern.name()) && block.getType() != Material.AIR) {
			List<MetadataValue> metadata = player.getMetadata(Actions.pattern.name());
			if (metadata.size() < 1) return;
			Object value = metadata.get(0).value();
			if (!(value instanceof PatternData)) return;
			PatternData patternData = (PatternData) value;
			PlayerBuilder data = playerCache.getOrCreateData(player.getUniqueId());
			if (data.getCenterLocation() == null) return;

			int distance = calcualateDistance(block.getLocation(), data.getCenterLocation());
			List<Location> locations = patternData.whenBreak(data, player, data.getCenterLocation(), block.getLocation(), distance);
			Material material = block.getType();
			Map<Material, ItemStack> itemStacks = new HashMap<>();

			SettingsData settingsData = getPlugin().getSettings().getSettingsData();
			boolean silkTouch = settingsData != null && settingsData.isSilkTouch();

			if (!locations.isEmpty()) {
				for (Location location : locations) {
					Block locationBlock = location.getBlock();
					Material blockType = locationBlock.getType();
					if (blockType == Material.AIR) continue;
					if (blockType != material) continue;
					if (block.getLocation().equals(location)) continue;

					ItemStack[] items = validTool.validateItem(silkTouch, player.getItemInHand(), block);
					InventoyUtility.collectItems(itemStacks, items);
					locationBlock.setType(Material.AIR);
				}
				if (patternData.allItemsOnBreak() && !itemStacks.isEmpty()) {
					event.setCancelled(true);
					ItemStack[] items = validTool.validateItem(silkTouch, player.getItemInHand(), block);
					InventoyUtility.collectItems(itemStacks, items);
					block.setType(Material.AIR);
				}
			}
			InventoyUtility.giveBackItems(player, itemStacks);
		}
	}

	public void collectItems(Map<Material, ItemStack> itemStacks, ItemStack[] items) {
		for (ItemStack item : items) {
			ItemStack stack = itemStacks.get(item.getType());
			if (stack != null) {
				stack.setAmount(stack.getAmount() + item.getAmount());
			} else {
				itemStacks.put(item.getType(), item);
			}
		}
	}

	public int checkLocations(List<Location> locations, Material material, boolean checkOnlyAir) {
		int amountReplaced = 0;
		for (Location location : locations) {
			Material blockMaterial = location.getBlock().getType();
			if (blockMaterial == material)
				continue;
			if (checkOnlyAir && blockMaterial != Material.AIR)
				continue;
			amountReplaced++;
		}
		return amountReplaced;
	}

	public int calcualateDistance(Location nextLocation, Location centerLocation) {
		int distanceZ = nextLocation.getBlockZ() - centerLocation.getBlockZ();
		int distanceX = nextLocation.getBlockX() - centerLocation.getBlockX();

		int value = Math.max(Math.abs(distanceX), Math.abs(distanceZ));
		return value;
	}
}
