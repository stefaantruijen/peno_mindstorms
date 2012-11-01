package bluebot;



/**
 * Represents the logic for handling a {@link Robot}
 * 
 * @author Ruben Feyen
 */
public interface Driver extends Mobile {
	
	/**
	 * Calibrates the sensors
	 */
	public void calibrate();
	
	/**
	 * Executes the "white line" algorithm
	 */
	public void doWhiteLineOrientation();
	
}