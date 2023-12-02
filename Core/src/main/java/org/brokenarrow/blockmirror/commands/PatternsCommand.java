package org.brokenarrow.blockmirror.commands;

import org.broken.arrow.itemcreator.library.ItemCreator;
import org.brokenarrow.blockmirror.BlockMirror;
import org.brokenarrow.blockmirror.api.builders.ItemWrapper;
import org.brokenarrow.blockmirror.api.builders.PlayerBuilder;
import org.brokenarrow.blockmirror.api.builders.SettingsData;
import org.brokenarrow.blockmirror.api.builders.language.Language;
import org.brokenarrow.blockmirror.api.commands.CommandHolder;
import org.brokenarrow.blockmirror.api.utility.Actions;
import org.brokenarrow.blockmirror.menus.ChoosePattern;
import org.brokenarrow.blockmirror.utily.InventoyUtility;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class PatternsCommand extends CommandHolder {

	private final ItemCreator itemCreator = BlockMirror.getInstance().getItemCreator();

	public PatternsCommand() {
		super("patterns");
	}

	@Override
	public void onCommand(final CommandSender sender, @Nonnull final String commandLabel, @Nonnull final String[] cmdArg) {
		// Assume that the player has placed a block at position (x, y)
		Player player = ((Player) sender);
		Location location = player.getLocation();
		Language language = BlockMirror.getPlugin().getLanguageCache().getLanguage();

		String arg = "";
		if (cmdArg.length > 0)
			arg = cmdArg[0];
		player.removeMetadata(Actions.set_block.name(), BlockMirror.getPlugin());
		player.removeMetadata(Actions.classic_set_block.name(), BlockMirror.getPlugin());
		if (player.hasMetadata(Actions.set_distance.name())) {
			player.removeMetadata(Actions.set_distance.name(), BlockMirror.getPlugin());
			SettingsData settingsData = BlockMirror.getPlugin().getSettings().getSettingsData();
			if (settingsData != null && settingsData.getTools() != null) {
				ItemWrapper markertool = settingsData.getTools().getMarkertool();
				ItemStack itemStack = itemCreator.of(markertool.getMaterial(), markertool.getDisplayName(), markertool.getLore())
						.setItemMetaData(Actions.set_distance.name(), Actions.set_distance.name()).makeItemStack();
				InventoyUtility.removeCustomItemFromInventory(player, itemStack, Actions.set_distance.name());
			}
		}

		if (!player.hasMetadata(Actions.pattern.name())) {
			PlayerBuilder data = BlockMirror.getPlugin().getPlayerCache().getOrCreateData(player.getUniqueId());
			BlockMirror.getPlugin().getPlayerCache().setPlayerData(player.getUniqueId(), data.getBuilder().setCenterLocation(location).build());
			player.setMetadata(Actions.pattern.name(), new FixedMetadataValue(BlockMirror.getPlugin(), null));
		}
		if (arg.equals("clear")) {
			BlockMirror.getPlugin().getPlayerCache().clearPlayerData(player.getUniqueId());
			player.removeMetadata(Actions.pattern.name(), BlockMirror.getPlugin());

			if (language.getPluginMessages() != null)
				BlockMirror.getPlugin().sendMessage(player, "Pattern_command_clear");
		} else {
			new ChoosePattern(player, "Choose_pattern").menuOpen(player);
		}
	}

	@Nullable
	@Override
	public List<String> onTabComplete(@Nonnull final CommandSender sender, @Nonnull final String commandLabel, @Nonnull final String[] cmdArg) {
		List<String> tab = new ArrayList<>();
		if (cmdArg.length == 1) tab.add("clear");
		return tab;
	}
}
