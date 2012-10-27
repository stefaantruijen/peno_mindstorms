package bluebot.core;


import bluebot.Mobile;
import bluebot.util.EventDispatcher;



/**
 * Represents the API of the client-side back-end
 * 
 * @author Ruben Feyen
 */
public interface Controller extends EventDispatcher<ControllerListener>, Mobile {
	
	public void doCalibrate();
	
	public void doPolygon(int corners, float length);
	
	public void doWhiteLineOrientation();
	
}