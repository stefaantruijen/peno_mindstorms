package bluebot.ui;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import bluebot.core.Controller;



/**
 * 
 * @author Ruben Feyen
 */
public class ControllerFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private Controller controller;
	
	
	public ControllerFrame(final Controller controller) {
		super(MainFrame.TITLE);
		this.controller = controller;
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		initComponents();
		pack();
		setLocationRelativeTo(null);
		setResizable(false);
	}
	
	
	
	private final Component createTabCommunication() {
		final CommunicationList list = new CommunicationList();
		controller.addListener(list.createControllerListener());
		return list.createScrollPane();
	}
	
	private final Component createTabControls() {
		final JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout(0, 0));
		
		final JoystickComponent component = new JoystickComponent();
		component.addListener(new JoystickListener() {
			public void onJoystickStop() {
				controller.stop();
			}
			
			public void onJoystickRight(boolean flag) {
				if (flag) {
					controller.turnRight();
				} else {
					controller.stop();
				}
			}
			
			public void onJoystickLeft(boolean flag) {
				if (flag) {
					controller.turnLeft();
				} else {
					controller.stop();
				}
			}
			
			public void onJoystickForward(boolean flag) {
				if (flag) {
					controller.moveForward();
				} else {
					controller.stop();
				}
			}
			
			public void onJoystickBackward(boolean flag) {
				if (flag) {
					controller.moveBackward();
				} else {
					controller.stop();
				}
			}
		});
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
		tabs.addTab("Communication",	createTabCommunication());
		tabs.addTab("Configuration",	new JPanel());
		tabs.addTab("Controls",			createTabControls());
		tabs.addTab("Debug",			createTabDebug());
		tabs.addTab("Errors",			createTabErrors());
		tabs.setSelectedIndex(2);
		return tabs;
	}
	
	private final void initComponents() {
		add(createTabs());
	}
	
}