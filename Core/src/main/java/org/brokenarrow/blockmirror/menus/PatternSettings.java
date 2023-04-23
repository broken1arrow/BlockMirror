package org.brokenarrow.blockmirror.menus;

import org.brokenarrow.blockmirror.BlockMirror;
import org.brokenarrow.blockmirror.api.blockpattern.PatternData;
import org.brokenarrow.blockmirror.api.blockpattern.PatternSettingsWrapperApi;
import org.brokenarrow.blockmirror.api.builders.PlayerBuilder;
import org.brokenarrow.blockmirror.api.builders.language.PlaceholderText;
import org.brokenarrow.blockmirror.api.builders.menu.ButtonType;
import org.brokenarrow.blockmirror.api.builders.menu.MenuButtonData;
import org.brokenarrow.blockmirror.api.builders.menu.MenuTemplate;
import org.brokenarrow.blockmirror.api.filemanger.SerializeingLocation;
import org.brokenarrow.blockmirror.utily.TextConvertPlaceholders;
import org.brokenarrow.menu.library.MenuButton;
import org.brokenarrow.menu.library.MenuHolder;
import org.brokenarrow.menu.library.utility.Item.CreateItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;

public class PatternSettings extends MenuHolder {

	private final MenuTemplate menuTemplate;
	private final PatternData patternData;
	private PlayerBuilder data;
	private BlockMirror plugin = BlockMirror.getPlugin();

	public PatternSettings(Player player, String menuName, @Nonnull PatternData patternData) {
		super(patternData.getPatternSettingsWrapers());
		this.menuTemplate = BlockMirror.getPlugin().getMenusCache().getTemplate(menuName);
		this.patternData = patternData;
		if (this.menuTemplate == null) return;
		data = BlockMirror.getPlugin().getPlayerCache().getOrCreateData(player.getUniqueId());
		setMenuSize(menuTemplate.getinvSize(menuName));
		setFillSpace(menuTemplate.getFillSlots());
		setTitle(menuTemplate.getMenuTitel());
		setMenuOpenSound(menuTemplate.getSound());
	}


	@Override
	public MenuButton getFillButtonAt(final Object object) {
		return registerFillButtons();
	}


	private MenuButton registerFillButtons() {
		MenuButtonData menuButton = menuTemplate.getMenuButton(-1);
		return new MenuButton() {
			@Override
			public void onClickInsideMenu(final Player player, final Inventory menu, final ClickType click, final ItemStack clickedItem, final Object object) {
				PatternSettingsWrapperApi patternSettings = (PatternSettingsWrapperApi) object;
				if (click.isLeftClick())
					patternSettings.leftClick(patternData, player);
				else
					patternSettings.rightClick(patternData, player);
				PatternSettings.super.updateButton(this);
			}

			@Override
			public ItemStack getItem(final Object object) {
				if (menuButton != null) {
					PatternSettingsWrapperApi pattern = (PatternSettingsWrapperApi) object;


					boolean glow = false;
					boolean isSettingSet = pattern.isSettingSet(player);

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
					String text = TextConvertPlaceholders.translatePlaceholders(
							menudisplayName,
							pattern.displayName(getViewer(), isSettingSet), pattern.lore(getViewer(), isSettingSet));

					List<String> lore = TextConvertPlaceholders.translatePlaceholdersList(
							menuLore,
							pattern.displayName(getViewer(), isSettingSet), pattern.lore(getViewer(), isSettingSet));
					return CreateItemStack.of(pattern.icon(getViewer(), isSettingSet), text, lore).setGlow(glow).makeItemStack();

				}
				return null;
			}

			@Override
			public ItemStack getItem() {
				return null;
			}
		};
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
			public void onClickInsideMenu(final Player player, final Inventory menu, final ClickType click, final ItemStack clickedItem, final Object object) {
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


}
