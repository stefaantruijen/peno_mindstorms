package bluebot.nxt;


import bluebot.util.EventDispatcher;



/**
 * 
 * @author Ruben Feyen
 */
public interface Wheel extends EventDispatcher<WheelListener> {
	
	public float getDelta();
	
	public float getDiameter();
	
	public void reset();
	
}