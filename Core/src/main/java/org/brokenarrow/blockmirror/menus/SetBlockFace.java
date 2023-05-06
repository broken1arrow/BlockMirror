package org.brokenarrow.blockmirror.menus;

import org.brokenarrow.blockmirror.BlockMirror;
import org.brokenarrow.blockmirror.api.blockpattern.PatternData;
import org.brokenarrow.blockmirror.api.builders.BlockRotation;
import org.brokenarrow.blockmirror.api.builders.PlayerBuilder;
import org.brokenarrow.blockmirror.api.builders.PlayerBuilder.Builder;
import org.brokenarrow.blockmirror.api.builders.language.PlaceholderText;
import org.brokenarrow.blockmirror.api.builders.menu.ButtonType;
import org.brokenarrow.blockmirror.api.builders.menu.MenuButtonData;
import org.brokenarrow.blockmirror.api.builders.menu.MenuTemplate;
import org.brokenarrow.blockmirror.api.filemanger.SerializeingLocation;
import org.brokenarrow.blockmirror.menus.type.MenuType;
import org.brokenarrow.blockmirror.utily.TextConvertPlaceholders;
import org.brokenarrow.menu.library.MenuButton;
import org.brokenarrow.menu.library.MenuHolder;
import org.brokenarrow.menu.library.utility.Item.CreateItemStack;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SetBlockFace extends MenuHolder {

	private final MenuTemplate menuTemplate;
	private final MenuType menuType;
	private final PatternData patternData;
	private PlayerBuilder data;
	private final BlockMirror plugin = BlockMirror.getPlugin();

	public SetBlockFace(PlayerBuilder data, PatternData patternData, MenuType menuType, String menuName) {
		super(Arrays.asList(BlockFace.values()));
		this.menuTemplate = BlockMirror.getPlugin().getMenusCache().getTemplate(menuName);
		this.menuType = menuType;
		this.patternData = patternData;
		if (this.menuTemplate == null) return;
		if (data == null) this.data = new PlayerBuilder.Builder().build();
		else this.data = data;
		setMenuSize(menuTemplate.getinvSize(menuName));
		setFillSpace(menuTemplate.getFillSlots());
		setTitle(menuTemplate.getMenuTitel());
		setMenuOpenSound(menuTemplate.getSound());
	}

	@Override
	public MenuButton getFillButtonAt(@Nonnull final Object object) {
		return registerFillButtons(object);
	}


	private MenuButton registerFillButtons(Object object) {
		MenuButtonData menuButton = menuTemplate.getMenuButton(-1);
		return new MenuButton() {
			@Override
			public void onClickInsideMenu(@Nonnull final Player player, @Nonnull final Inventory menu, @Nonnull final ClickType click, @Nonnull final ItemStack clickedItem, final Object object) {
				Builder builder = data.getBuilder();
				if (builder != null) {
					if (click.isRightClick())
						builder.setBlockRotation(null);
					else
						builder.setBlockRotation(new BlockRotation(((BlockFace) object).name()));
					BlockMirror.getPlugin().getPlayerCache().setPlayerData(player.getUniqueId(), builder.build());
					new SetBlockFace(BlockMirror.getPlugin().getPlayerCache().getData(player.getUniqueId()), patternData, menuType, "set_blockface").menuOpen(player);
				}
			}

			@Override
			public ItemStack getItem(@Nonnull final Object object) {
				if (menuButton != null) {
					String text = ((BlockFace) object).name();
					List<String> lore = new ArrayList<>();
					boolean glow = false;
					PlaceholderText placeholderText = plugin.getLanguageCache().getLanguage().getPlaceholderText();
					String blockFaceNotSet = placeholderText != null ? placeholderText.getBlockFaceNotSet() : "";
					if (menuButton.getPassive() != null) {
						text = TextConvertPlaceholders.translatePlaceholders(
								menuButton.getPassive().getDisplayName(),
								((BlockFace) object).name(), data.getBlockRotation() != null);
						lore = TextConvertPlaceholders.translatePlaceholders(
								menuButton.getPassive().getLore(),
								((BlockFace) object).name(), data.getBlockRotation() != null, data.getBlockRotation() != null ? data.getBlockRotation().getRotation() : blockFaceNotSet);
						glow = menuButton.getPassive().isGlow();
					} else if (menuButton.getActive() != null) {
						text = TextConvertPlaceholders.translatePlaceholders(
								menuButton.getActive().getDisplayName(),
								((BlockFace) object).name(), data.getBlockRotation() != null);
						lore = TextConvertPlaceholders.translatePlaceholders(
								menuButton.getActive().getLore(),
								((BlockFace) object).name(), data.getBlockRotation() != null, data.getBlockRotation() != null ? data.getBlockRotation().getRotation() : blockFaceNotSet);
						glow = menuButton.getActive().isGlow();
					}

					return CreateItemStack.of("CRACKED_STONE_BRICKS", text, lore)
							.setGlow(glow && data.getBlockRotation() != null).makeItemStack();
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
			public void onClickInsideMenu(@Nonnull final Player player, @Nonnull final Inventory menu, @Nonnull final ClickType click, @Nonnull final ItemStack clickedItem, final Object object) {
				if (run(value, menu, player, click)) {
					data = BlockMirror.getPlugin().getPlayerCache().getData(player.getUniqueId());
					SetBlockFace.super.updateButton(this);
				}
			}

			@Override
			public ItemStack getItem() {

				if (isActive(value))
					if (value.getActive() != null)
						return TextConvertPlaceholders.convertTextItemstack(value.getActive().getItemStack(), SerializeingLocation.serializeLoc(data.getCenterLocation()));
					else
						return TextConvertPlaceholders.convertTextItemstack(value.getPassive().getItemStack(), SerializeingLocation.serializeLoc(data.getCenterLocation()));
				else return TextConvertPlaceholders.convertTextItemstack(value.getPassive().getItemStack(), "");
			}
		};
	}

	public boolean isActive(MenuButtonData value) {
		return false;
	}

	public boolean run(final MenuButtonData value, final Inventory menu, final Player player, final ClickType click) {
		if (value.getButtonType() == ButtonType.Back) {
			if (menuType == MenuType.Classic_Mirror_Settings)
				new ClassicMirrorSettings(player, "Auction_selector").menuOpen(player);
			else if (this.patternData != null)
				new PatternSettings(player, "Pattern_settings", this.patternData)
						.menuOpen(player);
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
