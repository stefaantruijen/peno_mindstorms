package bluebot;


import java.util.EventListener;



/**
 * Event listener interface for the (internal) configuration
 * 
 * @author Ruben Feyen
 */
public interface ConfigListener extends EventListener {
	
	/**
	 * This method is called when the speed is changed to high
	 */
	public void onSpeedHigh();
	
	/**
	 * This method is called when the speed is changed to low
	 */
	public void onSpeedLow();
	
	/**
	 * This method is called when the speed is changed to medium
	 */
	public void onSpeedMedium();
	
}