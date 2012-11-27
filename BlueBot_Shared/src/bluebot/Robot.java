package bluebot;

import java.io.File;



/**
 * Represents a robot
 */
public interface Robot extends Mobile {
	
	public final static double DEFAULT_SPEED_ROTATE = 75;
	public final static double DEFAULT_SPEED_TRAVEL = 0;
	public static final int DEFAULT_ACCELERATION = 500;
	public static final float OFFSET_SENSOR_LIGHT = 70F;
	public static final float OFFSET_SENSOR_ULTRASONIC = 30F;
	
	
	
	/**
	 * Returns the heading of the robot
	 * 
	 * @return a <code>float</code> value representing the current heading (in degrees)
	 */
	public float getHeading();
	
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
	 * Corrects the internal orientation (if necessary)
	 */
	public void modifyOrientation();
	
	/**
	 * Returns the current value of the light sensor
	 * 
	 * @return an <code>int</code> from the interval [0, 100]
	 */
	public int readSensorLight();
	
	/**
	 * Returns the current value of the light sensor
	 * 
	 * @return an <code>int</code> from the interval [0, 1036]
	 */
	public int readSensorLightValue();
	
	/**
	 * Returns the current value of the ultrasonic sensor
	 * 
	 * @return an <code>int</code> from the interval [0, 255]
	 */
	public int readSensorUltraSonic();
	
	//TODO: make a setRotateSpeed();
	public float getAngleIncrement();
	//TODO: getDistanceIncrement()? 

	public float getArcLimit();

	/**
	 * plays the given wav file
	 * @param file
	 */
	public void playSound(File file);
	
}