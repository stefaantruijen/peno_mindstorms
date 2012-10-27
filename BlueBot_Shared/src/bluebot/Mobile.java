package bluebot;



/**
 * Represents a mobile entity
 * 
 * @author Ruben Feyen
 */
public interface Mobile {
	
	/**
	 * Moves backward
	 */
	public void moveBackward();
	
	/**
	 * Moves backward
	 * 
	 * @param distance - the distance to move (in mm)
	 */
	public void moveBackward(float distance);
	
	/**
	 * Moves forward
	 */
	public void moveForward();
	
	/**
	 * Moves forward
	 * 
	 * @param distance - the distance to move (in mm)
	 */
	public void moveForward(float distance);
	
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
	 */
	public void turnLeft(float angle);
	
	/**
	 * Turns right
	 */
	public void turnRight();
	
	/**
	 * Turns right
	 * 
	 * @param angle - the angle to turn (in degrees)
	 */
	public void turnRight(float angle);
	
}