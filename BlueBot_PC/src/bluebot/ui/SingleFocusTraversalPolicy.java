package bluebot.ui;


import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;



/**
 * 
 * @author Ruben Feyen
 */
public class SingleFocusTraversalPolicy extends FocusTraversalPolicy {
	
	private Component focus;
	
	
	public SingleFocusTraversalPolicy(final Component focus) {
		this.focus = focus;
	}
	
	
	
	public Component getComponentAfter(final Container container,
			final Component component) {
		return focus;
	}
	
	public Component getComponentBefore(final Container container,
			final Component component) {
		return focus;
	}
	
	public Component getFirstComponent(final Container container) {
		return focus;
	}
	
	public Component getLastComponent(final Container container) {
		return focus;
	}
	
	public Component getDefaultComponent(final Container container) {
		return focus;
	}
	
}