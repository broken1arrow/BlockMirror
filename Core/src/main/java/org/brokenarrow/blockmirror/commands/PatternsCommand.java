package org.brokenarrow.blockmirror.commands;

import org.broken.arrow.library.itemcreator.ItemCreator;
import org.brokenarrow.blockmirror.BlockMirror;
import org.brokenarrow.blockmirror.api.builders.ItemWrapperApi;
import org.brokenarrow.blockmirror.api.builders.language.Language;
import org.brokenarrow.blockmirror.api.commands.CommandHolder;
import org.brokenarrow.blockmirror.api.settings.SettingsDataApi;
import org.brokenarrow.blockmirror.api.utility.Actions;
import org.brokenarrow.blockmirror.menus.ChoosePattern;
import org.brokenarrow.blockmirror.utily.InventoyUtility;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
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
        if (player.hasMetadata(Actions.set_block.name())) {
            BlockMirror.getPlugin().sendMessage(player, "Mode_set_block_dublicated");
            return;
        }
        if (player.hasMetadata(Actions.classic_set_block.name())) {
            BlockMirror.getPlugin().sendMessage(player, "Mode_classic_set_block_dublicated");
            return;
        }

        if (player.hasMetadata(Actions.set_distance.name())) {
            player.removeMetadata(Actions.set_distance.name(), BlockMirror.getPlugin());
            SettingsDataApi settingsData = BlockMirror.getPlugin().getSettings().getSettingsData();
            if (settingsData != null && settingsData.getTools() != null) {
                ItemWrapperApi markertool = settingsData.getTools().getMarkertool();
                ItemStack itemStack = itemCreator.of(markertool.getMaterial(), markertool.getDisplayName(), markertool.getLore())
                        .setItemMetaData(Actions.set_distance.name(), Actions.set_distance.name()).makeItemStack();
                InventoyUtility.removeCustomItemFromInventory(player, itemStack, Actions.set_distance.name());
            }
        }

        if (arg.equals("clear")) {
            BlockMirror.getPlugin().getPlayerCache().clearPlayerData(player.getUniqueId());
            player.removeMetadata(Actions.pattern.name(), BlockMirror.getPlugin());
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
