package org.brokenarrow.blockmirror.utily.blockVisualization;

import org.broken.lib.rbg.TextTranslator;
import org.brokenarrow.blockmirror.BlockMirror;
import org.brokenarrow.menu.library.dependencies.nbt.nbtapi.NBTEntity;
import org.brokenarrow.menu.library.utility.ServerVersion;
import org.brokenarrow.menu.library.utility.Validate;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class BlockVisualizerCache {
	private final Map<Location, VisualizeData> visualizedBlocks = new ConcurrentHashMap<>();
	private final VisualTask visualTask;

	public BlockVisualizerCache() {
		this.visualTask = new VisualTask();
	}

	public void visualize(final Player viwer, @NotNull final Block block, final Material mask, final String text) {
		if (block == null) {
			throw new NullPointerException("block is marked non-null but is null");
		} else {
			if (isVisualized(block)) {
				stopVisualizing(block);
				getVisualTask().stop();
			}
			Validate.checkBoolean(isVisualized(block), "Block at " + block.getLocation() + " already visualized");
			final Location location = block.getLocation();
			final FallingBlock falling = spawnFallingBlock(location, mask, text);
			final VisualizeData visualizeData = new VisualizeData(viwer, falling, text, mask);
			visualizeData.setRemoveIfAir(false);
			visualizeData.setStopIfAir(false);
			final Iterator<Player> players = block.getWorld().getPlayers().iterator();
			if (viwer == null) {
				while (players.hasNext()) {
					final Player player = players.next();
					visualizeData.addPlayersAllowed(player);
					sendBlockChange(2, player, location, ServerVersion.olderThan(ServerVersion.v1_9) ? mask : Material.BARRIER);
				}
			} else {
				while (players.hasNext()) {
					final Player player = players.next();
					if (player.hasPermission("cch.admin.block_visualize") || player.getUniqueId().equals(viwer.getUniqueId())) {
						visualizeData.addPlayersAllowed(player);
						sendBlockChange(2, player, location, ServerVersion.olderThan(ServerVersion.v1_9) ? mask : Material.BARRIER);
					}
				}
			}
			visualizedBlocks.put(location, visualizeData);
		}
	}

	private FallingBlock spawnFallingBlock(final Location location, final Material mask, final String text) {
		if (ServerVersion.olderThan(ServerVersion.v1_9)) {
			return null;
		} else {
			final FallingBlock falling = spawnFallingBlock(location.clone().add(0.5D, 0.0, 0.5D), mask);
			falling.setDropItem(false);
			falling.setVelocity(new Vector(0, 0, 0));
			setCustomName(falling, text);
			apply(falling, true);
			return falling;
		}
	}

	public void stopVisualizing(@Nonnull final Block block) {
		if (block == null) {
			throw new NullPointerException("block is marked non-null but is null");
		} else {
			Validate.checkBoolean(!isVisualized(block), "Block at " + block.getLocation() + " not visualized");
			final VisualizeData visualizeData = visualizedBlocks.remove(block.getLocation());
			final FallingBlock fallingBlock = visualizeData.getFallingBlock();

			if (fallingBlock != null) {
				fallingBlock.remove();
			}
			final Set<Player> playersAllowed = visualizeData.getPlayersAllowed();
			final Iterator<Player> players;
			if (playersAllowed != null && !playersAllowed.isEmpty()) players = playersAllowed.iterator();
			else players = block.getWorld().getPlayers().iterator();
			while (players.hasNext()) {
				final Player player = players.next();
				sendBlockChange(1, player, block.getLocation().getBlock());
			}
		}

	}

	public void stopAll() {
		for (final Location location : visualizedBlocks.keySet()) {
			final Block block = location.getBlock();
			if (isVisualized(block)) {
				stopVisualizing(block);
			}
		}

	}

	public boolean isVisualized(@Nonnull final Block block) {
		if (block == null) {
			throw new NullPointerException("block is marked non-null but is null");
		} else {
			return visualizedBlocks.containsKey(block.getLocation());
		}
	}

	private static void setCustomName(final Entity en, final String name) {
		try {
			en.setCustomNameVisible(true);
			if (name != null && !name.equals(""))
				en.setCustomName(TextTranslator.toSpigotFormat(name));
		} catch (final NoSuchMethodError ignored) {
		}

	}

	public static void sendBlockChange(final int delayTicks, final Player player, final Location location, final Material material) {
		if (delayTicks > 0) {
			BlockMirror.runTaskLater(delayTicks, () -> {
				sendBlockChange0(player, location, material);
			}, false);
		} else {
			sendBlockChange0(player, location, material);
		}

	}

	public static void sendBlockChange(final int delayTicks, final Player player, final Block block) {
		if (delayTicks > 0) {
			BlockMirror.runTaskLater(delayTicks, () -> {
				sendBlockChange0(player, block);
			}, false);
		} else {
			sendBlockChange0(player, block);
		}

	}

	private static void sendBlockChange0(final Player player, final Block block) {
		try {
			player.sendBlockChange(block.getLocation(), block.getBlockData());
		} catch (final NoSuchMethodError var3) {
			player.sendBlockChange(block.getLocation(), block.getType(), block.getData());
		}

	}

	private static void sendBlockChange0(final Player player, final Location location, final Material material) {
		try {
			player.sendBlockChange(location, material.createBlockData());
		} catch (final NoSuchMethodError var4) {
			player.sendBlockChange(location, material, (byte) material.getId());
		}

	}

	public static void apply(@Nonnull final Object instance, final boolean key) {
	/*	if (instance instanceof FallingBlock) {
			FallingBlock entity = ((FallingBlock) instance);
			if (ServerVersion.olderThan(ServerVersion.v1_13)) {
				menulibrary.dependencies.nbt.nbtapi.NBTEntity nbtEntity = new menulibrary.dependencies.nbt.nbtapi.NBTEntity((Entity) instance);
				nbtEntity.setInteger("NoGravity", key ? 0 : 1);
				entity.setGlowing(key);
			}else {
				System.out.println("instance  fffffff");
				entity.setGravity(!key);
				entity.setGlowing(key);
			}
		}*/
		if (instance instanceof Entity) {
			final Entity entity = ((Entity) instance);
			if (ServerVersion.olderThan(ServerVersion.v1_13)) {
				final NBTEntity nbtEntity = new NBTEntity((Entity) instance);
				nbtEntity.setInteger("NoGravity", !key ? 0 : 1);
				entity.setGlowing(key);
			} else {
				entity.setGravity(!key);
				entity.setGlowing(key);
			}
		}
	}

	private static FallingBlock spawnFallingBlock(final Location loc, final Material material) {
		return spawnFallingBlock(loc, material, (byte) 0);
	}

	private static FallingBlock spawnFallingBlock(final Location loc, final Material material, final byte data) {
		if (loc.getWorld() == null) return null;

		if (ServerVersion.atLeast(ServerVersion.v1_13)) {
			if (ServerVersion.atLeast(ServerVersion.v1_16))
				return loc.getWorld().spawnFallingBlock(loc, material.createBlockData());
			else return loc.getWorld().spawnFallingBlock(loc, material, data);
		} else {
			try {
				return (FallingBlock) loc.getWorld().getClass().getMethod("spawnFallingBlock", Location.class, Integer.TYPE, Byte.TYPE).invoke(loc.getWorld(), loc, material.getId(), data);
			} catch (final ReflectiveOperationException var4) {
				var4.printStackTrace();
				return null;
			}
		}
	}

	public VisualTask getVisualTask() {
		return this.visualTask;
	}

	public final class VisualTask extends BukkitRunnable {
		private BukkitTask task;
		private int taskID = -1;

		private VisualTask() {
		}

		public void start() {
			if (task == null) {
				task = runTaskTimer(BlockMirror.getPlugin(), 0L, 40L);
				taskID = task.getTaskId();
			}
		}

		public void stop() {
		}

		public boolean isCancel() {
			return this.isCancelled();
		}

		@Override
		public void run() {
			if (visualizedBlocks.isEmpty()) {
				stop();
				return;
			}
			for (final Map.Entry<Location, VisualizeData> visualizeBlocks : visualizedBlocks.entrySet()) {
				final Location location = visualizeBlocks.getKey();
				final VisualizeData visualizeData = visualizeBlocks.getValue();
				if (ServerVersion.newerThan(ServerVersion.v1_9) && (location.getBlock().getType() == Material.AIR)) {
					if (visualizeData.isRemoveIfAir()) {
						stopVisualizing(location.getBlock());
					}
					if (visualizeData.isStopIfAir())
						continue;
				}


				if (visualizeData.getViwer() == null)
					for (final Player player : visualizeData.getPlayersAllowed())
						visualize(player, location.getBlock(), visualizeData.getMask(), visualizeData.getText());
				else
					visualize(visualizeData.getViwer(), location.getBlock(), visualizeData.getMask(), visualizeData.getText());
			}
		}
	}

	public static final class VisualizeData {
		private final Player viwer;
		private final Set<Player> playersAllowed;
		private final FallingBlock fallingBlock;
		private final String text;
		private final Material mask;
		private boolean stopIfAir;
		private boolean removeIfAir;

		public VisualizeData(final Player viwer, final FallingBlock fallingBlock, final String text, final Material mask) {
			this(viwer, new HashSet<>(), fallingBlock, text, mask);
		}

		public VisualizeData(final Player viwer, final Set<Player> playersAllowed, final FallingBlock fallingBlock, final String text, final Material mask) {
			this.viwer = viwer;
			this.playersAllowed = playersAllowed;
			this.fallingBlock = fallingBlock;
			this.text = text;
			this.mask = mask;
		}

		public void setStopIfAir(final boolean stopIfAir) {
			this.stopIfAir = stopIfAir;
		}

		public void setRemoveIfAir(final boolean removeIfAir) {
			this.removeIfAir = removeIfAir;
		}

		public void addPlayersAllowed(final Player viwer) {
			playersAllowed.add(viwer);
		}

		public Player getViwer() {
			return viwer;
		}

		public Set<Player> getPlayersAllowed() {
			return playersAllowed;
		}

		public FallingBlock getFallingBlock() {
			return fallingBlock;
		}

		public String getText() {
			return text;
		}

		public Material getMask() {
			return mask;
		}

		public boolean isStopIfAir() {
			return stopIfAir;
		}

		public boolean isRemoveIfAir() {
			return removeIfAir;
		}
	}
}