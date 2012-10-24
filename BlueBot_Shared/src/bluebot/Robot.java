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
	
	public int readSensorLight();
	
	public void stop();
	
	public void turnLeft();
	
	public void turnLeft(float angle);
	
	public void turnRight();
	
	public void turnRight(float angle);
	
	public void setTravelSpeed(double speed);
	
	public boolean isMoving();
	
}