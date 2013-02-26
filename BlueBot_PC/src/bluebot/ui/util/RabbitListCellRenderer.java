package bluebot.ui.util;


import java.awt.Color;
import java.awt.Component;

import javax.swing.JList;
import javax.swing.JTextPane;
import javax.swing.ListCellRenderer;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import bluebot.io.RabbitMessage;



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
			doc.insertString(0,
					(value.getKey() + "\n"),
					doc.getStyle("rabbit_key"));
			final int offset = doc.getLength();
			doc.insertString(offset,
					value.getMessage(),
					doc.getStyle("rabbit_msg"));
			doc.setParagraphAttributes(offset, (doc.getLength() - offset),
					doc.getStyle("rabbit_msg"), true);
		} catch (final BadLocationException e) {
			e.printStackTrace();
			setText(e.toString());
		}
		return this;
	}
	
	private final void init() {
		final StyledDocument doc = getStyledDocument();
		final Style normal = doc.getStyle(StyleContext.DEFAULT_STYLE);
		{
			final Style style = doc.addStyle("rabbit_key", normal);
			StyleConstants.setBold(style, true);
			StyleConstants.setFontSize(style, 16);
			StyleConstants.setForeground(style, Color.GRAY);
			StyleConstants.setItalic(style, true);
			StyleConstants.setLeftIndent(style, 0);
		}
		{
			final Style style = doc.addStyle("rabbit_msg", normal);
			StyleConstants.setFontSize(style, 14);
			StyleConstants.setLeftIndent(style, 16);
		}
	}
	
}
