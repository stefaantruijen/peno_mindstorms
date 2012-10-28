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
	
	/**
	 * Sets the speed to high
	 */
	public void setSpeedHigh();
	
	/**
	 * Sets the speed to low
	 */
	public void setSpeedLow();
	
	/**
	 * Sets the speed to medium
	 */
	public void setSpeedMedium();
	
}