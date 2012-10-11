package bluebot.ui;


import java.awt.GridBagConstraints;



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
	
}