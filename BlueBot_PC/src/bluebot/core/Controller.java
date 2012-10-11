package bluebot.core;


import bluebot.util.EventDispatcher;



/**
 * 
 * @author Ruben Feyen
 */
public interface Controller extends EventDispatcher<ControllerListener> {
	
	public void doPolygon(int corners, float length);
	
	public void moveBackward();
	
	public void moveForward();
	
	public void stop();
	
	public void turnLeft();
	
	public void turnRight();
	
}