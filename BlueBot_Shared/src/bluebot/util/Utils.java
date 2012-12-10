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
		return (float)(clampAngleDegrees(angle) * Constants.RADIANS_PER_DEGREE);
	}
	
	/**
	 * Compares two <code>float</code> values for equality
	 * 
	 * @param a - the first value
	 * @param b - the second value
	 * 
	 * @return <code>TRUE</code> if <b>a</b> and <b>b</b> are equal,
	 * 			<code>FALSE</code> otherwise
	 */
	public static final boolean equals(final float a, final float b) {
		return (Float.floatToIntBits(a) == Float.floatToIntBits(b));
	}
	
	/**
	 * Formats a (time) duration
	 * 
	 * @param duration - a duration (in ms)
	 * 
	 * @return (h+:)?mm:ss
	 */
	public static final String formatDuration(long duration) {
		duration /= 1000;
		final int sec = (int)(duration % 60);
		duration /= 60;
		final int min = (int)(duration % 60);
		duration /= 60;
		final int hrs = (int)duration;
		
		final StringBuilder sb = new StringBuilder();
		if (hrs > 0) {
			sb.append(hrs).append(':');
		}
		if (min < 10) {
			sb.append('0');
		}
		sb.append(min).append(':');
		if (sec < 10) {
			sb.append('0');
		}
		return sb.append(sec).toString();
	}
	
}