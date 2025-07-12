package org.brokenarrow.blockmirror.commands;

import org.brokenarrow.blockmirror.BlockMirror;
import org.brokenarrow.blockmirror.api.builders.PlayerBuilder;
import org.brokenarrow.blockmirror.api.builders.language.Language;
import org.brokenarrow.blockmirror.api.commands.CommandHolder;
import org.brokenarrow.blockmirror.api.utility.Actions;
import org.brokenarrow.blockmirror.menus.ClassicMirrorSettings;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ClassicMirror extends CommandHolder {

    public ClassicMirror() {
        super("classic");
    }

    @Override
    public void onCommand(final CommandSender sender, @NotNull final String commandLabel, @NotNull final String @NotNull [] cmdArg) {
        if (!(sender instanceof Player)) return;
        Language language = BlockMirror.getPlugin().getLanguageCache().getLanguage();
        Player player = ((Player) sender);
        if (cmdArg.length > 0) {
            String arg = cmdArg[0];
            if (arg.equals("clear")) {
                PlayerBuilder playerBuilder = BlockMirror.getPlugin().getPlayerCache().getData(player.getUniqueId());
                if (playerBuilder != null) {
                    BlockMirror.getPlugin().getRunTask().setQueueTime(playerBuilder.getEffectID(), 1);
                    BlockMirror.getPlugin().getPlayerCache().clearPlayerData(player.getUniqueId());
                }
                player.removeMetadata(Actions.classic_set_block.name(), BlockMirror.getPlugin());
                BlockMirror.getPlugin().sendMessage(player, "Classic_command_clear");
            }
        } else {

            new ClassicMirrorSettings(player, "Auction_selector").menuOpen(player);
        }
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull final CommandSender sender, @NotNull final String commandLabel, @NotNull final String @NotNull [] cmdArg) {
        List<String> tab = new ArrayList<>();
        if (cmdArg.length == 1) {
            tab.add("clear");
        }
        return tab;
    }
}
