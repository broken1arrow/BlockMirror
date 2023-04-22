package org.brokenarrow.blockmirror.listeners;

import org.brokenarrow.blockmirror.BlockMirror;
import org.brokenarrow.blockmirror.PlayerCache;
import org.brokenarrow.blockmirror.api.CustomPlacementsApi;
import org.brokenarrow.blockmirror.api.builders.Distance;
import org.brokenarrow.blockmirror.api.builders.ItemWrapper;
import org.brokenarrow.blockmirror.api.builders.PlayerBuilder;
import org.brokenarrow.blockmirror.api.builders.PlayerBuilder.Builder;
import org.brokenarrow.blockmirror.api.builders.SettingsData;
import org.brokenarrow.blockmirror.api.builders.language.Language;
import org.brokenarrow.blockmirror.api.eventscustom.PreBlockBreakCustom;
import org.brokenarrow.blockmirror.api.eventscustom.PreBlockPlaceCustom;
import org.brokenarrow.blockmirror.api.filemanger.SerializeingLocation;
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
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.brokenarrow.blockmirror.BlockMirror.getPlugin;

public class CustomPlacements implements CustomPlacementsApi {
	private final Language language = BlockMirror.getPlugin().getLanguageCache().getLanguage();
	private final ValidTool validTool = new ValidTool();

	@Override
	public void onBlockInteract(PlayerInteractEvent event) {

		Block block = event.getClickedBlock();
		PlayerCache playerCache = BlockMirror.getPlugin().getPlayerCache();
		Player player = event.getPlayer();

		if (player.hasMetadata(Actions.set_distance.name()) && block != null && block.getType() != Material.AIR) {
			SettingsData settingsData = BlockMirror.getPlugin().getSettings().getSettingsData();
			ItemStack itemStack = event.getItem();
			boolean isRightType = false;
			if (settingsData != null && settingsData.getTools() != null) {
				ItemWrapper markertool = settingsData.getTools().getMarkertool();
				isRightType = itemStack != null && markertool.getMaterial() == itemStack.getType();
			}
			if (!isRightType) return;
			boolean hasMetadata = BlockMirror.getPlugin().getNbt().getCompMetadata().hasMetadata(itemStack, Actions.set_distance.name());
			if (!hasMetadata) return;

			PlayerBuilder data = playerCache.getData(player.getUniqueId());
			if (data == null) data = new PlayerBuilder.Builder().build();
			Builder builder = data.getBuilder();
			if (data.getCenterLocation() == null) {
				Location location = block.getLocation().clone();
				if (player.isSneaking()) {
					location.add(0, 1, 0);
				}
				String loc = SerializeingLocation.serializeLoc(location);

				if (language.getPluginMessages() != null)
					BlockMirror.getPlugin().sendMessage(player, "Set_center_loc", loc);
				builder.setCenterLocation(location);
			} else {
				Location location = block.getLocation();
				List<Distance> distances = data.getDistances();
				if (distances == null) distances = new ArrayList<>();
				distances.add(calcualateDistance(location, data.getCenterLocation()));
				builder.setDistances(distances);
				String loc = SerializeingLocation.serializeLoc(location);
				if (language.getPluginMessages() != null)
					BlockMirror.getPlugin().sendMessage(player, "Set_loc", loc);
			}
			playerCache.setPlayerData(player.getUniqueId(), builder.build());
			event.setCancelled(true);
		}
	}

	@Override
	public void onBlockPlace(BlockPlaceEvent event) {
		Block block = event.getBlock();
		PlayerCache playerCache = BlockMirror.getPlugin().getPlayerCache();
		Player player = event.getPlayer();

		if (player.hasMetadata(Actions.set_block.name()) && block.getType() != Material.AIR) {
			PlayerBuilder data = playerCache.getData(player.getUniqueId());
			if (data == null) return;
			if (data.getCenterLocation() == null) return;
			if (data.getDistances() == null) return;
			boolean hasNeededItems = InventoyUtility.checkInventory(player, block.getType(), data.getDistances().size() + 1);
			PreBlockPlaceCustom preBlockPlaceCustom = new PreBlockPlaceCustom(player, data, data.getDistances(), hasNeededItems);

			if (hasNeededItems && !preBlockPlaceCustom.isCancelled()) {
				for (Distance distance : data.getDistances()) {
					Location location = new Location(block.getWorld(), block.getX() + distance.getDistanceX(), block.getY() + distance.getDistanceY(), block.getZ() + distance.getDistanceZ());
					BlockPlacements.setDirection(data, location.getBlock(), block);
				}
				Bukkit.getScheduler().runTaskLater(BlockMirror.getPlugin(), () -> InventoyUtility.removeItemFromInventory(player, event.getItemInHand(), data.getDistances().size()), 1);
				if (data.getBlockRotation() != null) {
					BlockPlacements.setDirection(data, block, block);
				}
			}
		}
	}

	@Override
	public void onBlockBreak(BlockBreakEvent event) {
		Block block = event.getBlock();
		PlayerCache playerCache = BlockMirror.getPlugin().getPlayerCache();
		Player player = event.getPlayer();
		if (!player.hasMetadata(Actions.set_block.name()) || block.getType() == Material.AIR) return;
		PlayerBuilder data = playerCache.getData(player.getUniqueId());
		if (data == null) return;
		if (data.getCenterLocation() == null) return;
		if (data.getDistances() == null) return;
		Map<Material, ItemStack> itemStacks = new HashMap<>();
		PreBlockBreakCustom preBlockBreakCustom = new PreBlockBreakCustom(player, data, data.getDistances());
		if (preBlockBreakCustom.isCancelled()) return;

		SettingsData settingsData = getPlugin().getSettings().getSettingsData();
		boolean silkTouch = settingsData != null && settingsData.isSilkTouch();

		Material material = block.getType();
		for (Distance distance : data.getDistances()) {
			Location location = new Location(block.getWorld(), block.getX() + distance.getDistanceX(), block.getY() + distance.getDistanceY(), block.getZ() + distance.getDistanceZ());
			Block locationBlock = location.getBlock();
			if (locationBlock.getType() != material) continue;

			ItemStack[] items = validTool.validateItem(silkTouch, player.getItemInHand(), block);
			InventoyUtility.collectItems(itemStacks, items);
			locationBlock.setType(Material.AIR);
		}
		InventoyUtility.giveBackItems(player, itemStacks);
	}

	public Distance calcualateDistance(Location nextLocation, Location centerLocation) {
		int distanceZ = nextLocation.getBlockZ() - centerLocation.getBlockZ();
		int distanceX = nextLocation.getBlockX() - centerLocation.getBlockX();
		int distanceY = nextLocation.getBlockY() - centerLocation.getBlockY();

		return new Distance(distanceZ, distanceX, distanceY);
	}
}
