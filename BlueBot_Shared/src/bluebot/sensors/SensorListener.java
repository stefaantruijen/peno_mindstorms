package bluebot.sensors;


import java.util.EventListener;

import bluebot.Robot;



/**
 * Event listener interface for the sensors
 * 
 * @author Ruben Feyen
 */
public interface SensorListener extends EventListener {
	
	/**
	 * This method is called whenever an infrared sensor value is received
	 * 
	 * @param value - the value of the infrared sensor
	 * 
	 * @see Robot#getInfraredDirection()
	 */
	public void onSensorValueInfrared(int value);
	
	/**
	 * This method is called whenever a light sensor value is received
	 * 
	 * @param value - the value of the light sensor
	 * 
	 * @see {@link Robot#readSensorLight()}
	 */
	public void onSensorValueLight(int value);
	
	/**
	 * This method is called whenever an ultrasonic sensor value is received
	 * 
	 * @param value - the value of the ultrasonic sensor
	 * 
	 * @see {@link Robot#readSensorUltraSonic()}
	 */
	public void onSensorValueUltraSonic(int value);
	
}