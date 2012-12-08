package bluebot.ui;


import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;

import bluebot.util.Resources;



/**
 * 
 * @author Ruben Feyen
 */
public class IconTabbedPane extends JTabbedPane {
	private static final long serialVersionUID = 1L;
	
	
	public IconTabbedPane() {
		super(BOTTOM);
	}
	
	
	
	@Override
	public void addTab(final String title, final Component component) {
		final int index = getTabCount();
		super.addTab(title, component);
		setTabComponentAt(index, createTabComponent(title));
	}
	
	protected Component createTabComponent(final String title) {
		final JLabel label = new JLabel(loadIcon(title));
		label.setPreferredSize(new Dimension(32, 32));
		return label;
	}
	
	private static final Icon loadIcon(final String name) {
		return Resources.loadIcon(IconTabbedPane.class, (name + ".png"));
	}
	
}