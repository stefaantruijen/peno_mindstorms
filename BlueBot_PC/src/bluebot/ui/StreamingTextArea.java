package bluebot.ui;


import java.awt.Font;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.logging.Logger;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;



/**
 * 
 * @author Ruben Feyen
 */
public class StreamingTextArea extends JTextArea {
	private static final long serialVersionUID = 1L;
	
	public static final Font DEFAULT_FONT = new Font(Font.MONOSPACED, Font.BOLD, 12);
	public static final int DEFAULT_SIZE_LIMIT = (1 << 16);
	
	
	public StreamingTextArea() {
		setEditable(false);
		setFont(DEFAULT_FONT);
	}
	
	
	
	@Override
	public void append(final String str) {
		super.append(str);
		trim();
		scrollToBottom();
	}
	
	private final void scrollToBottom() {
		setCaretPosition(getDocument().getLength());
	}
	
	private final void trim() {
		final int excess = (getDocument().getLength() - DEFAULT_SIZE_LIMIT);
		if (excess > 0) {
			replaceRange("", 0, excess);
		}
	}
	
	public PrintStream wrap(final OutputStream stream) {
//		return new PrintStream(new WrappedOutputStream(stream), true);
		return new WrappedPrintStream(stream);
	}
	
	
	
	
	
	
	
	
	
	
	private final class WrappedPrintStream extends PrintStream {
		
		public WrappedPrintStream(final OutputStream stream) {
			super(stream, true);
		}
		
		
		
		private final void pipe(final String str) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					StreamingTextArea.this.append(str);
				}
			});
		}
		
		@Override
		public void print(final String str) {
			super.print(str);
			pipe(str);
		}
		
		public void println(final Object obj) {
			super.println(obj);
			pipe("\n");
			if (obj == null) {
				Logger.getLogger("TEST").info("NULL");
			} else {
				Logger.getLogger("TEST").info(obj.getClass().getName());
			}
		}
		
		@Override
		public void println(final String str) {
			super.println(str);
			pipe("\n");
		}
		
	}
	
}