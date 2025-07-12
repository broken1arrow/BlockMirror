package org.brokenarrow.blockmirror.menus;

import org.broken.arrow.library.itemcreator.ItemCreator;
import org.broken.arrow.library.menu.button.MenuButton;
import org.broken.arrow.library.menu.button.logic.ButtonUpdateAction;
import org.broken.arrow.library.menu.button.logic.FillMenuButton;
import org.broken.arrow.library.menu.holder.MenuHolderPage;
import org.brokenarrow.blockmirror.BlockMirror;
import org.brokenarrow.blockmirror.PlayerCache;
import org.brokenarrow.blockmirror.api.BlockMirrorUtillity;
import org.brokenarrow.blockmirror.api.blockpattern.PatternData;
import org.brokenarrow.blockmirror.api.builders.PlayerBuilder;
import org.brokenarrow.blockmirror.api.builders.language.PlaceholderText;
import org.brokenarrow.blockmirror.api.builders.menu.ButtonType;
import org.brokenarrow.blockmirror.api.builders.menu.MenuButtonData;
import org.brokenarrow.blockmirror.api.builders.menu.MenuTemplate;
import org.brokenarrow.blockmirror.api.filemanger.SerializeingLocation;
import org.brokenarrow.blockmirror.api.utility.Actions;
import org.brokenarrow.blockmirror.api.utility.BlockVisualizeAPI;
import org.brokenarrow.blockmirror.utily.EffectsActivated;
import org.brokenarrow.blockmirror.utily.TextConvertPlaceholders;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;

import javax.annotation.Nonnull;
import java.util.List;

public class ChoosePattern extends MenuHolderPage<PatternData> {

    private final MenuTemplate menuTemplate;
    private final BlockMirror plugin = BlockMirror.getPlugin();
    private final PlayerCache playerCache = plugin.getPlayerCache();
    ItemCreator itemCreator = BlockMirrorUtillity.getInstance().getItemCreator();
    private PlayerBuilder data;
    private BlockVisualizeAPI blockVisualize = plugin.getBlockVisualize();

    public ChoosePattern(Player player, String menuName) {
        super(BlockMirror.getPlugin().getPatternCache().getPatterns());
        this.menuTemplate = BlockMirror.getPlugin().getMenusCache().getTemplate(menuName);
        if (this.menuTemplate == null) return;
        data = playerCache.getOrCreateData(player.getUniqueId());
        setMenuSize(menuTemplate.getinvSize(menuName));
        setFillSpace(menuTemplate.getFillSlots());
        setTitle(menuTemplate.getMenuTitel());
        setMenuOpenSound(menuTemplate.getSound());
        setIgnoreItemCheck(true);

        if (data != null && data.getCenterLocation() != null && player.hasMetadata(Actions.pattern.name())) {
            PlayerBuilder.Builder builder = data.getBuilder();
            EffectsActivated.setEffect(player, data, builder,false);
            plugin.getPlayerCache().setPlayerData(player.getUniqueId(), builder.build());
            data = playerCache.getOrCreateData(player.getUniqueId());
        }
    }

    @Override
    public MenuButton getButtonAt(final int slot) {
        if (this.menuTemplate == null) return null;
        return registerButtons(menuTemplate.getMenuButton(slot));
    }

    private MenuButton registerButtons(final MenuButtonData value) {
        if (value == null) return null;
        return new MenuButton() {
            @Override
            public void onClickInsideMenu(@Nonnull final Player player, @Nonnull final Inventory menu, @Nonnull final ClickType click, @Nonnull final ItemStack clickedItem) {
                if (run(value, menu, player, click)) {
                    data = BlockMirror.getPlugin().getPlayerCache().getData(player.getUniqueId());
                    ChoosePattern.super.updateButtons();
                }
            }

            @Override
            public ItemStack getItem() {
                PlaceholderText placeholderText = plugin.getLanguageCache().getLanguage().getPlaceholderText();
                String blockFaceNotSet = placeholderText != null ? placeholderText.getBlockFaceNotSet() : "";
                if (isActive(value)) {
                    String loc = data.getCenterLocation() != null ? SerializeingLocation.serializeLoc(data.getCenterLocation()) : "";
                    if (value.getActive() != null)
                        return TextConvertPlaceholders.convertTextItemstack(value.getActive().getItemStack(), loc, data.getBlockRotation() != null ? data.getBlockRotation().getRotation() : blockFaceNotSet);
                    else
                        return TextConvertPlaceholders.convertTextItemstack(value.getPassive().getItemStack(), loc, data.getBlockRotation() != null ? data.getBlockRotation().getRotation() : blockFaceNotSet);
                } else
                    return TextConvertPlaceholders.convertTextItemstack(value.getPassive().getItemStack(), "", data.getBlockRotation() != null ? data.getBlockRotation().getRotation() : blockFaceNotSet);
            }
        };
    }

    public boolean isActive(MenuButtonData value) {
        if (value.getButtonType() == ButtonType.CentreLoc) {
            return data.getCenterLocation() != null;
        }
        if (value.getButtonType() == ButtonType.block_face) {
            return data.getBlockRotation() != null;
        }
        return false;
    }

