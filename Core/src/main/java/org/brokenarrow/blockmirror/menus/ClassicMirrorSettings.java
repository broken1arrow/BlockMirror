package org.brokenarrow.blockmirror.menus;

import org.broken.arrow.itemcreator.library.SkullCreator;
import org.broken.arrow.menu.library.button.MenuButton;
import org.broken.arrow.menu.library.holder.MenuHolder;
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
import org.brokenarrow.blockmirror.api.utility.OppositeFacing;
import org.brokenarrow.blockmirror.utily.TextConvertPlaceholders;
import org.brokenarrow.blockmirror.utily.blockVisualization.BlockVisualize;
import org.brokenarrow.blockmirror.utily.effects.SpawnBorderEffects;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import javax.annotation.Nonnull;
import java.util.UUID;

import static org.brokenarrow.blockmirror.menus.type.MenuType.Classic_Mirror_Settings;

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
		if (data != null && data.getCenterLocation() != null) {
			Builder builder = data.getBuilder();
			BlockMirror.getPlugin().getRunTask().setQueueTime(data.getEffectID(), -2);
			BlockVisualize.visulizeBlock(data.getCenterLocation().getBlock(), data.getCenterLocation(), true);
			SpawnBorderEffects spawnBorderEffects = new SpawnBorderEffects(null, player, data.getCenterLocation(), -1, 2.5);
			builder.setEffectID(spawnBorderEffects.getID());
			BlockMirror.getPlugin().getRunTask().addTask(spawnBorderEffects);
			BlockMirror.getPlugin().getPlayerCache().setPlayerData(player.getUniqueId(), builder.build());
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
					ClassicMirrorSettings.super.updateButton(this);
				}
			}

			@Override
			public ItemStack getItem() {
				PlaceholderText placeholderText = plugin.getLanguageCache().getLanguage().getPlaceholderText();
				String blockFaceNotSet = placeholderText != null ? placeholderText.getBlockFaceNotSet() : "";
				return getItemStack(value);
/*				if (isActive(value)) {
					String loc = data.getCenterLocation() != null ? SerializeingLocation.serializeLoc(data.getCenterLocation()) : "";
					if (value.getActive() != null)
						return TextConvertPlaceholders.convertTextItemstack(value.getActive().getItemStack(), loc, data.getBlockRotation() != null ? data.getBlockRotation().getRotation() : blockFaceNotSet);
					else
						return TextConvertPlaceholders.convertTextItemstack(value.getPassive().getItemStack(), loc, data.getBlockRotation() != null ? data.getBlockRotation().getRotation() : blockFaceNotSet);
				} else
					return TextConvertPlaceholders.convertTextItemstack(value.getPassive().getItemStack(), "", data.getBlockRotation() != null ? data.getBlockRotation().getRotation() : blockFaceNotSet);
			*/
			}
		};
	}

	public ItemStack getItemStack(MenuButtonData value) {
		boolean active = false;
		Object placeholderTextFromData = "";
		if (value.getButtonType() == ButtonType.Start) {
			active = player.hasMetadata(Actions.classic_set_block.name());
		}
		if (value.getButtonType() == ButtonType.CentreLoc) {
			active = data.getCenterLocation() != null;
		}
		if (value.getButtonType() == ButtonType.block_face) {
			active = data.getBlockRotation() != null;
		}
		if (value.getButtonType() == ButtonType.block_replace_block) {
			active = data.isReplaceBlock();
		}

		if (value.getButtonType() == ButtonType.opposite_face_of_block) {
			active = data.isFlipFacing() != OppositeFacing.NONE;
			placeholderTextFromData = data.isFlipFacing().name().toLowerCase();
		}

		if (data.getMirrorLoc() != null) {
			if (value.getButtonType() == ButtonType.mirrorX) {
				active = data.getMirrorLoc().isMirrorX();
			}
			if (value.getButtonType() == ButtonType.mirrorY) {
				active = data.getMirrorLoc().isMirrorY();
			}
			if (value.getButtonType() == ButtonType.mirrorZ) {
				active = data.getMirrorLoc().isMirrorZ();
			}
			if (value.getButtonType() == ButtonType.mirrorXY) {
				active = data.getMirrorLoc().isMirrorXY();
			}
			if (value.getButtonType() == ButtonType.mirrorZY) {
				active = data.getMirrorLoc().isMirrorZY();
			}
			if (value.getButtonType() == ButtonType.mirrorXZ) {
				active = data.getMirrorLoc().isMirrorXZ();
			}
			if (value.getButtonType() == ButtonType.mirrorZX) {
				active = data.getMirrorLoc().isMirrorZX();
			}
		}
		PlaceholderText placeholderText = plugin.getLanguageCache().getLanguage().getPlaceholderText();
		String blockFaceNotSet = placeholderText != null ? placeholderText.getBlockFaceNotSet() : "";

		if (active) {
			String loc = data.getCenterLocation() != null ? SerializeingLocation.serializeLoc(data.getCenterLocation()) : "";
			if (value.getActive() != null)
				return TextConvertPlaceholders.convertTextItemstack(value.getActive().getItemStack(), loc, data.getBlockRotation() != null ? data.getBlockRotation().getRotation() : blockFaceNotSet, placeholderTextFromData);
			else
				return TextConvertPlaceholders.convertTextItemstack(value.getPassive().getItemStack(), loc, data.getBlockRotation() != null ? data.getBlockRotation().getRotation() : blockFaceNotSet, placeholderTextFromData);
		} else
			return TextConvertPlaceholders.convertTextItemstack(value.getPassive().getItemStack(), "", data.getBlockRotation() != null ? data.getBlockRotation().getRotation() : blockFaceNotSet, placeholderTextFromData);
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
				BlockMirror.getPlugin().getRunTask().setQueueTime(data.getEffectID(), -2);
				if (data.getCenterLocation() != null)
					BlockVisualize.visulizeBlock(data.getCenterLocation().getBlock(), data.getCenterLocation(), false);
				BlockVisualize.visulizeBlock(player.getLocation().getBlock(), player.getLocation(), false);
			} else {
				player.setMetadata(Actions.classic_set_block.name(), new FixedMetadataValue(BlockMirror.getPlugin(), Actions.classic_set_block));
				if (data.getCenterLocation() != null) {
					BlockMirror.getPlugin().getRunTask().setQueueTime(data.getEffectID(), -2);
					BlockVisualize.visulizeBlock(data.getCenterLocation().getBlock(), data.getCenterLocation(), true);
					SpawnBorderEffects spawnBorderEffects = new SpawnBorderEffects(null, player, data.getCenterLocation(), -1, 2.5);
					builder.setEffectID(spawnBorderEffects.getID());
					BlockMirror.getPlugin().getRunTask().addTask(spawnBorderEffects);
				}
			}
		}
		if (value.getButtonType() == ButtonType.CentreLoc) {
			if (click.isLeftClick()) {
				if (data.getCenterLocation() != null) {
					BlockMirror.getPlugin().getRunTask().setQueueTime(data.getEffectID(), -2);
					BlockVisualize.visulizeBlock(data.getCenterLocation().getBlock(), data.getCenterLocation(), false);
				}
				builder.setCenterLocation(player.getLocation());
				SpawnBorderEffects spawnBorderEffects = new SpawnBorderEffects(null, player, player.getLocation(), -1, 2.5);
				builder.setEffectID(spawnBorderEffects.getID());
				BlockMirror.getPlugin().getRunTask().addTask(spawnBorderEffects);
				BlockVisualize.visulizeBlock(player.getLocation().getBlock(), player.getLocation(), true);
			} else {
				if (data.getEffectID() > 0 && data.getCenterLocation() != null) {
					BlockVisualize.visulizeBlock(data.getCenterLocation().getBlock(), data.getCenterLocation(), false);
					builder.setCenterLocation(null);
					BlockMirror.getPlugin().getRunTask().setQueueTime(data.getEffectID(), -2);
					//BlockVisualize.visulizeBlock(player.getLocation().getBlock(), player.getLocation(), false);
				}
			}
		}

		if (value.getButtonType() == ButtonType.block_replace_block) {
			builder.setReplaceBlock(!data.isReplaceBlock());
		}

		if (value.getButtonType() == ButtonType.opposite_face_of_block) {
			OppositeFacing facingMode;
			if (click.isLeftClick())
				facingMode = data.isFlipFacing().next();
			else
				facingMode = data.isFlipFacing().previous();

			builder.setFlipFacing(facingMode);
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
			new SetBlockFace(data, null, Classic_Mirror_Settings, "set_blockface").menuOpen(player);
		}
		builder.setMirrorLoc(mirrorbuilder.build());
		BlockMirror.getPlugin().getPlayerCache().setPlayerData(player.getUniqueId(), builder.build());
		return true;
	}

	public ItemStack set(String icon) {
		if (icon.startsWith("uuid="))
			return SkullCreator.itemFromUuid(UUID.fromString(icon.replaceFirst("uuid=", "")));
		else if (icon.startsWith("base64="))
			return SkullCreator.itemFromBase64(icon.replaceFirst("base64=", ""));
		else if (icon.startsWith("url="))
			return SkullCreator.itemFromUrl(icon.replaceFirst("url=", ""));
		else if (icon.equals("Player_Skull") && player != null) {
			return SkullCreator.itemFromUuid(player.getUniqueId());
		}
		return null;
	}
}
