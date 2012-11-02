package bluebot;



/**
 * Represents a mobile entity
 * 
 * @author Ruben Feyen
 */
public interface Mobile {
	
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