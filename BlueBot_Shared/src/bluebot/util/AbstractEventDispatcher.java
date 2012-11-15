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
//		final ArrayList<T> list = new ArrayList<T>(listeners.size());
//		
//		
//		final T[] listeners = (T[])new Object[this.listeners.size()];
//		this.listeners.toArray(listeners);
//		return Arrays.asList(listeners);
		return new ArrayList<T>(listeners);
		
//		for (final T listener : listeners) {
//			list.add(listener);
//		}
//		return list;
	}
	
	public void removeListener(final T listener) {
		listeners.remove(listener);
	}
	
	public void removeListeners() {
		listeners.clear();
	}
	
}