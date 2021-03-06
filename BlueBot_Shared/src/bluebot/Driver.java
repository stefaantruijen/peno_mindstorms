package bluebot;


import bluebot.sensors.Brightness;
import bluebot.sensors.Calibration;
import bluebot.sensors.CalibrationException;



/**
 * Represents the logic for handling a {@link Robot}
 * 
 * @author Ruben Feyen
 */
public interface Driver extends Mobile {
	
	/**
	 * Terminates the driver and releases any allocated system resources
	 */
	public void dispose();
	
	/**
	 * Returns the angle moved since the start of the movement.
	 * @return
	 */
	public float getAngleIncrement();
	
	public float getArcLimit();

	/**
	 * Returns the calibration data of the driver
	 * 
	 * @return a {@link Calibration} object
	 */
	public Calibration getCalibration();
	
	/**
	 * Corrects the internal orientation (if necessary)
	 */
	public void modifyOrientation();
	
	/**
	 * Plays a little bit of music
	 */
	public void playSound();
	
	/**
	 * Returns the current value of the light sensor
	 * 
	 * @return an <code>int</code> from the interval [0, 100]
	 */
	public int readSensorLight();
	
	/**
	 * Returns the current brightness level detected by the light sensor
	 * 
	 * @return a {@link Brightness} value
	 * 
	 * @throws CalibrationException if the light sensor is not calibrated
	 */
	public Brightness readSensorLightBrightness() throws CalibrationException;
	
	/**
	 * Returns the current value of the light sensor
	 * 
	 * @return an <code>int</code> from the interval [0, 1023]
	 */
	public int readSensorLightValue();
	
	/**
	 * Returns the current value of the ultrasonic sensor
	 * 
	 * @return an <code>int</code> from the interval [0, 255]
	 */
	public int readSensorUltraSonic();
	
	/**
	 * Returns true if the light sensor reads a black brightness
	 * 
	 * @return
	 * @throws CalibrationException
	 * 		If the driver is not yet calibrated
	 */
	public boolean readsBlack() throws CalibrationException;

	/**
	 * Returns true if the light sensor reads a white brightness
	 * 
	 * @return
	 * @throws CalibrationException
 	 * 		If the driver is not yet calibrated
	 */
	public boolean readsWhite() throws CalibrationException;
	
	/**
	 * Returns true if the touch sensor is pressed
	 * @return
	 */
	public boolean isPressed();
	
	/**
	 * Returns true if the light sensor reads a 'gray' brightness
	 * 'gray' here means strictly not white and not black.
	 * 
	 * @return
	 * @throws CalibrationException
 	 * 		If the driver is not yet calibrated
	 */
	public boolean readsGray() throws CalibrationException;
	
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
	 * Sends a notification about finding your item
	 * 
	 * @param teamId - the ID of your team
	 */
	public void sendItemFound(int teamId);
	
	/**
	 * Sends a message
	 * 
	 * @param msg - the message
	 * @param title - a title for the message
	 */
	public void sendMessage(String msg, String title);
	
	/**
	 * Sends a barcode.
	 * 
	 * @param barcode - the barcode
	 */
	public void sendBarcode(int barcode);

	/**
	 * Sends an MQmessage
	 * 
	 * @param msg - the message
	 */
	public void sendMQMessage(String string);

	/**
	 * true if infrared object in range
	 * @return
	 */
	public boolean seeInfrared();
	/**
	 * the direction of the infrered, 0 if no direction
	 * @return
	 */
	public int getInfraredDirection();
	
	/**
	 * 
	 * sets start location
	 */
	public void setStartLocation(int x, int y, float heading);
	
}
