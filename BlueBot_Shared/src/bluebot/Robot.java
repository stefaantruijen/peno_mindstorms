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
	
	public final static double DEFAULT_SPEED_ROTATE = 75;
	public final static double DEFAULT_SPEED_TRAVEL = 300;
	public static final int DEFAULT_ACCELERATION = 100;
	
	
	
	/**
	 * Returns the heading of the robot
	 * 
	 * @return a <code>float</code> value representing the current heading (in degrees)
	 */
	public float getHeading();
	
	/**
	 * Returns the orientation of the robot
	 * 
	 * @return an {@link Orientation} object
	 */
	public Orientation getOrientation();
	
	/**
	 * Returns the X coordinate of the robot
	 * 
	 * @return a <code>float</code> value representing the position on the X axis
	 */
	public float getX();
	
	/**
	 * Returns the Y coordinate of the robot
	 * 
	 * @return a <code>float</code> value representing the position on the Y axis
	 */
	public float getY();
	
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
	
	//TODO: make a setRotateSpeed();
	public void turnHeadCWise(int offset);
	
	public void turnHeadCCWise(int offset);
	public float getAngleIncrement();
	//TODO: getDistanceIncrement()? 
	
	//TODO: getPosition()
	//TODO: getHeading()
}