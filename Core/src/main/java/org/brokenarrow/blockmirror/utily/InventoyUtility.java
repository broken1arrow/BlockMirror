package org.brokenarrow.blockmirror.utily;

import org.brokenarrow.blockmirror.BlockMirror;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class InventoyUtility {


	public static boolean checkInventory(Player player, Material material, int locationAmount) {
		int amount = 0;

		ItemStack item = new ItemStack(material);
		for (ItemStack itemStack : player.getInventory()) {
			if (itemStack != null)
				if (itemStack.getType() == material && itemStack.isSimilar(item)) {
					if (itemStack.getAmount() >= locationAmount)
						return true;
					amount += itemStack.getAmount();
				}
		}

		return amount >= locationAmount;
	}

	public static int getAmountOfItems(Player player, Material material, int locationAmount) {
		int amount = 0;

		ItemStack item = new ItemStack(material);
		for (ItemStack itemStack : player.getInventory()) {
			if (itemStack != null)
				if (itemStack.getType() == material && itemStack.isSimilar(item)) {
					if (itemStack.getAmount() >= locationAmount)
						return itemStack.getAmount();
					amount += itemStack.getAmount();
				}
		}

		return amount;
	}

	public static void removeCustomItemFromInventory(Player player, ItemStack itemToRemove, String key) {
		if (itemToRemove == null) return;
		for (ItemStack itemStack : player.getInventory()) {
			if (itemStack != null) {
				if (itemToRemove.getType() == itemStack.getType()) {
					String metadata = BlockMirror.getPlugin().getNbt().getCompMetadata().getMetadata(itemStack, key);
					if (itemToRemove.isSimilar(itemStack) || metadata != null) {
						itemStack.setAmount(0);
						itemStack.setType(Material.AIR);

					}
				}
			}
		}
	}

	public static void removeItemFromInventory(Player player, ItemStack placeditemStack, int amountToRemove) {
		final ItemStack placeditemStackClone = placeditemStack.clone();
		int amount = amountToRemove;
		int size = 0;
		while (amount > 0) {
			for (ItemStack itemStack : player.getInventory()) {
				size++;
				if (itemStack != null)
					if (placeditemStackClone.getType() == itemStack.getType() && placeditemStackClone.isSimilar(itemStack)) {
						if (itemStack.getAmount() > 0) {
							int toRemove = Math.min(itemStack.getAmount(), amount);
							amount -= toRemove;
							itemStack.setAmount(itemStack.getAmount() - toRemove);
						}
					}
				if (amount <= 0)
					break;
			}
			if (size >= player.getInventory().getSize())
				break;
		}
	}

	public static void giveBackItems(Player player, Material material, int amount) {
		if (material == null) return;

		int stacks = amount / material.getMaxStackSize();
		List<ItemStack> itemStacks = new ArrayList<>();
		for (int times = 0; times < stacks; times++) {
			itemStacks.add(new ItemStack(material, material.getMaxStackSize()));
		}
		int leftOvers = amount - (stacks * 64);
		if (leftOvers > 0) {
			itemStacks.add(new ItemStack(material, leftOvers));
		}
		for (ItemStack itemStack : itemStacks) {
			HashMap<Integer, ItemStack> leftOver = player.getInventory().addItem(itemStack);
			if (!leftOver.isEmpty()) {
				for (ItemStack item : leftOver.values()) {
					World world = player.getLocation().getWorld();
					if (world != null) world.dropItemNaturally(player.getLocation(), item);
				}
			}
		}
	}

	public static void giveBackItems(Player player, List<ItemStack> itemStacks) {
		if (itemStacks.isEmpty()) return;

		for (ItemStack itemStack : itemStacks) {
			if (itemStack == null) continue;
			HashMap<Integer, ItemStack> leftOver = player.getInventory().addItem(itemStack);
			if (!leftOver.isEmpty()) {
				for (ItemStack item : leftOver.values()) {
					World world = player.getLocation().getWorld();
					if (world != null) world.dropItemNaturally(player.getLocation(), item);
				}
			}
		}
	}

	public static void giveBackItems(Player player, ItemStack itemStack) {
		if (itemStack == null) return;

		HashMap<Integer, ItemStack> leftOver = player.getInventory().addItem(itemStack);
		if (!leftOver.isEmpty()) {
			for (ItemStack item : leftOver.values()) {
				World world = player.getLocation().getWorld();
				if (world != null) world.dropItemNaturally(player.getLocation(), item);
			}
		}
	}

	public static void collectItems(Map<Material, ItemStack> itemStacks, ItemStack[] items) {
		for (ItemStack item : items) {
			ItemStack stack = itemStacks.get(item.getType());
			if (stack != null) {
				stack.setAmount(stack.getAmount() + item.getAmount());
			} else {
				itemStacks.put(item.getType(), item);
			}
		}
	}

	public static void giveBackItems(final Player player, final Map<Material, ItemStack> itemStacks) {
		if (itemStacks.isEmpty()) return;

		for (Entry<Material, ItemStack> itemStack : itemStacks.entrySet()) {
			if (itemStack == null) continue;
			HashMap<Integer, ItemStack> leftOver = player.getInventory().addItem(itemStack.getValue());
			if (!leftOver.isEmpty()) {
				for (ItemStack item : leftOver.values()) {
					World world = player.getLocation().getWorld();
					if (world != null) world.dropItemNaturally(player.getLocation(), item);
				}
			}
		}
	}
}
