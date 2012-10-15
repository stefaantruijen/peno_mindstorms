package bluebot.ui;


import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import bluebot.ui.CommunicationListModel.Entry;



/**
 * 
 * @author Ruben Feyen
 */
public class CommunicationListCellRenderer extends JLabel
		implements ListCellRenderer {
	private static final long serialVersionUID = 1L;
	
	
	public CommunicationListCellRenderer() {
		
	}
	
	
	
	public Component getListCellRendererComponent(final JList list,
			final Object value, final int index,
			final boolean selected, final boolean focus) {
		final Entry entry = (Entry)value;
		setIcon(entry.getIcon());
		setText(entry.getMessage());
		return this;
	}
	
}