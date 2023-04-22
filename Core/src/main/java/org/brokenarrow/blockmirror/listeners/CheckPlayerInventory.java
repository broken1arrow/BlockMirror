package org.brokenarrow.blockmirror.listeners;

import org.brokenarrow.blockmirror.BlockMirror;
import org.brokenarrow.blockmirror.api.blockListener;
import org.brokenarrow.blockmirror.api.builders.ItemWrapper;
import org.brokenarrow.blockmirror.api.builders.SettingsData;
import org.brokenarrow.blockmirror.api.utility.Actions;
import org.brokenarrow.blockmirror.utily.InventoyUtility;
import org.brokenarrow.menu.library.utility.Item.CreateItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class CheckPlayerInventory implements blockListener {
	SettingsData settingsData = BlockMirror.getPlugin().getSettings().getSettingsData();

	@Override
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		InventoyUtility.removeCustomItemFromInventory(player, getItem(settingsData), Actions.set_distance.name());
	}

	@Override
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		InventoyUtility.removeCustomItemFromInventory(player, getItem(settingsData), Actions.set_distance.name());
	}

	public ItemStack getItem(SettingsData settingsData) {
		ItemStack itemStack = null;
		if (settingsData != null && settingsData.getTools() != null) {
			ItemWrapper markertool = settingsData.getTools().getMarkertool();
			itemStack = CreateItemStack.of(markertool.getMaterial(), markertool.getDisplayName(), markertool.getLore())
					.setItemMetaData(Actions.set_distance.name(), Actions.set_distance.name()).makeItemStack();
		}
		return itemStack;
	}
}
