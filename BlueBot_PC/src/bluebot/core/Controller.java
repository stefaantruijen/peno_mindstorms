package bluebot.core;


import bluebot.util.EventDispatcher;



/**
 * Represents the API of the client-side back-end
 * 
 * @author Ruben Feyen
 */
public interface Controller extends EventDispatcher<ControllerListener> {
	
	public void doPolygon(int corners, float length);
	
	public void moveBackward();
	
	public void moveBackward(float distance);
	
	public void moveForward();
	
	public void moveForward(float distance);
	
	public void stop();
	
	public void turnLeft();
	
	public void turnLeft(float degrees);
	
	public void turnRight();
	
	public void turnRight(float degrees);
	
}