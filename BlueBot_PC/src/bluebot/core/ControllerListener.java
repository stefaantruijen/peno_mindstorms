package bluebot.core;


import bluebot.io.ConnectionListener;



/**
 * Event listener interface for {@link Controller} objects
 * 
 * @author Ruben Feyen
 */
public interface ControllerListener extends ConnectionListener {
	
	public void onError(String msg);
	
}