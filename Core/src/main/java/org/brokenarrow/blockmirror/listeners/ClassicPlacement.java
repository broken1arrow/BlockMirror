package org.brokenarrow.blockmirror.listeners;

import org.brokenarrow.blockmirror.PlayerCache;
import org.brokenarrow.blockmirror.api.BlockListener;
import org.brokenarrow.blockmirror.api.builders.Distance;
import org.brokenarrow.blockmirror.api.builders.MirrorLoc;
import org.brokenarrow.blockmirror.api.builders.MirrorOption;
import org.brokenarrow.blockmirror.api.builders.PlayerBuilder;
import org.brokenarrow.blockmirror.api.builders.SettingsData;
import org.brokenarrow.blockmirror.api.eventscustom.PreBlockBreakClassic;
import org.brokenarrow.blockmirror.api.eventscustom.PreBlockPlaceClassic;
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

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.brokenarrow.blockmirror.BlockMirror.getPlugin;

public class ClassicPlacement implements BlockListener {
	private final ValidTool validTool = new ValidTool();

	@Override
	public void onBlockPlace(BlockPlaceEvent event) {
		Block block = event.getBlock();
		PlayerCache playerCache = getPlugin().getPlayerCache();
		Player player = event.getPlayer();

		if (player.hasMetadata(Actions.classic_set_block.name()) && block.getType() != Material.AIR) {
			PlayerBuilder data = playerCache.getData(player.getUniqueId());
			if (data == null) return;
			if (data.getCenterLocation() == null) return;
			Set<Location> locations = getMirrorLoc(data, block.getLocation());
			if (locations == null) return;
			boolean hasNeededItems = InventoyUtility.checkInventory(player, block.getType(), locations.size() + 1);
			PreBlockPlaceClassic preBlockPlaceClassic = new PreBlockPlaceClassic(player, data, locations, hasNeededItems);

			if (hasNeededItems && !preBlockPlaceClassic.isCancelled()) {
				for (Location location : locations) {
					BlockPlacements.setDirection(data, location.getBlock(), block);
				}
				Bukkit.getScheduler().runTaskLater(getPlugin(), () -> InventoyUtility.removeItemFromInventory(player, event.getItemInHand(), locations.size()), 1);
				if (data.getBlockRotation() != null) {
					BlockPlacements.setDirection(data, block, block);
				}
			}
		}
	}

	@Override
	public void onBlockBreak(BlockBreakEvent event) {
		Block block = event.getBlock();
		PlayerCache playerCache = getPlugin().getPlayerCache();
		Player player = event.getPlayer();

		if (!player.hasMetadata(Actions.classic_set_block.name()) || block.getType() == Material.AIR) return;
		PlayerBuilder data = playerCache.getData(player.getUniqueId());
		if (data == null) return;
		if (data.getCenterLocation() == null) return;
		Map<Material, ItemStack> itemStacks = new HashMap<>();
		Set<Location> locations = getMirrorLoc(data, block.getLocation());
		if (locations == null) return;
		Material material = block.getType();
		PreBlockBreakClassic preBlockBreakClassic = new PreBlockBreakClassic(player, data, locations);
		if (preBlockBreakClassic.isCancelled()) return;

		SettingsData settingsData = getPlugin().getSettings().getSettingsData();
		boolean silkTouch = settingsData != null && settingsData.isSilkTouch();
		for (Location location : locations) {
			Block locationBlock = location.getBlock();
			if (locationBlock.getType() != material) continue;
			ItemStack[] items = validTool.validateItem(silkTouch, player.getItemInHand(), block);
			InventoyUtility.collectItems(itemStacks, items);
			locationBlock.setType(Material.AIR);
		}
		InventoyUtility.giveBackItems(player, itemStacks);
	}

