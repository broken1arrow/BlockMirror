package org.brokenarrow.blockmirror.menus;

import org.brokenarrow.blockmirror.BlockMirror;
import org.brokenarrow.blockmirror.api.builders.MirrorLoc;
import org.brokenarrow.blockmirror.api.builders.PlayerBuilder;
import org.brokenarrow.blockmirror.api.builders.PlayerBuilder.Builder;
import org.brokenarrow.blockmirror.api.builders.language.PlaceholderText;
import org.brokenarrow.blockmirror.api.builders.menu.ButtonType;
import org.brokenarrow.blockmirror.api.builders.menu.MenuButtonData;
import org.brokenarrow.blockmirror.api.builders.menu.MenuTemplate;
import org.brokenarrow.blockmirror.api.filemanger.SerializeingLocation;
import org.brokenarrow.blockmirror.api.utility.Actions;
import org.brokenarrow.blockmirror.utily.TextConvertPlaceholders;
import org.brokenarrow.blockmirror.utily.blockVisualization.BlockVisualize;
import org.brokenarrow.blockmirror.utily.effects.SpawnBorderEffects;
import org.brokenarrow.menu.library.MenuButton;
import org.brokenarrow.menu.library.MenuHolder;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

public class ClassicMirrorSettings extends MenuHolder {

	private final MenuTemplate menuTemplate;
	private PlayerBuilder data;
	private BlockMirror plugin = BlockMirror.getPlugin();

	public ClassicMirrorSettings(Player player, String menuName) {
		this.menuTemplate = BlockMirror.getPlugin().getMenusCache().getTemplate(menuName);
		if (this.menuTemplate == null) return;
		data = BlockMirror.getPlugin().getPlayerCache().getOrCreateData(player.getUniqueId());
		setMenuSize(menuTemplate.getinvSize(menuName));
		setFillSpace(menuTemplate.getFillSlots());
		setTitle(menuTemplate.getMenuTitel());
		setMenuOpenSound(menuTemplate.getSound());
	}

	@Override
	public MenuButton getFillButtonAt(final Object object) {
		return null;
	}


	private MenuButton registerFillButtons() {
		return new MenuButton() {
			@Override
			public void onClickInsideMenu(final Player player, final Inventory menu, final ClickType click, final ItemStack clickedItem, final Object object) {

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
					ClassicMirrorSettings.super.updateButton(this);
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
		if (value.getButtonType() == ButtonType.Start) {
			return player.hasMetadata(Actions.classic_set_block.name());
		}
		if (value.getButtonType() == ButtonType.CentreLoc) {
			return data.getCenterLocation() != null;
		}
		if (value.getButtonType() == ButtonType.block_face) {
			return data.getBlockRotation() != null;
		}
		if (data.getMirrorLoc() != null) {
			if (value.getButtonType() == ButtonType.mirrorX) {
				return data.getMirrorLoc().isMirrorX();
			}
			if (value.getButtonType() == ButtonType.mirrorY) {
				return data.getMirrorLoc().isMirrorY();
			}
			if (value.getButtonType() == ButtonType.mirrorZ) {
				return data.getMirrorLoc().isMirrorZ();
			}
			if (value.getButtonType() == ButtonType.mirrorXY) {
				return data.getMirrorLoc().isMirrorXY();
			}
			if (value.getButtonType() == ButtonType.mirrorZY) {
				return data.getMirrorLoc().isMirrorZY();
			}
			if (value.getButtonType() == ButtonType.mirrorXZ) {
				return data.getMirrorLoc().isMirrorXZ();
			}
			if (value.getButtonType() == ButtonType.mirrorZX) {
				return data.getMirrorLoc().isMirrorZX();
			}
		}
		return false;
	}

	public boolean run(final MenuButtonData value, final Inventory menu, final Player player, final ClickType click) {

		if (data == null) data = new PlayerBuilder.Builder().build();
		Builder builder = data.getBuilder();
		MirrorLoc.Builder mirrorbuilder;
		if (data.getMirrorLoc() != null)
			mirrorbuilder = data.getMirrorLoc().getBuilder();
		else
			mirrorbuilder = new MirrorLoc.Builder();
		if (value.getButtonType() == ButtonType.Back) {
			player.closeInventory();
			return false;
		}
		if (value.getButtonType() == ButtonType.Start) {
			if (player.hasMetadata(Actions.classic_set_block.name())) {
				player.removeMetadata(Actions.classic_set_block.name(), BlockMirror.getPlugin());
				BlockMirror.getPlugin().getRunTask().setQueueTime(data.getEffectID(), 1);
				BlockVisualize.visulizeBlock(player.getLocation().getBlock(), player.getLocation(), false);
			} else {
				player.setMetadata(Actions.classic_set_block.name(), new FixedMetadataValue(BlockMirror.getPlugin(), Actions.classic_set_block));
			}
		}
		if (value.getButtonType() == ButtonType.CentreLoc) {
			if (click.isLeftClick()) {
				builder.setCenterLocation(player.getLocation());
				SpawnBorderEffects spawnBorderEffects = new SpawnBorderEffects(null, player, player.getLocation(), -1, 2.5);
				builder.setEffectID(spawnBorderEffects.getID());
				BlockMirror.getPlugin().getRunTask().addTask(spawnBorderEffects);
				BlockVisualize.visulizeBlock(player.getLocation().getBlock(), player.getLocation(), true);
			} else {
				if (data.getEffectID() > 0) {
					builder.setCenterLocation(null);
					BlockMirror.getPlugin().getRunTask().setQueueTime(data.getEffectID(), 1);
					BlockVisualize.visulizeBlock(player.getLocation().getBlock(), player.getLocation(), false);
				}
			}
		}
		if (value.getButtonType() == ButtonType.mirrorX) {
			mirrorbuilder.setMirrorX(click.isLeftClick());
		}
		if (value.getButtonType() == ButtonType.mirrorY) {
			mirrorbuilder.setMirrorY(click.isLeftClick());
		}
		if (value.getButtonType() == ButtonType.mirrorZ) {
			mirrorbuilder.setMirrorZ(click.isLeftClick());
		}
		if (value.getButtonType() == ButtonType.mirrorXY) {
			mirrorbuilder.setMirrorXY(click.isLeftClick());
		}
		if (value.getButtonType() == ButtonType.mirrorZY) {
			mirrorbuilder.setMirrorZY(click.isLeftClick());
		}
		if (value.getButtonType() == ButtonType.mirrorXZ) {
			mirrorbuilder.setMirrorXZ(click.isLeftClick());
		}
		if (value.getButtonType() == ButtonType.mirrorZX) {
			mirrorbuilder.setMirrorZX(click.isLeftClick());
		}
		if (value.getButtonType() == ButtonType.block_face) {
			new SetBlockFace(data, "set_blockface").menuOpen(player);
		}
		builder.setMirrorLoc(mirrorbuilder.build());
		BlockMirror.getPlugin().getPlayerCache().setPlayerData(player.getUniqueId(), builder.build());
		return true;
	}
}
