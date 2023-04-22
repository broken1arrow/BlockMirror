package org.brokenarrow.blockmirror.commands;

import org.brokenarrow.blockmirror.BlockMirror;
import org.brokenarrow.blockmirror.api.builders.BlockRotation;
import org.brokenarrow.blockmirror.api.builders.ItemWrapper;
import org.brokenarrow.blockmirror.api.builders.PlayerBuilder;
import org.brokenarrow.blockmirror.api.builders.PlayerBuilder.Builder;
import org.brokenarrow.blockmirror.api.builders.SettingsData;
import org.brokenarrow.blockmirror.api.builders.language.Language;
import org.brokenarrow.blockmirror.api.commands.CommandHolder;
import org.brokenarrow.blockmirror.api.utility.Actions;
import org.brokenarrow.blockmirror.utily.InventoyUtility;
import org.brokenarrow.menu.library.utility.Item.CreateItemStack;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;


public class CustomMirror extends CommandHolder {

	public CustomMirror() {
		super("custom");
	}

	@Override
	public void onCommand(final CommandSender sender, @NotNull final String commandLabel, @NotNull final String @NotNull [] cmdArg) {
		Language language = BlockMirror.getPlugin().getLanguageCache().getLanguage();
		if (cmdArg.length > 0) {
			String arg = cmdArg[0];
			String facing = null;
			if (cmdArg.length > 1)
				facing = cmdArg[1];
			Player player = ((Player) sender);
			if (arg.equals("set")) {
				player.setMetadata(Actions.set_distance.name(), new FixedMetadataValue(BlockMirror.getPlugin(), "set_distance"));
				player.removeMetadata(Actions.set_block.name(), BlockMirror.getPlugin());
				SettingsData settingsData = BlockMirror.getPlugin().getSettings().getSettingsData();
				ItemStack itemStack = null;
				if (settingsData != null && settingsData.getTools() != null) {
					ItemWrapper markertool = settingsData.getTools().getMarkertool();
					itemStack = CreateItemStack.of(markertool.getMaterial(), markertool.getDisplayName(), markertool.getLore())
							.setItemMetaData(Actions.set_distance.name(), Actions.set_distance.name()).makeItemStack();
				}
				if (itemStack != null)
					player.getInventory().addItem(itemStack);
				if (language.getPluginMessages() != null)
					BlockMirror.getPlugin().sendMessage(player, "Custom_command_set");

			}
			if (arg.equals("place")) {
				player.setMetadata(Actions.set_block.name(), new FixedMetadataValue(BlockMirror.getPlugin(), "set_distance"));
				player.removeMetadata(Actions.set_distance.name(), BlockMirror.getPlugin());
				if (facing != null) {
					PlayerBuilder data = BlockMirror.getPlugin().getPlayerCache().getOrCreateData(player.getUniqueId());
					Builder builder = data.getBuilder();
					builder.setBlockRotation(new BlockRotation(facing.toUpperCase()));
					BlockMirror.getPlugin().getPlayerCache().setPlayerData(player.getUniqueId(), builder.build());
				}
				if (language.getPluginMessages() != null)
					BlockMirror.getPlugin().sendMessage(player, "Custom_command_place");
			}
			if (arg.equals("clear")) {
				BlockMirror.getPlugin().getPlayerCache().clearPlayerData(player.getUniqueId());
				player.removeMetadata(Actions.set_block.name(), BlockMirror.getPlugin());
				player.removeMetadata(Actions.set_distance.name(), BlockMirror.getPlugin());
				SettingsData settingsData = BlockMirror.getPlugin().getSettings().getSettingsData();
				if (settingsData != null && settingsData.getTools() != null) {
					ItemWrapper markertool = settingsData.getTools().getMarkertool();
					ItemStack itemStack = CreateItemStack.of(markertool.getMaterial(), markertool.getDisplayName(), markertool.getLore())
							.setItemMetaData(Actions.set_distance.name(), Actions.set_distance.name()).makeItemStack();
					InventoyUtility.removeCustomItemFromInventory(player, itemStack, Actions.set_distance.name());
				}
				if (language.getPluginMessages() != null)
					BlockMirror.getPlugin().sendMessage(player, "Custom_command_clear");
			}
		}
	}

	@Nullable
	@Override
	public List<String> onTabComplete(@NotNull final CommandSender sender, @NotNull final String commandLabel, @NotNull final String @NotNull [] cmdArg) {
		List<String> list = new ArrayList<>();
		if (cmdArg.length < 2) {
			list.add("set");
			list.add("place");
			list.add("clear");
		}
		if (cmdArg.length == 2 && cmdArg[1].equals("place")) {
			for (BlockFace blockFace : BlockFace.values())
				list.add(blockFace.name());
		}
		return list;
	}
}
