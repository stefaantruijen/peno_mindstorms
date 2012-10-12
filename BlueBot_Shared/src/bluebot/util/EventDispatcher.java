package bluebot.util;


import java.util.EventListener;



/**
 * Represents an object capable of firing events
 * and/or registering event listeners
 * 
 * @author Ruben Feyen
 */
public interface EventDispatcher<T extends EventListener> {
	
	/**
	 * Adds a listener
	 * 
	 * @param listener - the {@link EventListener} to be added
	 */
	public void addListener(T listener);
	
	/**
	 * Removes a listener
	 * 
	 * @param listener - the {@link EventListener} to be removed
	 */
	public void removeListener(T listener);
	
}