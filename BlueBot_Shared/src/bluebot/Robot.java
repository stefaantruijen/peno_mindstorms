package bluebot;

import bluebot.util.Orientation;



/**
 * Represents a robot
 * 
 * TODO: Allow to send Debug info/messages?
 * 
 * @author Ruben Feyen
 */
public interface Robot extends Mobile {
	
	/**
	 * Returns the orientation of the robot
	 * 
	 * @return an {@link Orientation} object
	 */
	public Orientation getOrientation();
	
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
	 * @param speed - the desired travel speed (mm/s)
	 */
	public void setTravelSpeed(double speed);
	
	//TODO: make a setRotateSpeed();

	public float getAngleIncrement();
	//TODO: getDistanceIncrement()? 
	
	//TODO: getPosition()
	//TODO: getHeading()
}