	/**
	 * Returns a list of mirror locations for the given player data and placed location.
	 * Returns an empty list if there are no mirror locations.
	 *
	 * @return
	 */
	@Nullable
	public Set<Location> getMirrorLoc(PlayerBuilder data, Location placedLoc) {
		MirrorLoc mirrorLoc = data.getMirrorLoc();
		if (mirrorLoc == null) {
			return null;
		}
		Set<Location> locations = new HashSet<>();
		int centerX = data.getCenterLocation().getBlockX();
		int centerY = data.getCenterLocation().getBlockY();
		int centerZ = data.getCenterLocation().getBlockZ();

		int xDiff = placedLoc.getBlockX() - centerX;
		int yDiff = placedLoc.getBlockY() - centerY;
		int zDiff = placedLoc.getBlockZ() - centerZ;
		// Loop through each mirror option and mirror the placed location accordingly.
		for (MirrorOption option : mirrorLoc.getOptions())
			switch (option) {
				case MIRROR_X:
					// Mirror the location across the X axis.
					locations.add(mirrorLocation(centerX - placedLoc.getBlockX(), yDiff, zDiff, data.getCenterLocation()));
					// If the mirror location also mirrors the Y axis, mirror the location across both axes.
					if (mirrorLoc.isMirrorY()) {
						locations.add(mirrorLocation(centerX - placedLoc.getBlockX(), centerY - placedLoc.getBlockY(), zDiff, data.getCenterLocation()));
					}
					// If the mirror location also mirrors the Z axis, mirror the location across both axes.
					if (mirrorLoc.isMirrorZ()) {
						locations.add(mirrorLocation(xDiff, yDiff, zDiff, data.getCenterLocation()));
						// If the mirror location also mirrors the Y axis, mirror the location across all three axes.
						if (mirrorLoc.isMirrorY()) {
							locations.add(mirrorLocation(xDiff, placedLoc.getBlockY() - centerY, zDiff, data.getCenterLocation()));
						}
					}
					break;
				case MIRROR_Y:
					// Mirror the location across the Y axis.
					locations.add(mirrorLocation(xDiff, centerY - placedLoc.getBlockY(), zDiff, data.getCenterLocation()));
					break;
				case MIRROR_Z:
					// Mirror the location across the Z axis.
					locations.add(mirrorLocation(xDiff, yDiff, centerZ - placedLoc.getBlockZ(), data.getCenterLocation()));
					// If the mirror location also mirrors the Y axis, mirror the location across both axes.
					if (mirrorLoc.isMirrorY()) {
						locations.add(mirrorLocation(xDiff, centerY - placedLoc.getBlockY(), centerZ - placedLoc.getBlockZ(), data.getCenterLocation()));
					}
					break;
				case MIRROR_XY:
					// Mirror the location across both the X and Y axes.
					locations.add(mirrorLocation(centerX - placedLoc.getBlockX(), centerY - placedLoc.getBlockY(), zDiff, data.getCenterLocation()));
					break;
				case MIRROR_ZY:
					// Mirror the location across both the Z and Y axes.
					locations.add(mirrorLocation(xDiff, centerY - placedLoc.getBlockY(), centerZ - placedLoc.getBlockZ(), data.getCenterLocation()));
					break;
				case MIRROR_XZ:
					// Mirror the location across both the X and Z axes.
					Location location_XZ = mirrorLocation(centerZ - placedLoc.getBlockZ(), yDiff, centerX - placedLoc.getBlockX(), data.getCenterLocation());
					locations.add(location_XZ);
					if (mirrorLoc.isMirrorZX()) {
						locations.add(location_XZ.clone().subtract(0, 0, (centerX - placedLoc.getBlockX()) * 2));
						locations.add(placedLoc.clone().subtract(0, 0, (placedLoc.getBlockZ() - centerZ) * 2));
					}
					break;
				case MIRROR_ZX:
					// Mirror the location across both the Z and X axes.
					Location location_ZX = mirrorLocation(placedLoc.getBlockZ() - centerZ, yDiff, placedLoc.getBlockX() - centerX, data.getCenterLocation());
					locations.add(location_ZX);
					if (mirrorLoc.isMirrorXZ()) {
						locations.add(location_ZX.clone().add(0, 0, (centerX - placedLoc.getBlockX()) * 2));
						locations.add(placedLoc.clone().add((centerX - placedLoc.getBlockX()) * 2, 0, 0));
						locations.add(placedLoc.clone().add((centerX - placedLoc.getBlockX()) * 2, 0, (centerZ - placedLoc.getBlockZ()) * 2));
					}
					break;
				case ROTATE_CLOCKWISE_90:
				case ROTATE_COUNTERCLOCKWISE_90:
					// TODO: Implement rotation
					break;

				default:
					break;
			}
		locations.removeIf(location -> location.equals(placedLoc));

		return locations;
	}

	public Location mirrorLocation(int x, int y, int z, Location centerLoc) {

		int centerX = centerLoc.getBlockX();
		int centerY = centerLoc.getBlockY();
		int centerZ = centerLoc.getBlockZ();

		int mirroredX = x + centerX;
		int mirroredY = y + centerY;
		int mirroredZ = z + centerZ;

		return new Location(centerLoc.getWorld(), mirroredX, mirroredY, mirroredZ);
	}

