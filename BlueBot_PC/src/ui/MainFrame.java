package ui;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;



/**
 * 
 * @author Ruben Feyen
 */
public class MainFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	public static final String TITLE = "P&O - Team Blauw";
	
	
	public MainFrame() {
		super(TITLE);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		initComponents();
		pack();
		setLocationRelativeTo(null);
		setResizable(false);
	}
	
	
	
	private final Component createTabControls() {
		final JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout(0, 0));
		
		final ControllerComponent component = new ControllerComponent();
		panel.add(component, BorderLayout.CENTER);
		
		// Focus is required to allow the controller buttons component to monitor key(board) events
		component.requestFocus();
		return panel;
	}
	
	private final Component createTabDebug() {
		final StreamingTextArea debug = new StreamingTextArea();
		System.setOut(debug.wrap(System.out));
		
		final JScrollPane scroll = new JScrollPane(debug, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.setPreferredSize(new Dimension(500, 500));
		return scroll;
	}
	
	private final Component createTabErrors() {
		final StreamingTextArea errors = new StreamingTextArea();
		errors.setForeground(Color.RED);
		System.setErr(errors.wrap(System.err));
		
		final JScrollPane scroll = new JScrollPane(errors, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.setPreferredSize(new Dimension(500, 500));
		return scroll;
	}
	
	private final Component createTabs() {
		final JTabbedPane tabs = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
		// The next line is required to allow focus on the controller buttons component
		tabs.setFocusable(false);
		tabs.addTab("Configuration",	new JPanel());
		tabs.addTab("Controls",			createTabControls());
		tabs.addTab("Debug",			createTabDebug());
		tabs.addTab("Errors",			createTabErrors());
		tabs.setSelectedIndex(1);
		return tabs;
	}
	
	private final void initComponents() {
		add(createTabs());
	}
	
	public static void main(final String... args) {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
//					setLookAndFeel();
					new MainFrame().setVisible(true);
				}
			});
		} catch (final Throwable e) {
			e.printStackTrace();
		}
	}
	
	/*
	private static final void setLookAndFeel() {
		final LookAndFeel laf;
		try {
			laf =
//				new AcrylLookAndFeel();
//				new AeroLookAndFeel();
//				new AluminiumLookAndFeel();
				new FastLookAndFeel();
//				new HiFiLookAndFeel();
//				new LunaLookAndFeel();
//				new McWinLookAndFeel();
//				new MintLookAndFeel();
//				new NoireLookAndFeel();
//				new SmartLookAndFeel();
//				new TextureLookAndFeel();
		} catch (final Throwable e) {
			// L&F not available, perhaps the JTattoo library is missing
			e.printStackTrace();
			return;
		}
		
		try {
			UIManager.setLookAndFeel(laf);
		} catch (final UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}
	*/
	
}