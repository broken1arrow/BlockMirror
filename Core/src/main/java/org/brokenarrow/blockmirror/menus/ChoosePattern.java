package org.brokenarrow.blockmirror.menus;

import org.broken.arrow.itemcreator.library.ItemCreator;
import org.broken.arrow.menu.library.button.MenuButton;
import org.broken.arrow.menu.library.button.logic.ButtonUpdateAction;
import org.broken.arrow.menu.library.button.logic.FillMenuButton;
import org.broken.arrow.menu.library.holder.MenuHolderPage;
import org.brokenarrow.blockmirror.BlockMirror;
import org.brokenarrow.blockmirror.api.BlockMirrorUtillity;
import org.brokenarrow.blockmirror.api.blockpattern.PatternData;
import org.brokenarrow.blockmirror.api.builders.PlayerBuilder;
import org.brokenarrow.blockmirror.api.builders.language.PlaceholderText;
import org.brokenarrow.blockmirror.api.builders.menu.ButtonType;
import org.brokenarrow.blockmirror.api.builders.menu.MenuButtonData;
import org.brokenarrow.blockmirror.api.builders.menu.MenuTemplate;
import org.brokenarrow.blockmirror.api.filemanger.SerializeingLocation;
import org.brokenarrow.blockmirror.api.utility.Actions;
import org.brokenarrow.blockmirror.utily.TextConvertPlaceholders;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import javax.annotation.Nonnull;
import java.util.List;

public class ChoosePattern extends MenuHolderPage<PatternData> {

    private final MenuTemplate menuTemplate;
    private PlayerBuilder data;
    private final BlockMirror plugin = BlockMirror.getPlugin();
    ItemCreator itemCreator = BlockMirrorUtillity.getInstance().getItemCreator();

    public ChoosePattern(Player player, String menuName) {
        super(BlockMirror.getPlugin().getPatternCache().getPatterns());
        this.menuTemplate = BlockMirror.getPlugin().getMenusCache().getTemplate(menuName);
        if (this.menuTemplate == null) return;
        data = BlockMirror.getPlugin().getPlayerCache().getOrCreateData(player.getUniqueId());
        setMenuSize(menuTemplate.getinvSize(menuName));
        setFillSpace(menuTemplate.getFillSlots());
        setTitle(menuTemplate.getMenuTitel());
        setMenuOpenSound(menuTemplate.getSound());
        setIgnoreItemCheck(true);
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
                    ChoosePattern.super.updateButton(this);
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
            PlayerBuilder data = BlockMirror.getPlugin().getPlayerCache().getOrCreateData(player.getUniqueId());

            BlockMirror.getPlugin().getPlayerCache()
                    .setPlayerData(player.getUniqueId(),
                            data.getBuilder().setCenterLocation(click.isLeftClick() ? player.getLocation() : null).build());
        }

        return true;
    }


    @Override
    public FillMenuButton<PatternData> createFillMenuButton() {
        return new FillMenuButton<>((player1, inventory, click, itemStack, patternData) -> {
            if (click.isLeftClick() && click.isShiftClick()) {
                if (patternData != null)
                    new PatternSettings(player, "Pattern_settings", patternData)
                            .menuOpen(player);
            } else if (click.isLeftClick())
                player.setMetadata(Actions.pattern.name(), new FixedMetadataValue(BlockMirror.getPlugin(), patternData));
            else
                player.setMetadata(Actions.pattern.name(), new FixedMetadataValue(BlockMirror.getPlugin(), null));
            return ButtonUpdateAction.THIS;
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
                String loc = isSame ? SerializeingLocation.serializeLoc(player.getLocation()) : "";
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
                String text = TextConvertPlaceholders.translatePlaceholders(
                        menudisplayName,
                        patterns.displayName(isSame), patterns.lore(isSame), loc);

                List<String> lore = TextConvertPlaceholders.translatePlaceholdersList(
                        menuLore,
                        patterns.displayName(isSame), patterns.lore(isSame), loc);
                return itemCreator.of(patterns.icon(isSame), text, lore).setGlow(glow).makeItemStack();
            }
            return null;
        });
    }
}
