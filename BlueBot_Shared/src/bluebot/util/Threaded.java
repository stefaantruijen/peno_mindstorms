package bluebot.util;



/**
 * 
 * @author Ruben Feyen
 */
public abstract class Threaded implements Runnable {
	
	private final Object lock = new Object();
	
	private Thread thread;
	
	
	
	public void interrupt() {
		try {
			thread.interrupt();
		} catch (final NullPointerException e) {
			throw new RuntimeException("The thread has not been started");
		}
	}
	
	public void start() {
		synchronized (lock) {
			if (thread == null) {
				thread = new Thread(this);
				thread.setDaemon(true);
				thread.start();
			}
		}
	}
	
	public void stop() {
		synchronized (lock) {
			if (thread != null) {
				thread.interrupt();
				thread = null;
			}
		}
	}
	
}