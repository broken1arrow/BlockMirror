package org.brokenarrow.blockmirror.listeners;

import org.broken.arrow.library.itemcreator.ItemCreator;
import org.brokenarrow.blockmirror.BlockMirror;
import org.brokenarrow.blockmirror.api.BlockListener;
import org.brokenarrow.blockmirror.api.BlockMirrorUtility;
import org.brokenarrow.blockmirror.api.builders.ItemWrapperApi;
import org.brokenarrow.blockmirror.api.settings.SettingsDataApi;
import org.brokenarrow.blockmirror.api.utility.Actions;
import org.brokenarrow.blockmirror.utily.InventoyUtility;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class CheckPlayerInventory implements BlockListener {
	SettingsDataApi settingsData = BlockMirror.getPlugin().getSettings().getSettingsData();
	ItemCreator itemCreator = BlockMirrorUtility.getInstance().getItemCreator();

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

	public ItemStack getItem(SettingsDataApi settingsData) {
		ItemStack itemStack = null;
		if (settingsData != null && settingsData.getTools() != null) {
			ItemWrapperApi markertool = settingsData.getTools().getMarkertool();
			itemStack = itemCreator.of(markertool.getMaterial(), markertool.getDisplayName(), markertool.getLore())
					.setItemMetaData(Actions.set_distance.name(), Actions.set_distance.name()).makeItemStack();
		}
		return itemStack;
	}
}
