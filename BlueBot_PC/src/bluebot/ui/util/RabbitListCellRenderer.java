package bluebot.ui.util;


import java.awt.Component;

import javax.swing.JList;
import javax.swing.JTextPane;
import javax.swing.ListCellRenderer;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyledDocument;



/**
 * 
 * @author Ruben Feyen
 */
public class RabbitListCellRenderer extends JTextPane
		implements ListCellRenderer<RabbitMessage> {
	private static final long serialVersionUID = 1L;
	
	
	public RabbitListCellRenderer() {
		init();
	}
	
	
	
	public Component getListCellRendererComponent(
			final JList<? extends RabbitMessage> list,
			final RabbitMessage value,
			final int index, final boolean selected, final boolean focus) {
		final StyledDocument doc = getStyledDocument();
		try {
			doc.remove(0, doc.getLength());
			doc.insertString(doc.getLength(),
					(value.getKey() + "\n"),
					doc.getStyle("key"));
			doc.insertString(doc.getLength(),
					("    " + value.getMessage()),
					doc.getStyle("msg"));
		} catch (final BadLocationException e) {
			e.printStackTrace();
			setText(e.toString());
		}
		return this;
	}
	
	@SuppressWarnings("unused")
	private final void init() {
		final StyledDocument doc = getStyledDocument();
		{
			final Style style = doc.addStyle("key", null);
		}
		{
			final Style style = doc.addStyle("msg", null);
		}
	}
	
}
