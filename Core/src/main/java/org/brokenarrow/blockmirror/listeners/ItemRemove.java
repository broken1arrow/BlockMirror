package org.brokenarrow.blockmirror.listeners;

import org.brokenarrow.blockmirror.BlockMirror;
import org.brokenarrow.blockmirror.api.BlockListener;
import org.brokenarrow.blockmirror.api.builders.language.Language;
import org.brokenarrow.blockmirror.api.utility.Actions;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ItemRemove implements BlockListener {

	BlockMirror plugin = BlockMirror.getPlugin();

	@Override
	public void onSwichSlot(PlayerItemHeldEvent event) {
		Player player = event.getPlayer();
		if (!player.hasMetadata(Actions.set_distance.name())) return;

		ItemStack itemStack = event.getPlayer().getInventory().getItem(event.getPreviousSlot());
		if (itemStack == null) return;

		String metadata = plugin.getNbt().getCompMetadata().getMetadata(itemStack, Actions.set_distance.name());

		if (metadata != null) {

			plugin.sendMessage(player, "Switch_slot");

			event.getPlayer().getInventory().setItem(event.getPreviousSlot(), new ItemStack(Material.AIR));
			player.removeMetadata(Actions.set_distance.name(), plugin);

			//BlockMirror.runTaskLater(20, () -> new ModifyContinerData.AlterContainerDataMenu(playerMetadata).menuOpen(player), false);
		}
	}

	@Override
	public void onDropItem(PlayerDropItemEvent event) {
		Player player = event.getPlayer();
		if (!player.hasMetadata(Actions.set_distance.name())) return;

		Language language = plugin.getLanguageCache().getLanguage();
		ItemStack itemStack = event.getItemDrop().getItemStack();
		String metadata = plugin.getNbt().getCompMetadata().getMetadata(itemStack, Actions.set_distance.name());

		if (metadata != null) {

			if (language.getPluginMessages() != null)
				plugin.sendMessage(player, "Drop_item");

			Inventory inventory = player.getInventory();
			int heldItemSlot = event.getPlayer().getInventory().getHeldItemSlot();
			inventory.setItem(heldItemSlot, new ItemStack(Material.AIR));

			event.getItemDrop().setPickupDelay(500);
			event.getItemDrop().remove();

			player.removeMetadata(Actions.set_distance.name(), plugin);
			//BlockMirror.runTaskLater(20, () -> new ModifyContinerData.AlterContainerDataMenu(playerMetadata).menuOpen(player), false);
		}
	}
}
