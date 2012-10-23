package bluebot;



/**
 * Represents a robot
 * 
 * @author Ruben Feyen
 */
public interface Robot {
	
	public void moveBackward();
	
	public void moveBackward(float distance);
	
	public void moveForward();
	
	public void moveForward(float distance);
	
	public void stop();
	
	public void turnLeft();
	
	public void turnLeft(float angle);
	
	public void turnRight();
	
	public void turnRight(float angle);
	
}