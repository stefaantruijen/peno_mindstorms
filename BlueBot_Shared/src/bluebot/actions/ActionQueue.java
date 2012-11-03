package bluebot.actions;


import bluebot.Driver;
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
		}
		
		final Action action = this.action;
		if (action != null) {
			action.abort();
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
				action = queue.pull();
				if (!action.isAborted()) {
					action.execute(driver);
				}
			} catch (final InterruptedException e) {
				// ignored
			} finally {
				// Clear the interrupted flag
				// to avoid interrupting a new action
				Thread.interrupted();
			}
		}
	}
	
}