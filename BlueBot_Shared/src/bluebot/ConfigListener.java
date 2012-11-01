package bluebot;


import java.util.EventListener;



/**
 * Event listener interface for the (internal) configuration
 * 
 * @author Ruben Feyen
 */
public interface ConfigListener extends EventListener {
	
	/**
	 * This method is called whenever the speed changes
	 * 
	 * @param percentage - the percentage of the maximum speed
	 */
	public void onSpeedChanged(int percentage);
	
}