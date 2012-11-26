package bluebot;

import bluebot.util.Orientation;



/**
 * Represents a mobile entity
 * 
 * @author Ruben Feyen
 */
public interface Mobile {
	
	/**
	 * Returns the current orientation
	 * 
	 * @return an {@link Orientation} object
	 */
	public Orientation getOrientation();
	
	/**
	 * Returns the current speed (percentage)
	 * 
	 * @return an <code>int</code> from the range [0, 100]
	 */
	public int getSpeed();
	
	/**
	 * Determines whether or not a movement is in progress
	 * 
	 * @return <code>TRUE</code> if moving, <code>FALSE</code> otherwise
	 */
	public boolean isMoving();
	
	/**
	 * Moves backward
	 */
	public void moveBackward();
	
	/**
	 * Moves backward
	 * 
	 * @param distance - the distance to move (in mm)
	 * @param wait - if <code>TRUE</code>, this method blocks until the motion is finished
	 */
	public void moveBackward(float distance, boolean wait);
	
	/**
	 * Moves forward
	 */
	public void moveForward();
	
	/**
	 * Moves forward
	 * 
	 * @param distance - the distance to move (in mm)
	 * @param wait - if <code>TRUE</code>, this method blocks until the motion is finished
	 */
	public void moveForward(float distance, boolean wait);
	
	/**
	 * Resets the current orientation
	 */
	public void resetOrientation();
	
	/**
	 * Sets the speed
	 * 
	 * @param percentage - the desired percentage of the maximum speed
	 * 
	 * @throws IllegalArgumentException if <b>percentage</b> is outside the interval [0, 100]
	 */
	public void setSpeed(int percentage);
	
	/**
	 * Stops moving/turning
	 */
	public void stop();
	
	/**
	 * Turns the head (= distance sensor) clockwise
	 * 
	 * @param offset - ???
	 */
	public void turnHeadClockWise(int offset);
	
	/**
	 * Turns the head (= distance sensor) counter-clockwise
	 * 
	 * @param offset - ???
	 */
	public void turnHeadCounterClockWise(int offset);
	
	/**
	 * Turns left
	 */
	public void turnLeft();
	
	/**
	 * Turns left
	 * 
	 * @param angle - the angle to turn (in degrees)
	 * @param wait - if <code>TRUE</code>, this method blocks until the motion is finished
	 */
	public void turnLeft(float angle, boolean wait);
	
	/**
	 * Turns right
	 */
	public void turnRight();
	
	/**
	 * Turns right
	 * @param angle - the angle to turn (in degrees)
	 * @param wait - if <code>TRUE</code>, this method blocks until the motion is finished
	 */
	public void turnRight(float angle, boolean wait);
	
}