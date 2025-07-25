package org.brokenarrow.blockmirror.utily.effects;

import org.broken.arrow.library.menu.utility.ServerVersion;
import org.brokenarrow.blockmirror.api.utility.particels.CreateParticle;
import org.brokenarrow.blockmirror.api.utility.particels.ParticleDustOptions;
import org.brokenarrow.blockmirror.api.utility.particels.ParticleEffect;
import org.brokenarrow.blockmirror.api.utility.particels.ParticleEffectApi;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

public class SpawnBorderEffects implements HeavyLoad {

	private final Set<Location> location;
	private final Location containerLocation;
	private Player player;
	private long time;
	private final double milliPerTick;
	private int id;

	public SpawnBorderEffects(final Set<Location> location, final Player player, final Location centreLocation, final long showTime, final double milliPerTick) {
		this.player = player;
		this.containerLocation = centreLocation;
		this.milliPerTick = milliPerTick;
		if (showTime == -1)
			this.time = -1;
		else
			time = System.currentTimeMillis() + (1000L * (showTime != 0 ? showTime : 30));
		this.location = spawn();
		id = this.hashCode();
	}

	private void spawnEffects() {
		for (final Location location : this.location) {
			if (this.player != null) {
				ParticleEffectApi effect;
				if (ServerVersion.atLeast(ServerVersion.V1_13))
					effect = new ParticleEffect.Builder().setParticle(Particle.REDSTONE).setDustOptions(new ParticleDustOptions("0,0,0", 1)).build();
				else
					effect = new ParticleEffect.Builder().setEffect(Effect.valueOf("COLOURED_DUST")).build();
				if (effect != null) {
					final CreateParticle createParticle = new CreateParticle(player, effect, location);
					createParticle.create();
				}
			}
		}
	}

	@Override
	public void setTime(final long time) {
		this.time = time;
	}

	@Override
	public long getTime() {
		return this.time;
	}

	@Override
	public boolean compute() {
		spawnEffects();
		return false;
	}

	@Override
	public boolean reschedule() {
		if (rescheduleMaxRunTime() == -1)
			return true;
		else {
			if (rescheduleMaxRunTime() == -2)
				return false;
			return System.currentTimeMillis() <= rescheduleMaxRunTime();
		}
	}

	@Override
	public double getMilliPerTick() {
		return milliPerTick;
	}

	@Override
	public boolean computeWithDelay(final int conter) {
		return true;
	}

	@Override
	public long rescheduleMaxRunTime() {
		return time;
	}

	@Override
	public int getID() {
		return this.id;
	}

	public Set<Location> spawn() {
		Set<Location> location = new HashSet<>();
		Location centerLoc = new Location(this.containerLocation.getWorld(), this.containerLocation.getBlockX(), this.containerLocation.getBlockY(), this.containerLocation.getBlockZ());
		for (double x = 0; x <= 1; x += 1) {
			for (int y = 0; y <= 1; y++) {
				for (double z = 0; z <= 1; z += 1) {
					// Calculate the corner location
					Location corner = centerLoc.clone().add(x, y, z);
					location.add(corner);
				}
			}
		}
		for (int x = 0; x <= 1; x++) {
			for (int y = 0; y <= 1; y++) {
				Location edge1 = centerLoc.clone().add(x, y, 0.5);
				Location edge2 = centerLoc.clone().add(0.5, y, x);
				Location edge3 = centerLoc.clone().add(x, 0.5, y);
				location.add(edge1);
				location.add(edge2);
				location.add(edge3);
			}
		}
		return location;
	}

	public Set<Location> spawnEffects(Location location, String radie, double particleDistanceY, double particleDistanceX, double particleDistanceZ) {
		String[] radieSplited;
		double radieX = 0;
		double radieY = 0;
		double radieZ = 0;
		double minradieX = 0;
		double minradieY = 0;
		double minradieZ = 0;
		int radiesLengt = (radieSplited = radie.split(",")).length;
		Location locationCloned = location;

		if (radiesLengt == 3) {
			radieX = (Double.parseDouble(radieSplited[0]));
			radieY = (Double.parseDouble(radieSplited[1]));
			radieZ = (Double.parseDouble(radieSplited[2]));
		} else if (radie.startsWith("-1")) {
			locationCloned = getCentrumBlock(location.clone(), -0.5, false);
			int chunkSize = 0;

			if (radie.contains(",")) {
				String[] radiesplited = radie.split(",");
				if (radiesplited.length == 2) {
					String calculateChunkSize = radiesplited[1];
					chunkSize = Integer.parseInt(calculateChunkSize);
				}
			}

			radieX = chunkSize > 0 ? 8 + (8 * chunkSize) : 8;
			radieY = location.getWorld().getMaxHeight();
			radieZ = chunkSize > 0 ? 8 + (8 * chunkSize) : 8;

		}


		Location corner1 = locationCloned.clone().add(radieX + 0.5, radieY, radieZ + 0.5);
		Location corner2 = locationCloned.clone().subtract(radieX - 0.5, radieY, radieZ - 0.5);

		double particleDistance = 2.0;
		double particleDistancex = 2.0;
		double particleDistancez = 2.0;

		Set<Location> result = new HashSet<>();
		World world = corner1.getWorld();
		double minX = Math.min(corner1.getX(), corner2.getX());
		double minY = Math.min(corner1.getY(), corner2.getY());
		double minZ = Math.min(corner1.getZ(), corner2.getZ());
		double maxX = Math.max(corner1.getX(), corner2.getX());
		double maxY = Math.max(corner1.getY(), corner2.getY());
		double maxZ = Math.max(corner1.getZ(), corner2.getZ());

		for (double x = minX; x <= maxX; x += particleDistancex) {
			for (double y = minY; y <= maxY; y += particleDistance) {
				for (double z = minZ; z <= maxZ; z += particleDistancez) {
					int components = 0;
					if (x == minX || x == maxX) components++;
					if (y == minY || y == maxY) components++;
					if (z == minZ || z == maxZ) components++;
					if (components >= 2) {
						result.add(new Location(world, x, y, z));

					}
				}
			}
		}
		return result;
	}

	public Location getCentrumBlock(final Location location, final Double yloc, final boolean offCenter) {
		return location.clone().add(((location.getChunk().getX() * 16) - location.getBlockX()) + (offCenter ? 7.5 : 8), yloc != null ? yloc : -0.5, ((location.getChunk().getZ() * 16) - location.getBlockZ()) + (offCenter ? 7.5 : 8));
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;

		SpawnBorderEffects that = (SpawnBorderEffects) o;
		return time == that.time && Double.compare(milliPerTick, that.milliPerTick) == 0 && Objects.equals(location, that.location) && Objects.equals(containerLocation, that.containerLocation) && Objects.equals(player, that.player);
	}

	@Override
	public int hashCode() {
		int result = Objects.hashCode(location);
		result = 31 * result + Objects.hashCode(containerLocation);
		result = 31 * result + Objects.hashCode(player);
		result = 31 * result + Long.hashCode(time);
		result = 31 * result + Double.hashCode(milliPerTick);
		result = 31 * result + new Random().nextInt(Integer.MAX_VALUE - 7000);
		return result;
	}
}