	/*	public List<Location> getMirrrorLoc(PlayerBuilder data, Location placedLoc) {
	 *//*	if (mirrorLoc.isRotateClockWise_90()) {
			int temp = x;
			x = -z;
			z = temp;
		}
		if (mirrorLoc.isRotateCounterClockWise_90()) {
			int temp = x;
			x = z;
			z = -temp;
		}*//*
		MirrorLoc mirrorLoc = data.getMirrorLoc();
		if (mirrorLoc == null) return null;
		List<Location> locations = new ArrayList<>();
		int centerX = data.getCenterLocation().getBlockX();
		int centerY = data.getCenterLocation().getBlockY();
		int centerZ = data.getCenterLocation().getBlockZ();
		if (mirrorLoc.isMirrorX()) {
			Location location = new Location(placedLoc.getWorld(), centerX, placedLoc.getY(), centerZ);
			location.add(centerX - placedLoc.getBlockX(), 0, 0);
			location.subtract(0, 0, centerZ - placedLoc.getBlockZ());

			locations.add(location);

			if (mirrorLoc.isMirrorY()) {
				Location locationY = new Location(placedLoc.getWorld(), centerX, centerY, centerZ);
				locationY.add(0, centerY - placedLoc.getY(), 0);
				locationY.subtract(centerX - location.getBlockX(), 0, centerZ - location.getBlockZ());
				locations.add(locationY);
			}
			if (mirrorLoc.isMirrorZ()) {
				Location locationZ = new Location(placedLoc.getWorld(), centerX, placedLoc.getY(), centerZ);
				locationZ.add(0, 0, centerZ - location.getZ());
				locationZ.subtract(centerX - location.getBlockX(), 0, 0);
				locations.add(locationZ);
				if (mirrorLoc.isMirrorY()) {
					Location locationY = new Location(placedLoc.getWorld(), centerX, centerY, centerZ);
					locationY.add(0, centerY - placedLoc.getY(), 0);
					locationY.subtract(centerX - locationZ.getBlockX(), 0, centerZ - locationZ.getBlockZ());
					locations.add(locationY);
				}
			}

		}
		if (mirrorLoc.isMirrorY()) {
			Location location = new Location(placedLoc.getWorld(), centerX, centerY, centerZ);
			location.add(0, centerY - placedLoc.getY(), 0);
			location.subtract(centerX - placedLoc.getBlockX(), 0, centerZ - placedLoc.getBlockZ());

			locations.add(location);
		}
		if (mirrorLoc.isMirrorZ()) {
			Location location = new Location(placedLoc.getWorld(), centerX, placedLoc.getY(), centerZ);
			location.add(0, 0, centerZ - placedLoc.getZ());
			location.subtract(centerX - placedLoc.getBlockX(), 0, 0);
			locations.add(location);
			if (mirrorLoc.isMirrorY()) {
				Location locationY = new Location(placedLoc.getWorld(), centerX, centerY, centerZ);
				locationY.add(0, centerY - placedLoc.getY(), 0);
				locationY.subtract(centerX - location.getBlockX(), 0, centerZ - location.getBlockZ());
				locations.add(locationY);
			}
		}
		if (mirrorLoc.isMirrorXY()) {
			Location location = new Location(placedLoc.getWorld(), centerX, placedLoc.getBlockY(), centerZ);
			location.add(0, placedLoc.getBlockY() - centerY, 0);
			location.add(centerX - placedLoc.getBlockX(), 0, centerZ - placedLoc.getBlockZ());

			locations.add(location);
		}
		if (mirrorLoc.isMirrorZY()) {
			Location location = new Location(placedLoc.getWorld(), centerX, placedLoc.getBlockY(), centerZ);
			location.add(0, placedLoc.getBlockY() - centerY, 0);
			location.add(0, 0, placedLoc.getBlockZ() - centerZ);

			locations.add(location);
		}
		if (mirrorLoc.isMirrorXZ()) {
			Location location = new Location(placedLoc.getWorld(), centerX, placedLoc.getBlockY(), centerZ);
			location.add(placedLoc.getBlockZ() - centerZ, 0, placedLoc.getBlockX() - centerX);

			locations.add(location);
		}
		if (mirrorLoc.isMirrorZX()) {
			Location location = new Location(placedLoc.getWorld(), centerX, placedLoc.getBlockY(), centerZ);
			location.subtract(placedLoc.getBlockZ() - centerZ, 0, placedLoc.getBlockX() - centerX);

			locations.add(location);
		}
		if (mirrorLoc.isRotateClockWise_90()) {
		}
		if (mirrorLoc.isRotateCounterClockWise_90()) {
		}
		return locations;
	}*/

	public Distance calcualateDistance(Location nextLocation, Location centerLocation) {
		int distanceZ = nextLocation.getBlockZ() - centerLocation.getBlockZ();
		int distanceX = nextLocation.getBlockX() - centerLocation.getBlockX();
		int distanceY = nextLocation.getBlockY() - centerLocation.getBlockY();

		return new Distance(distanceZ, distanceX, distanceY);
	}
}
