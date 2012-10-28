package bluebot.util;



/**
 * This class provides various utility methods
 * 
 * @author Ruben Feyen
 */
public final class Utils {
	
	private Utils() {
		// disabled
	}
	
	
	
	/**
	 * Clamps the value of an angle
	 * 
	 * @param angle - the angle (in degrees)
	 * 
	 * @return a <code>float</code> value from the interval [0, 360)
	 */
	public static final float clampAngleDegrees(float angle) {
		for (; angle < 0F; angle += 360F);
		for (; angle >= 360F; angle -= 360F);
		return angle;
	}
	
	/**
	 * Converts an angle from degrees to radians
	 * 
	 * @param angle - the angle (in degrees)
	 * 
	 * @return the angle (in radians)
	 */
	public static final float degrees2radians(final float angle) {
		return (float)(Math.PI / 180D * clampAngleDegrees(angle));
	}
	
}