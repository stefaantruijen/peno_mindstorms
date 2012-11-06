package bluebot;

import bluebot.graph.Tile;
import bluebot.sensors.Calibration;



/**
 * Represents the logic for handling a {@link Robot}
 * 
 * @author Ruben Feyen
 */
public interface Driver extends Mobile {
	
	// TODO: Remove and use getHeading() instead
	public float getAngleIncrement();
	
	/**
	 * Returns the calibration data of the driver
	 * 
	 * @return a {@link Calibration} object
	 */
	public Calibration getCalibration();
	
	/**
	 * Returns the current value of the light sensor
	 * 
	 * @return an <code>int</code> from the interval [0, 100]
	 */
	public int readSensorLight();
	
	/**
	 * Returns the current value of the ultrasonic sensor
	 * 
	 * @return an <code>int</code> from the interval [0, 255]
	 */
	public int readSensorUltraSonic();
	
	/**
	 * Sends a debug message
	 * 
	 * @param msg - the message to be sent
	 */
	public void sendDebug(String msg);
	
	/**
	 * Sends an error message
	 * 
	 * @param msg - the message to be sent
	 */
	public void sendError(String msg);
	
	/**
	 * Sends a message
	 * 
	 * @param msg - the message
	 * @param title - a title for the message
	 */
	public void sendMessage(String msg, String title);
	
	/**
	 * Sends a tile
	 * 
	 * @param tile - the (updated) {@link Tile}
	 */
	public void sendTile(Tile tile);
	
}