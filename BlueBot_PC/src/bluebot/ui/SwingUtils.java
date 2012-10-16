package bluebot.ui;


import java.awt.Component;
import java.awt.GridBagConstraints;

import javax.swing.BoxLayout;
import javax.swing.JOptionPane;
import javax.swing.JPanel;



/**
 * 
 * @author Ruben Feyen
 */
public final class SwingUtils {
	
	private SwingUtils() {
		// disabled
	}
	
	
	
	public static final GridBagConstraints createGBC() {
		final GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc.fill = GridBagConstraints.NONE;
		gbc.gridx = gbc.gridy = 0;
		
		return gbc;
	}
	
	private static final Component group(final Component[] components, final int axis) {
		final JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, axis));
		
		for (final Component component : components) {
			panel.add(component);
		}
		
		return panel;
	}
	
	public static final Component groupHorizontal(final Component... components) {
		return group(components, BoxLayout.LINE_AXIS);
	}
	
	public static final Component groupVertical(final Component... components) {
		return group(components, BoxLayout.PAGE_AXIS);
	}
	
	public static final void showWarning(final String msg) {
		JOptionPane.showMessageDialog(null,
				msg, "Warning", JOptionPane.WARNING_MESSAGE);
	}
	
}