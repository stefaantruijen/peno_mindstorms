package bluebot.ui;


import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;



/**
 * 
 * @author Ruben Feyen
 */
public class IconTableCellRenderer extends JLabel
		implements TableCellRenderer {
	private static final long serialVersionUID = 1L;
	
	
	
	public Component getTableCellRendererComponent(final JTable table,
			final Object value, final boolean selected, final boolean focus,
			final int row, final int col) {
		if (!(value instanceof Icon)) {
			return new JLabel();
		}
		
		final Icon icon = (Icon)value;
		setIcon(icon);
		setPreferredSize(new Dimension(icon.getIconWidth(), icon.getIconHeight()));
		return this;
	}
	
}