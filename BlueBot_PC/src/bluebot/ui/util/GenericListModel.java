package bluebot.ui.util;


import java.util.ArrayList;

import javax.swing.AbstractListModel;



/**
 * 
 * @author Ruben Feyen
 */
public class GenericListModel<T> extends AbstractListModel {
	private static final long serialVersionUID = 1L;
	
	private ArrayList<T> elements = new ArrayList<T>();
	
	
	
	public void addElement(final T element) {
		final int index = elements.size();
		if (elements.add(element)) {
			fireIntervalAdded(this, index, index);
		}
	}
	
	public T getElementAt(final int index) {
		return elements.get(index);
	}
	
	public int getSize() {
		return elements.size();
	}
	
	public void removeElement(final int index) {
		if (elements.remove(index) != null) {
			fireIntervalRemoved(this, index, index);
		}
	}
	
	public void removeElement(final T element) {
		final int index = elements.indexOf(element);
		if (index != -1) {
			removeElement(index);
		}
	}
	
}
