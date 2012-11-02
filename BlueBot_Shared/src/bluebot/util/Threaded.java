package bluebot.util;



/**
 * 
 * @author Ruben Feyen
 */
public abstract class Threaded implements Runnable {
	
	private final Object lock = new Object();
	
	private Thread thread;
	
	
	
	public void start() {
		synchronized (lock) {
			if (thread == null) {
				thread = new Thread(this);
				thread.setDaemon(true);
				thread.start();
			}
		}
	}
	
	public synchronized void stop() {
		synchronized (lock) {
			if (thread != null) {
				thread.interrupt();
				thread = null;
			}
		}
	}
	
}