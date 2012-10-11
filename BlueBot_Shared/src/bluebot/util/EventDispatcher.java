package bluebot.util;


import java.util.EventListener;



/**
 * 
 * @author Ruben Feyen
 */
public interface EventDispatcher<T extends EventListener> {
	
	public void addListener(T listener);
	
	public void removeListener(T listener);
	
}