package bluebot.util;


import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashSet;
import java.util.List;



/**
 * 
 * @author Ruben Feyen
 */
public abstract class AbstractEventDispatcher<T extends EventListener>
		implements EventDispatcher<T> {
	
	private HashSet<T> listeners = new HashSet<>();
	
	
	
	public void addListener(final T listener) {
		listeners.add(listener);
	}
	
	protected List<T> getListeners() {
		return new ArrayList<>(listeners);
	}
	
	public void removeListener(final T listener) {
		listeners.remove(listener);
	}
	
}