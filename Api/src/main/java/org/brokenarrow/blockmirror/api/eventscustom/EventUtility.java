package org.brokenarrow.blockmirror.api.eventscustom;

import org.bukkit.Bukkit;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import javax.annotation.Nonnull;

/**
 * All events require a static method named getHandlerList() which returns the same {@link HandlerList} as {@link #getHandlers()}.
 */

public abstract class EventUtility extends Event implements Cancellable {


	private final HandlerList handler;

	/**
	 * The default constructor is defined for cleaner code. This constructor
	 * assumes the event is synchronous.
	 *
	 * @param handler the handler for your event.
	 */
	public EventUtility(final HandlerList handler) {
		this(handler, false);
	}

	/**
	 * This constructor is used to explicitly declare an event as synchronous
	 * or asynchronous.
	 *
	 * @param isAsync true indicates the event will fire asynchronously, false
	 *                by default from default constructor.
	 * @param handler the handler for your event.
	 */
	public EventUtility(final HandlerList handler, final boolean isAsync) {
		super(isAsync);
		this.handler = handler;
	}

	public void registerEvent() {
		Bukkit.getPluginManager().callEvent(this);
	}

	/**
	 * Gets the cancellation state of this event. A cancelled event will not
	 * be executed in the server, but will still pass to other plugins
	 *
	 * @return true if this event is cancelled
	 * @apiNote You need override this method, if you want to check if event is cancelled.
	 */
	@Override
	public boolean isCancelled() {
		return false;
	}

	/**
	 * <p>
	 * This method sets the cancellation state of this event. A cancelled event will not
	 * be executed in the server, but will still pass to other plugins.
	 *
	 * @param cancel true if you wish to cancel this event
	 * @apiNote You need override this method, if you want to cancel the event.
	 */
	@Override
	public void setCancelled(final boolean cancel) {
	}

	@Override
	@Nonnull
	public HandlerList getHandlers() {
		return handler;
	}

}