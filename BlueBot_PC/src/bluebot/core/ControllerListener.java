package bluebot.core;


import java.util.EventListener;



/**
 * Event listener interface for {@link Controller} objects
 * 
 * @author Ruben Feyen
 */
public interface ControllerListener extends EventListener {
	
	public void onError(String msg);
	
	public void onMessage(String msg, String title);
	
}