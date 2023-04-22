package org.brokenarrow.blockmirror.utily.effects;

public interface HeavyLoad {

	boolean compute();

	default void computeTask() {
		if (!this.compute())
			return;
		final long stoptime = (long) (System.nanoTime() + (1000_000 * this.getMilliPerTick()));
		while (System.nanoTime() <= stoptime) {
			if (!this.compute())
				break;
		}
	}

	void setTime(final long time);

	long getTime();

	int getID();

	default boolean reschedule() {
		return false;
	}

	default boolean computeWithDelay(int conter) {
		return false;
	}

	default double getMilliPerTick() {
		return 4.5;
	}

	default long rescheduleMaxRunTime() {
		return 0;
	}

}
