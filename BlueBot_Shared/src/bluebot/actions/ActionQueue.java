package bluebot.actions;


import bluebot.Driver;
import bluebot.DriverException;
import bluebot.util.BlockingQueue;
import bluebot.util.Threaded;



/**
 * The {@link Action} queue for the {@link Driver}
 * 
 * @author Ruben Feyen
 */
public class ActionQueue extends Threaded {
	
	private final Object lock = new Object();
	
	private Action action;
	private Driver driver;
	private BlockingQueue<Action> queue;
	
	
	public ActionQueue(final Driver driver) {
		this.driver = driver;
		this.queue = new BlockingQueue<Action>();
	}
	
	
	
	/**
	 * Clears all queued actions and aborts the current action, if any
	 */
	public void abort() {
		synchronized (lock) {
			queue.clear();
			if (action != null) {
				action.abort();
				action = null;
			}
		}
	}
	
	/**
	 * Queues an action
	 * 
	 * @param action - the {@link Action} to be queued
	 */
	public void queue(final Action action) {
		synchronized (lock) {
			queue.push(action);
		}
	}
	
	public void run() {
		for (;;) {
			try {
				final Action action = queue.pull();
				synchronized (lock) {
					if (action.isAborted()) {
						continue;
					} else {
						this.action = action;
					}
				}
				action.execute(driver);
			} catch (final ActionException e) {
				driver.sendError(e.getMessage());
			} catch (final DriverException e) {
				driver.sendError(e.getMessage());
			} catch (final InterruptedException e) {
				// ignored
			} catch (final Exception e) {
				// Prevent actions from crashing the queue
				e.printStackTrace();
			} finally {
				// Clear the interrupted flag
				// to avoid interrupting a new action
				Thread.interrupted();
			}
		}
	}
	
}