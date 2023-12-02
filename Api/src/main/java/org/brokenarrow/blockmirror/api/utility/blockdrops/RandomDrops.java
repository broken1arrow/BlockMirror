package org.brokenarrow.blockmirror.api.utility.blockdrops;


import org.brokenarrow.blockmirror.api.utility.RandomUntility;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class RandomDrops {

	private static final RandomUntility randomUntility = new RandomUntility();

	private final Map<Material, LootDropsData> lootDropsDataMap = new HashMap<Material, LootDropsData>() {{
/*		// Add data for Weeping Vines
		if (ServerVersion.atLeast(ServerVersion.V1_16)) {
			LootDropsData weepingVinesData = new LootDropsData()
					.setLootDrop(Material.WEEPING_VINES, new DropData(33).setChanceForLevels(new Pair<>(1, 55.0), new Pair<>(2, 77.0), new Pair<>(3, 100.0))
							.customDropChance((toolLevel, tool, mainedBlock) -> {
								if (toolLevel > 3) return 100.0;
								return -1;
							}));
			this.put(Material.WEEPING_VINES, weepingVinesData);

			// Add data for Twisting Vines
			LootDropsData twistingVinesData = new LootDropsData()
					.setLootDrop(Material.TWISTING_VINES, new DropData(33).setChanceForLevels(new Pair<>(1, 55.0), new Pair<>(2, 77.0), new Pair<>(3, 100.0))
							.customDropChance((toolLevel, tool, mainedBlock) -> {
								if (toolLevel > 3) return 100.0;
								return -1;
							}));
			this.put(Material.TWISTING_VINES, twistingVinesData);
		}

		// Add data for Birch Leaves
		Material birchSapling;
		Material birchLeaves;
		if (ServerVersion.atLeast(ServerVersion.V1_13)) {
			birchSapling = Material.OAK_SAPLING;
			birchLeaves = Material.OAK_LEAVES;
		} else {
			birchSapling = CreateItemStack.of("BIRCH_SAPLING").makeItemStack().getType();
			birchLeaves = CreateItemStack.of("BIRCH_LEAVES").makeItemStack().getType();
		}

		LootDropsData birchLeavesData = new LootDropsData()
				.setLootDrop(birchSapling, new DropData(5.0).setChanceForLevels(new Pair<>(1, 6.25), new Pair<>(2, 8.33), new Pair<>(3, 10.0)))
				.setLootDrop(Material.STICK, new DropData(2.0)
						.setChanceForLevels(new Pair<>(1, 5.5), new Pair<>(2, 6.25), new Pair<>(3, 8.325))
						.alterAmount((randomUntility, toolLevel, tool, mainedBlock) -> randomUntility.chance(50) ? 2 : 1));
		this.put(birchLeaves, birchLeavesData);


		Material acaciaSapling;
		Material acaciaLeaves;
		if (ServerVersion.atLeast(ServerVersion.V1_13)) {
			acaciaSapling = Material.OAK_SAPLING;
			acaciaLeaves = Material.OAK_LEAVES;
		} else {
			acaciaSapling = CreateItemStack.of("ACACIA_SAPLING").makeItemStack().getType();
			acaciaLeaves = CreateItemStack.of("ACACIA_LEAVES").makeItemStack().getType();
		}
		// Add data for Acacia Leaves
		LootDropsData acaciaLeavesData = new LootDropsData()
				.setLootDrop(acaciaSapling, new DropData(5.0)
						.setChanceForLevels(new Pair<>(1, 6.25), new Pair<>(2, 8.33), new Pair<>(3, 10.0)))
				.setLootDrop(Material.STICK, new DropData(2.0)
						.setChanceForLevels(new Pair<>(1, 5.5), new Pair<>(2, 6.25), new Pair<>(3, 8.325))
						.alterAmount((randomUntility, toolLevel, tool, mainedBlock) -> randomUntility.chance(50) ? 2 : 1));
		this.put(acaciaLeaves, acaciaLeavesData);

		Material oakSapling;
		Material oakLeaves;
		if (ServerVersion.atLeast(ServerVersion.V1_13)) {
			oakSapling = Material.OAK_SAPLING;
			oakLeaves = Material.OAK_LEAVES;
		} else {
			oakSapling = CreateItemStack.of("OAK_SAPLING").makeItemStack().getType();
			oakLeaves = CreateItemStack.of("OAK_LEAVES").makeItemStack().getType();
		}
		// Add data for Oak Leaves
		LootDropsData oakLeavesData = new LootDropsData()
				.setLootDrop(oakSapling, new DropData(5.0).setChanceForLevels(new Pair<>(1, 6.25), new Pair<>(2, 8.33), new Pair<>(3, 10.0)))
				.setLootDrop(Material.STICK, new DropData(2.0).setChanceForLevels(new Pair<>(1, 5.5), new Pair<>(2, 6.25), new Pair<>(3, 8.325))
						.alterAmount((randomUntility, toolLevel, tool, mainedBlock) -> randomUntility.chance(50) ? 2 : 1))
				.setLootDrop(Material.APPLE, new DropData(0.5).setChanceForLevels(new Pair<>(1, 0.556), new Pair<>(2, 0.625), new Pair<>(3, 0.833)));
		this.put(oakLeaves, oakLeavesData);

		Material darkOakSapling;
		Material darkOakLeaves;
		if (ServerVersion.atLeast(ServerVersion.V1_13)) {
			darkOakSapling = Material.OAK_SAPLING;
			darkOakLeaves = Material.OAK_LEAVES;
		} else {
			darkOakSapling = CreateItemStack.of("DARK_OAK_SAPLING").makeItemStack().getType();
			darkOakLeaves = CreateItemStack.of("DARK_OAK_LEAVES").makeItemStack().getType();
		}
		// Add data for Dark Oak Leaves
		LootDropsData darkOakLeavesData = new LootDropsData()
				.setLootDrop(darkOakSapling, new DropData(5.0).setChanceForLevels(new Pair<>(1, 6.25), new Pair<>(2, 8.33), new Pair<>(3, 10.0)))
				.setLootDrop(Material.STICK, new DropData(2.0).setChanceForLevels(new Pair<>(1, 5.5), new Pair<>(2, 6.25), new Pair<>(3, 8.325))
						.alterAmount((randomUntility, toolLevel, tool, mainedBlock) -> randomUntility.chance(50) ? 2 : 1))
				.setLootDrop(Material.APPLE, new DropData(0.5).setChanceForLevels(new Pair<>(1, 0.556), new Pair<>(2, 0.625), new Pair<>(3, 0.833)));
		this.put(darkOakLeaves, darkOakLeavesData);

		Material jungleSapling;
		Material jungleLeaves;
		if (ServerVersion.atLeast(ServerVersion.V1_13)) {
			jungleSapling = Material.OAK_SAPLING;
			jungleLeaves = Material.OAK_LEAVES;
		} else {
			jungleSapling = CreateItemStack.of("JUNGLE_SAPLING").makeItemStack().getType();
			jungleLeaves = CreateItemStack.of("JUNGLE_LEAVES").makeItemStack().getType();
		}
		// Add data for Jungle Leaves
		LootDropsData jungleLeavesData = new LootDropsData()
				.setLootDrop(jungleSapling, new DropData(2.0).setChanceForLevels(new Pair<>(1, 2.224), new Pair<>(2, 2.5), new Pair<>(3, 3.336)))
				.setLootDrop(Material.STICK, new DropData(2.0).setChanceForLevels(new Pair<>(1, 5.5), new Pair<>(2, 6.25), new Pair<>(3, 8.325))
						.alterAmount((randomUntility, toolLevel, tool, mainedBlock) -> randomUntility.chance(50) ? 2 : 1));
		this.put(jungleLeaves, jungleLeavesData);

		// Add data for Azalea Leaves
		Material azaleaLeaves = Material.matchMaterial("AZALEA_LEAVES");
		if (azaleaLeaves != null) {
			LootDropsData azaleaLeavesData = new LootDropsData()
					.setLootDrop(Material.AZALEA, new DropData(5.0).setChanceForLevels(new Pair<>(1, 6.25), new Pair<>(2, 8.33), new Pair<>(3, 10.0)))
					.setLootDrop(Material.STICK, new DropData(2.0).setChanceForLevels(new Pair<>(1, 5.5), new Pair<>(2, 6.25), new Pair<>(3, 8.325))
							.alterAmount((randomUntility, toolLevel, tool, mainedBlock) -> randomUntility.chance(50) ? 2 : 1));
			this.put(azaleaLeaves, azaleaLeavesData);
		}

		// Add data for Flowering Azalea Leaves
		Material floweringAzalea = Material.matchMaterial("FLOWERING_AZALEA_LEAVES");
		if (floweringAzalea != null) {
			LootDropsData floweringAzaleaLeavesData = new LootDropsData()
					.setLootDrop(Material.FLOWERING_AZALEA, new DropData(5.0).setChanceForLevels(new Pair<>(1, 6.25), new Pair<>(2, 8.33), new Pair<>(3, 10.0)))
					.setLootDrop(Material.STICK, new DropData(2.0).setChanceForLevels(new Pair<>(1, 5.5), new Pair<>(2, 6.25), new Pair<>(3, 8.325))
							.alterAmount((randomUntility, toolLevel, tool, mainedBlock) -> randomUntility.chance(50) ? 2 : 1));
			this.put(floweringAzalea, floweringAzaleaLeavesData);
		}
		// Add data for Mangrove Leaves
		Material mangroveLeaves = Material.matchMaterial("MANGROVE_LEAVES");
		if (mangroveLeaves != null) {
			LootDropsData mangroveLeavesData = new LootDropsData()
					.setLootDrop(Material.STICK, new DropData(2.0).setChanceForLevels(new Pair<>(1, 5.5), new Pair<>(2, 6.25), new Pair<>(3, 8.325))
							.alterAmount((randomUntility, toolLevel, tool, mainedBlock) -> randomUntility.chance(50) ? 2 : 1));
			this.put(mangroveLeaves, mangroveLeavesData);
		}*/
	}};


	public ItemStack[] seedDrop(ItemStack tool, ItemStack minedItem) {

		List<ItemStack> itemStacks = new ArrayList<>();
		if (minedItem.getType() == Material.DEAD_BUSH) {
			int amount = randomUntility.nextRandomInt(3);
			if (amount > 0) {
				ItemStack itemStack = new ItemStack(Material.STICK);
				itemStack.setAmount(amount);
				itemStacks.add(itemStack);
			}
			return itemStacks.toArray(new ItemStack[0]);
		}
		int fortuneLevel = fortuneLevel(tool);
		int maxAmount = 0;
		if (fortuneLevel <= 0) maxAmount = 1;
		if (fortuneLevel == 1) maxAmount = 3;
		if (fortuneLevel == 2) maxAmount = 5;
		if (fortuneLevel == 3) maxAmount = 7;
		int amount = 0;
		if (maxAmount == 1)
			if (randomUntility.chance(42))
				amount = 1;
		for (int i = 0; i < fortuneLevel; i++) {
			if (randomUntility.chance(42))
				amount = Math.max(amount, randomUntility.nextRandomInt(maxAmount));
		}
		System.out.println("amount " + amount);
		System.out.println("bound) bound) " + maxAmount);
		if (amount > 0) {
			ItemStack itemStack = new ItemStack(Material.WHEAT_SEEDS);
			itemStack.setAmount(amount);
			itemStacks.add(itemStack);
		}
		randomUntility.newRandomsSeed();
		return itemStacks.toArray(new ItemStack[0]);
	}

	/**
	 * Give you the item some shall be dropped with calcuated chance.
	 *
	 * @param tool       used to main the block.
	 * @param minedBlock the block player break.
	 * @return array of itemstacks that could be added or empty if it could not add any.
	 */
	public ItemStack[] getDrops(@Nullable final ItemStack tool, @Nonnull final Block minedBlock) {
		List<ItemStack> itemStacks;
		Material material = minedBlock.getType();
		LootDropsData data = lootDropsDataMap.get(material);
		if (data != null) {
			itemStacks = getLootItemstacks(data, tool, minedBlock);
		} else
			itemStacks = new ArrayList<>();
		randomUntility.newRandomsSeed();
		return itemStacks.toArray(new ItemStack[0]);
	}

	private List<ItemStack> getLootItemstacks(@Nonnull final LootDropsData data, @Nullable final ItemStack tool, @Nonnull final Block block) {
		Map<Material, DropData> dataLootDrops = data.getLootDrops();
		List<ItemStack> itemStacks = new ArrayList<>();
		int fortuneLevel = fortuneLevel(tool);
		if (!dataLootDrops.isEmpty())
			for (Entry<Material, DropData> dataLootDrop : dataLootDrops.entrySet()) {
				if (dataLootDrop == null) continue;
				DropData dropData = dataLootDrop.getValue();
				double dropChance = dropData.executeCalculateDrop(fortuneLevel, tool, block);
				if (dropChance < 0) {
					if (fortuneLevel == 0)
						dropChance = dropData.getDefultChance();
					else
						dropChance = dropData.getChance(fortuneLevel);
				}
				if (dropChance > 0 && randomUntility.chance(dropChance)) {
					int amountOfItems = dropData.executeCalculateAmount(randomUntility, fortuneLevel, tool, block);
					ItemStack itemStack = new ItemStack(dataLootDrop.getKey());
					if (amountOfItems > 0)
						itemStack.setAmount(amountOfItems);
					itemStacks.add(itemStack);

				}
				System.out.println("vinesDrop " + dropChance);
			}
		return itemStacks;
	}

	public int fortuneLevel(ItemStack tool) {
		if (tool == null) return 0;
		ItemMeta toolMeta = tool.getItemMeta();
		if (toolMeta != null) {
			if (toolMeta.hasEnchant(Enchantment.LOOT_BONUS_BLOCKS)) {
				return toolMeta.getEnchantLevel(Enchantment.LOOT_BONUS_BLOCKS);
			}
		}
		return 0;
	}

}
