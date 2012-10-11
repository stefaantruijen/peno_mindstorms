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
		implements ListCellRenderer<CommunicationListModel.Entry> {
	private static final long serialVersionUID = 1L;
	
	
	public CommunicationListCellRenderer() {
		
	}
	
	
	
	public Component getListCellRendererComponent(final JList<? extends Entry> list,
			final Entry value, final int index,
			final boolean selected, final boolean focus) {
		setIcon(value.getIcon());
		setText(value.getMessage());
		return this;
	}
	
}