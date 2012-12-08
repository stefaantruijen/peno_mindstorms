package bluebot.ui;


import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;



/**
 * 
 * @author Ruben Feyen
 */
public class TerminalDocument extends DefaultStyledDocument {
	private static final long serialVersionUID = 1L;
	
	
	public TerminalDocument() {
		super(createStyleContext());
	}
	
	
	
	public int append(final String str) {
		return append(str, null);
	}
	
	public int append(final String str, final String style) {
		try {
			insertString(getLength(), str, getStyle(style));
			return getLength();
		} catch (final BadLocationException e) {
			throw new RuntimeException(e);
		}
	}
	
	private static final StyleContext createStyleContext() {
		final StyleContext sc = new StyleContext();
		
		final Style normal = sc.addStyle(StyleContext.DEFAULT_STYLE, null);
		
		@SuppressWarnings("unused")
		final Style echo = sc.addStyle("echo", normal);
//		StyleConstants.setForeground(echo, Color.RED);
		
		final Style prefix = sc.addStyle("prefix", normal);
		StyleConstants.setBold(prefix, true);
		
		return sc;
	}
	
	@Override
	public Style getStyle(final String name) {
		return ((name == null) ? null : super.getStyle(name));
	}
	
}