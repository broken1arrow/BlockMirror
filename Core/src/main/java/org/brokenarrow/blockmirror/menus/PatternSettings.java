package org.brokenarrow.blockmirror.menus;

import org.broken.arrow.library.itemcreator.ItemCreator;
import org.broken.arrow.library.menu.button.MenuButton;
import org.broken.arrow.library.menu.button.logic.ButtonUpdateAction;
import org.broken.arrow.library.menu.button.logic.FillMenuButton;
import org.broken.arrow.library.menu.holder.MenuHolderPage;
import org.brokenarrow.blockmirror.BlockMirror;
import org.brokenarrow.blockmirror.api.BlockMirrorUtility;
import org.brokenarrow.blockmirror.api.blockpattern.PatternData;
import org.brokenarrow.blockmirror.api.blockpattern.PatternSetting;
import org.brokenarrow.blockmirror.api.builders.PlayerBuilder;
import org.brokenarrow.blockmirror.api.builders.language.PlaceholderText;
import org.brokenarrow.blockmirror.api.builders.menu.ButtonType;
import org.brokenarrow.blockmirror.api.builders.menu.MenuButtonData;
import org.brokenarrow.blockmirror.api.builders.menu.MenuTemplate;
import org.brokenarrow.blockmirror.api.filemanger.SerializeingLocation;
import org.brokenarrow.blockmirror.utily.TextConvertPlaceholders;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;

public class PatternSettings extends MenuHolderPage<PatternSetting> {

    private final MenuTemplate menuTemplate;
    private final PatternData patternData;
    private final BlockMirror plugin = BlockMirror.getPlugin();
    private PlayerBuilder data;
    private final ItemCreator itemCreator = BlockMirrorUtility.getInstance().getItemCreator();

    public PatternSettings(Player player, String menuName, @Nonnull PatternData patternData) {
        super(patternData.getPatternSettings());
        this.menuTemplate = BlockMirror.getPlugin().getMenusCache().getTemplate(menuName);
        this.patternData = patternData;
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
                    PatternSettings.super.updateButton(this);
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
            new ChoosePattern(player, "Choose_pattern").menuOpen(player);
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

        return true;
    }


    @Override
    public FillMenuButton<PatternSetting> createFillMenuButton() {
        return new FillMenuButton<>((player, inventory, clickType, itemStack, patternSettings) -> {

            if (patternSettings != null && patternSettings.hasPermission(player)) {
                if (clickType.isLeftClick())
                    patternSettings.leftClick(patternData, player);
                else
                    patternSettings.rightClick(patternData, player);
            }
            return ButtonUpdateAction.THIS;
        }, (slot, pattern) -> {
            MenuButtonData menuButton = menuTemplate.getMenuButton(-1);
            if (menuButton != null && pattern != null) {

                boolean glow = false;
                boolean isSettingSet = pattern.isSettingSet(patternData, player);

                String menudisplayName = "";
                List<String> menuLore = null;
                if (!isSettingSet && menuButton.getPassive() != null) {
                    menudisplayName = menuButton.getPassive().getDisplayName();
                    menuLore = menuButton.getPassive().getLore();

                    glow = menuButton.getPassive().isGlow();
                } else if (isSettingSet) {
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
                String displayName = pattern.displayName(patternData, getViewer(), isSettingSet);
                List<String> lorePattern = pattern.lore(patternData, getViewer(), isSettingSet);
                String text = TextConvertPlaceholders.translatePlaceholders(
                        menudisplayName,
                        displayName, lorePattern);

                List<String> lore = TextConvertPlaceholders.translatePlaceholdersList(
                        menuLore,
                        displayName, lorePattern);
                return itemCreator.of(pattern.icon(getViewer(), isSettingSet), text, lore).setGlow(glow).makeItemStack();

            }
            return null;
        });
    }

}
