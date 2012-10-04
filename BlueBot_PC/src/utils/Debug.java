package utils;


import java.util.logging.Logger;



/**
 * 
 * @author Ruben Feyen
 */
public final class Debug {
	
	private static final boolean USE_LOGGER = false;
	
	
	private Debug() {
		// disabled
	}
	
	
	
	private static final Logger getLogger() {
		return Logger.getLogger("DEBUG");
	}
	
	public static void print(final String msg) {
		System.out.println(msg);
		if (USE_LOGGER) {
			getLogger().info(msg);
		}
	}
	
	public static void print(final String format, final Object... args) {
		print(String.format(format, args));
	}
	
}