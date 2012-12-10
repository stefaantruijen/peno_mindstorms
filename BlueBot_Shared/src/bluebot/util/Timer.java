package bluebot.util;



/**
 * Utility class for keeping track of time
 * 
 * @author Ruben Feyen
 */
public class Timer {
	
	private long t = System.currentTimeMillis();
	
	
	
	public long read() {
		return (System.currentTimeMillis() - t);
	}
	
	public void reset() {
		t = System.currentTimeMillis();
	}
	
}