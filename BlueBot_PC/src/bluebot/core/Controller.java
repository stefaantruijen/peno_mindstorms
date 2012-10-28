package bluebot.core;


import bluebot.Mobile;
import bluebot.io.ConnectionListener;
import bluebot.sensors.SensorListener;
import bluebot.util.EventDispatcher;



/**
 * Represents the API of the client-side back-end
 * 
 * @author Ruben Feyen
 */
public interface Controller extends EventDispatcher<ControllerListener>, Mobile {
	
	public void addListener(ConnectionListener listener);
	
	public void addListener(SensorListener listener);
	
	public void doCalibrate();
	
	public void doPolygon(int corners, float length);
	
	public void doWhiteLineOrientation();
	
	public void removeListener(ConnectionListener listener);
	
	public void removeListener(SensorListener listener);
	
	public void setSpeedHigh();
	
	public void setSpeedLow();
	
	public void setSpeedMedium();
	
}