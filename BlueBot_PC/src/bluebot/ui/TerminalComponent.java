package bluebot.ui;


import java.awt.Dimension;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.Document;
import javax.swing.text.StyleContext;



/**
 * 
 * @author Ruben Feyen
 */
public class TerminalComponent extends JTextPane {
	private static final long serialVersionUID = 1L;
	
	private static final String[] COMMANDS;
	public static final char DEFAULT_DELIMITER = '#';
	static {
		COMMANDS = new String[] {
				"calibrate",
				"clear",
				"maze",
				"move",
				"orientate",
				"polygon",
				"reset",
				"stop",
				"set",
				"tile",
				"turn"
		};
	}
	
	private char delimiter;
	private int offset;
	
	
	public TerminalComponent() {
		this(DEFAULT_DELIMITER);
	}
	public TerminalComponent(final char delimiter) {
		super(new TerminalDocument());
		this.delimiter = delimiter;
		
		prefix();
		getDocument().addDocumentListener(new AutoCompleteListener());
	}
	
	
	
	public void addListener(final TerminalListener listener) {
		listenerList.add(TerminalListener.class, listener);
	}
	
	private final void clear() {
		try {
			getDocument().remove(0, getDocument().getLength());
		} catch (final BadLocationException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void echo(final String line) {
		offset = getStyledDocument().append((line + '\n'), "echo");
	}
	
	private final void fireCommand(final String[] command) {
//		final String cmd = command[0];
//		if ((cmd == null) || cmd.isEmpty()) {
//			// ignored
//		} else 	if (cmd.equals("clear")) {
//			SwingUtilities.invokeLater(new Runnable() {
//				public void run() {
//					try {
//						
//					} catch (final BadLocationException e) {
//						throw new RuntimeException(e);
//					} finally {
//						prefix();
//					}
//				}
//			});
//		} else {
			for (final TerminalListener listener : getListeners()) {
				listener.onTerminalCommand(this, command);
			}
//		}
	}
	
	private final TerminalListener[] getListeners() {
		return listenerList.getListeners(TerminalListener.class);
	}
	
	@Override
	public TerminalDocument getStyledDocument() {
		return (TerminalDocument)super.getStyledDocument();
	}
	
	public String getInput() {
		if (getDocument().getLength() <= offset) {
			return "";
		}
		try {
			return getDocument().getText(offset, (getDocument().getLength() - offset));
		} catch (final BadLocationException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void main(final String... args) {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					final TerminalComponent terminal = new TerminalComponent();
//					terminal.setForeground(Color.RED);
					terminal.setPreferredSize(new Dimension(512, 512));
					
					final JFrame frame = new JFrame("TEST");
					frame.add(terminal);
					frame.pack();
					frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					frame.setLocationRelativeTo(null);
					frame.setResizable(false);
					frame.setVisible(true);
				}
			});
		} catch (final Throwable e) {
			e.printStackTrace();
		}
	}
	
	private final void prefix() {
		getStyledDocument().append(("BlueBot " + delimiter), "prefix");
		offset = getStyledDocument().append(" ", StyleContext.DEFAULT_STYLE);
	}
	
	@Override
	protected void processKeyEvent(final KeyEvent event) {
		if (event.getID() != KeyEvent.KEY_PRESSED) {
			super.processKeyEvent(event);
			return;
		}
		
		final String input;
		switch (event.getKeyCode()) {
			case KeyEvent.VK_BACK_SPACE:
			case KeyEvent.VK_DELETE:
				processKeyEventRemove(event);
				break;
				
			case KeyEvent.VK_ENTER:
				// Process command
				input = getInput();
				if (!input.isEmpty()) {
					final Document doc = getDocument();
					try {
						doc.insertString(doc.getLength(), "\n", null);
					} catch (final BadLocationException e) {
						throw new RuntimeException(e);
					}
					setCaretPosition(doc.getLength());
					final String[] command = input.trim().split("\\s+");
					final String cmd = command[0] = command[0].toLowerCase();
					if ((cmd == null) || cmd.isEmpty()) {
						// ignored
					} else if (cmd.equals("clear")) {
						clear();
					} else {
						fireCommand(command);
					}
				}
				prefix();
				break;
				
			case KeyEvent.VK_TAB:
				setCaretPosition(getDocument().getLength());
				break;
				
			default:
				super.processKeyEvent(event);
				break;
		}
	}
	
	private final void processKeyEventRemove(final KeyEvent event) {
		final Caret caret = getCaret();
		
		final int dot = caret.getDot();
		final int mark = caret.getMark();
		if (dot != mark) {
			// A selection is present
			if (Math.min(dot, mark) >= offset) {
				super.processKeyEvent(event);
			}
			return;
		}
		
		final boolean remove;
		switch (event.getKeyCode()) {
			case KeyEvent.VK_BACK_SPACE:
				remove = (dot > offset);
				break;
				
			case KeyEvent.VK_DELETE:
				remove = (dot >= offset);
				break;
				
			default:
				remove = false;
				break;
		}
		if (remove) {
			super.processKeyEvent(event);
		}
	}
	
	public void removeListener(final TerminalListener listener) {
		listenerList.remove(TerminalListener.class, listener);
	}
	
	private static final String suggest(final String input) {
		for (final String possible : COMMANDS) {
			if (possible.startsWith(input)) {
				return possible;
			}
		}
		return null;
	}
	
	
	
	
	
	
	
	
	
	
	private final class AutoCompleteListener implements DocumentListener {
		
		public void changedUpdate(final DocumentEvent event) {
			// ignored
		}
		
		public void insertUpdate(final DocumentEvent event) {
			final String input = getInput();
			if (!input.isEmpty() && !input.contains(" ")) {
				final String suggested = suggest(input);
				if ((suggested != null) && (suggested.length() > input.length())) {
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							final Document doc = event.getDocument();
							try {
								doc.insertString(doc.getLength(),
										suggested.substring(input.length()), null);
							} catch (final BadLocationException e) {
								throw new RuntimeException(e);
							}
							final int end = doc.getLength();
							select((end - suggested.length() + input.length()),
									end);
						}
					});
				}
			}
		}
		
		public void removeUpdate(final DocumentEvent event) {
			// ignored
		}
		
	}
	
}