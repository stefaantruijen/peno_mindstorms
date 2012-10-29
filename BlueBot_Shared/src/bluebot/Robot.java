package bluebot;



/**
 * Represents a robot
 * 
 * @author Ruben Feyen
 */
public interface Robot extends Mobile {
	
	/**
	 * Determines whether or not the robot is currently moving
	 * 
	 * @return <code>TRUE</code> if the robot is moving,
	 * 			<code>FALSE</code> otherwise
	 */
	public boolean isMoving();
	
	/**
	 * Returns the current value of the light sensor
	 * 
	 * @return an <code>int</code> from the interval [0, 100]
	 */
	public int readSensorLight();
	
	/**
	 * Returns the current value of the ultrasonic sensor
	 * 
	 * @return an <code>int</code> from the interval [0, 255]
	 */
	public int readSensorUltraSonic();
	
	/**
	 * Sets the travel speed
	 * 
	 * @param speed - the desired travel speed (in [wheel diameter] units/s)
	 */
	public void setTravelSpeed(double speed);

	public float getAngleIncrement();
	
}