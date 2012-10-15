package bluebot.util;


import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashSet;
import java.util.List;



/**
 * Basic implementation of the {@link EventDispatcher} interface
 * 
 * @author Ruben Feyen
 */
public abstract class AbstractEventDispatcher<T extends EventListener>
		implements EventDispatcher<T> {
	
	private HashSet<T> listeners = new HashSet<T>();
	
	
	
	public void addListener(final T listener) {
		listeners.add(listener);
	}
	
	/**
	 * Returns a list of all the listeners currently registered
	 * 
	 * @return a {@link List} containing instances of the appropriate {@link EventListener} type
	 */
	protected List<T> getListeners() {
		return new ArrayList<T>(listeners);
	}
	
	public void removeListener(final T listener) {
		listeners.remove(listener);
	}
	
}