package org.brokenarrow.blockmirror.tasks;

import org.brokenarrow.blockmirror.BlockMirror;
import org.brokenarrow.blockmirror.utily.effects.HeavyLoad;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class RunTasks extends BukkitRunnable {

	private static int taskIDNumber = -1;
	private int amount = 0;
	private final boolean async;
	private static final double MAX_MS_PER_TICK = 3.5;
	private static final Queue<HeavyLoad> workloadDeque = new ConcurrentLinkedDeque<>();
	private static final Map<Object, List<Long>> map = new HashMap<>();
	private static final Map<Integer, Long> changeQueueTime = new HashMap<>();
	private HeavyLoad firstReschudleElement;

	public RunTasks(boolean async) {
		this.async = async;
		if (async)
			taskIDNumber = runTaskTimerAsynchronously(BlockMirror.getPlugin(), 0L, 20L * 3).getTaskId();
		else
			taskIDNumber = runTaskTimer(BlockMirror.getPlugin(), 0L, 20L).getTaskId();
	}

	public void start() {
		if (Bukkit.getScheduler().isCurrentlyRunning(taskIDNumber) || Bukkit.getScheduler().isQueued(taskIDNumber))
			Bukkit.getScheduler().cancelTask(taskIDNumber);
		new RunTasks(this.async);
	}

	public boolean isContainsMaxAmountInQueue(Object object, int maxAmountIncache) {
		return map.containsKey(object) && map.get(object).size() >= (maxAmountIncache == 0 ? 1 : maxAmountIncache);
	}

	/**
	 * Add a task to preform, this will not limit amount of task you add.
	 *
	 * @param task you want to add.
	 * @return true if it can add the task.
	 */
	public boolean addTask(HeavyLoad task) {
		return workloadDeque.add(task);
		//test.add(task);
	}

	/**
	 * Set new time on queue task.
	 *
	 * @param id      on the class.
	 * @param newTime the new time you want to set.
	 */
	public void setQueueTime(int id, long newTime) {
		changeQueueTime.put(id, newTime);
		//test.add(task);
	}

	/**
	 * Add a task and limit amount you want it to add of same type.
	 * You limit it with the taskName and amount of same task max it can hold.
	 *
	 * @param taskName         the name of the task.
	 * @param timeBeforeRemove the time in seconds when it shall remove the task.
	 * @param maxAmountIncache amount of same task name you want it to run the task
	 * @param task             the task you want it to execute.
	 * @return true if it can add the task.
	 */
	public boolean addTask(Object taskName, long timeBeforeRemove, int maxAmountIncache, HeavyLoad task) {
		List<Long> longList = map.get(taskName);
		if (!isContainsMaxAmountInQueue(taskName, maxAmountIncache)) {
			if (longList == null)
				longList = new ArrayList<>();

			longList.add(System.currentTimeMillis() + (timeBeforeRemove * 1000));
			map.put(taskName, longList);
			this.addTask(task);
			return true;
		}
		return false;
	}

	private boolean cumputeTasks(HeavyLoad task) {
		if (task != null) {
			if (task.reschedule()) {
				Long newTime = changeQueueTime.get(task.getID());
				if (newTime != null) {
					task.setTime(newTime);
				}
				if(task.getTime() < 0)
					return false;

				addTask(task);
				if (firstReschudleElement == null) {
					firstReschudleElement = task;
				} else {
					return firstReschudleElement != task;
				}
			}
		}
		return false;
	}

	private boolean delayTasks(HeavyLoad task, int amount) {
		if (task != null)
			return task.computeWithDelay(amount);
		return false;
	}

	@Override
	public void run() {
		checkIfTimeFinish();
		long stoptime = (long) (System.nanoTime() + (1000_000 * MAX_MS_PER_TICK));

		while (!workloadDeque.isEmpty() && System.nanoTime() <= stoptime) {
			HeavyLoad heavyload = workloadDeque.poll();
			if (!delayTasks(heavyload, amount)) continue;
			heavyload.computeTask();
			if (cumputeTasks(heavyload)) break;
		}
		this.amount++;

	}

	public void checkIfTimeFinish() {
		if (this.amount % 20 == 0) {
			map.forEach((key, value) -> value.removeIf(time -> System.currentTimeMillis() >= time));
		}

	}

}
