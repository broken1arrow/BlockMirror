package org.brokenarrow.blockmirror.api.utility;

import org.bukkit.Location;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;


public final class RandomUntility extends Random {

	private static Random random;
	private static final AtomicLong seedUniquifier
			= new AtomicLong(8682522807148012L);

	public RandomUntility() {
		random = new Random(seedUniquifier() ^ System.nanoTime());
	}

	private static long seedUniquifier() {
		// L'Ecuyer, "Tables of Linear Congruential Generators of
		// Different Sizes and Good Lattice Structure", 1999
		for (; ; ) {
			long current = seedUniquifier.get();
			long next = current * 1181783497276652981L;
			if (seedUniquifier.compareAndSet(current, next))
				return next;
		}
	}

	public void newRandomsSeed() {
		random = new Random(seedUniquifier() ^ System.nanoTime());
	}

	public int randomIntNumber(int origin, int bound) {
		return randomIntNumber(origin, bound, false);
	}

	public int randomIntNumber(int origin, int bound, boolean newRandomsSeed) {
		if (newRandomsSeed)
			newRandomsSeed();
		return origin + nextRandomInt(bound - origin + 1);
	}

	public int randomIntNumber(int origin, boolean newRandomsSeed) {
		if (newRandomsSeed)
			newRandomsSeed();
		return nextRandomInt(origin + 1);
	}

	public boolean chance(final double percent) {
		return chance(percent, false);
	}

	public boolean chance(final int percent) {
		return chance(percent, false);
	}

	public boolean chance(final double percent, boolean newRandomsSeed) {
		if (newRandomsSeed)
			newRandomsSeed();
		if (percent == 100)
			return true;
		return random.nextDouble() * 100D < percent;
	}

	public Location nextLocation(Location origin, double radius, boolean is3D) {
/*		double randomRadius = random.nextDouble() * radius;
		double theta = Math.toRadians(random.nextDouble() * 360.0D);
		double phi = Math.toRadians(random.nextDouble() * 180.0D - 90.0D);

		double x = randomRadius * Math.cos(theta) * Math.sin(phi);
		double z = randomRadius * Math.cos(phi);*/

		return nextLocation(origin, 0, radius, is3D);
	}

	public Location nextLocation(Location origin, double minRadius, double maxRadius, boolean is3D) {
/*		double randomRadius = minRadius + (random.nextDouble() * (maxRadius - minRadius));
		double theta = Math.toRadians(random.nextDouble() * 360.0D);
		double phi = Math.toRadians(random.nextDouble() * 180.0D - 90.0D);

		double x = randomRadius * Math.cos(theta) * Math.sin(phi);
		double z = randomRadius * Math.cos(phi);

		return origin.clone().add(x, is3D ? randomRadius * Math.sin(theta) * Math.cos(phi) : 0.0D, z);*/
		final double rectX = random.nextDouble() * (maxRadius - minRadius) + minRadius;
		final double rectZ = random.nextDouble() * (maxRadius + minRadius) - minRadius;
		final double offsetX;
		final double offsetZ;
		double offsetY = 0;
		final int transform = random.nextInt(4);
		if (is3D) {
			final double rectY = random.nextDouble() * (maxRadius + minRadius) - minRadius;
			offsetY = getYCords(false, transform, rectY);
		}
		if (transform == 0) {
			offsetX = rectX;
			offsetZ = rectZ;
		} else if (transform == 1) {
			offsetX = -rectZ;
			offsetZ = rectX;
		} else if (transform == 2) {
			offsetX = -rectX;
			offsetZ = -rectZ;
		} else {
			offsetX = rectZ;
			offsetZ = -rectX;
		}

		return origin.clone().add(offsetX, offsetY, offsetZ);
	}

	public double getYCords(boolean onlyBelow, int transform, double rectY) {
		double offsetY;
		if (!onlyBelow) {
			double nextY = random.nextDouble();
			if (transform < 2) {
				offsetY = nextY >= 0.5 ? -rectY : rectY;
			} else {
				offsetY = nextY >= 0.5 ? rectY : -rectY;
			}
		} else {
			offsetY = -rectY;
		}

		return offsetY;
	}

	public int nextRandomInt(int bound) {
		if (bound < 0)
			bound = 1;
		return random.nextInt(bound);
	}
}
