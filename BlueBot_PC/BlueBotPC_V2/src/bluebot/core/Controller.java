package bluebot.core;



/**
 * 
 * @author Ruben Feyen
 */
public interface Controller {
	
	public void doPolygon(int corners, int length);
	
	public void moveBackward();
	
	public void moveForward();
	
	public void stop();
	
	public void turnLeft();
	
	public void turnRight();
	
}