    public boolean run(final MenuButtonData value, final Inventory menu, final Player player, final ClickType click) {

        if (value.getButtonType() == ButtonType.Back) {
            player.closeInventory();
            return false;
        }
        if (value.getButtonType() == ButtonType.Nxt_page) {
            this.nextPage();
            return false;
        }
        if (value.getButtonType() == ButtonType.Prv_page) {
            this.previousPage();
            return false;
        }
        if (value.getButtonType() == ButtonType.CentreLoc) {
            data = this.playerCache.getOrCreateData(player.getUniqueId());
            PlayerBuilder.Builder builder = data.getBuilder();
            if (!click.isLeftClick()) {
                EffectsActivated.removeEffect(player, data);
            } else {
                if (player.hasMetadata(Actions.pattern.name()))
                    EffectsActivated.setEffect(player, data, builder);
                else
                    EffectsActivated.setEffect(player, data, builder, "Loc set, choose pattern");
            }
            builder.setCenterLocation(click.isLeftClick() ? player.getLocation() : null);
            this.playerCache.setPlayerData(player.getUniqueId(), builder.build());
            data = this.playerCache.getOrCreateData(player.getUniqueId());
        }

        return true;
    }


    @Override
    public FillMenuButton<PatternData> createFillMenuButton() {
        return new FillMenuButton<>((player, inventory, click, itemStack, patternData) -> {
            data = this.playerCache.getOrCreateData(player.getUniqueId());
            final PlayerBuilder.Builder builder = data.getBuilder();
            if (click.isShiftClick()) {
                if (patternData != null)
                    new PatternSettings(player, "Pattern_settings", patternData)
                            .menuOpen(player);
                return ButtonUpdateAction.NONE;
            }
            /*else if (click.isLeftClick()) {
                player.setMetadata(Actions.pattern.name(), new FixedMetadataValue(BlockMirror.getPlugin(), patternData));
                builder.setCenterLocation(click.isLeftClick() ? player.getLocation() : null);
                EffectsActivated.setEffect(player, data, builder);
                this.playerCache.setPlayerData(player.getUniqueId(), builder.build());
            } else {
                EffectsActivated.removeEffect(player, data);
                builder.setCenterLocation(null);
                this.playerCache.setPlayerData(player.getUniqueId(), builder.build());
                player.removeMetadata(Actions.pattern.name(), BlockMirror.getPlugin());
            }*/

            if (patternData != null)
                patternData.clickMenu(player, click);
            data = this.playerCache.getOrCreateData(player.getUniqueId());
            return ButtonUpdateAction.ALL;
        }, (integer, patterns) -> {
            MenuButtonData menuButton = menuTemplate.getMenuButton(-1);
            if (menuButton != null && patterns != null) {


                boolean glow = false;
                List<MetadataValue> metadata = player.getMetadata(Actions.pattern.name());
                boolean isSame = false;
                if (metadata.size() > 0) {
                    Object value = metadata.get(0).value();
                    if (value instanceof PatternData) {
                        PatternData patternData = (PatternData) value;
                        isSame = patternData.equals(patterns);
                    }
                }
                String menudisplayName = "";
                List<String> menuLore = null;
                String loc = getLocationString();
                if (!isSame && menuButton.getPassive() != null) {
                    menudisplayName = menuButton.getPassive().getDisplayName();
                    menuLore = menuButton.getPassive().getLore();

                    glow = menuButton.getPassive().isGlow();
                } else if (isSame) {
                    if (menuButton.getActive() != null) {
                        menudisplayName = menuButton.getActive().getDisplayName();
                        menuLore = menuButton.getActive().getLore();

                        glow = menuButton.getActive().isGlow();
                    } else {
                        if (menuButton.getPassive() != null) {
                            menudisplayName = menuButton.getPassive().getDisplayName();
                            menuLore = menuButton.getPassive().getLore();

                            glow = menuButton.getPassive().isGlow();
                        }
                    }
                }
                String displayNamePattern = patterns.displayName(isSame);
                List<String> lorePattern = patterns.lore(isSame);

                String displayNamePlaceholderPattern = TextConvertPlaceholders.translatePlaceholders(
                        displayNamePattern,
                        getPlayerLocationString(),loc, lorePattern);

                List<String> lorePlacehlerPattern = TextConvertPlaceholders.translatePlaceholdersList(
                        lorePattern,
                        getPlayerLocationString(),loc,displayNamePlaceholderPattern);

                String text = TextConvertPlaceholders.translatePlaceholders(
                        menudisplayName,
                        displayNamePlaceholderPattern, lorePlacehlerPattern, loc, getPlayerLocationString());

                List<String> lore = TextConvertPlaceholders.translatePlaceholdersList(
                        menuLore,
                        displayNamePlaceholderPattern, lorePlacehlerPattern, loc, getPlayerLocationString());

                return itemCreator.of(patterns.icon(isSame), text, lore).setGlow(glow).makeItemStack();
            }
            return null;
        });
    }

    @Nonnull
    private String getLocationString() {
        Location centerLocation = data.getCenterLocation();
        if (centerLocation == null) {
            return plugin.getPlaceholder("Center_location_not_set");
        }
        return SerializeingLocation.serializeLoc(centerLocation);
    }

    @Nonnull
    private String getPlayerLocationString() {
        return plugin.getPlaceholder("Player_location").replace("{0}", SerializeingLocation.serializeLoc(this.player.getLocation()));
    }

}
