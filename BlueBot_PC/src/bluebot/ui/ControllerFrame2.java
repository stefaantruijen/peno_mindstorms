package bluebot.ui;


import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import bluebot.core.Controller;



/**
 * 
 * @author Ruben Feyen
 */
public class ControllerFrame2 extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private Controller controller;
	
	
	public ControllerFrame2(final Controller controller) {
		super(MainFrame.TITLE);
		this.controller = controller;
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setJMenuBar(new ControllerMenuBar(controller));
		initComponents();
		pack();
		setLocationRelativeTo(null);
		setResizable(false);
	}
	
	
	
	private final Component createModule(final Component content, final String title) {
		final JPanel panel = new JPanel(new BorderLayout(0, 0));
		panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), title));
		panel.add(content, BorderLayout.CENTER);
		return panel;
	}
	
	private final Component createModuleCommunication(final int width, final int height) {
//		final CommunicationList list = new CommunicationList();
//		controller.addListener(list.createControllerListener());
		
		final CommunicationTable table = new CommunicationTable();
		controller.addListener(table.createControllerListener());
		
		final JScrollPane scroller = table.createScrollPane();
		scroller.setPreferredSize(new Dimension(width, height));
		return createModule(scroller, "Communication");
	}
	
	private final Component createModuleJoystick(final int width, final int height) {
		final JoystickComponent joystick = new JoystickComponent();
		joystick.setPreferredSize(new Dimension(width, height));
		joystick.addListener(new JoystickListener() {
			public void onJoystickStop() {
				controller.stop();
			}
			
			public void onJoystickRight(final boolean flag) {
				if (flag) {
					controller.turnRight();
				} else {
					controller.stop();
				}
			}
			
			public void onJoystickLeft(final boolean flag) {
				if (flag) {
					controller.turnLeft();
				} else {
					controller.stop();
				}
			}
			
			public void onJoystickForward(final boolean flag) {
				if (flag) {
					controller.moveForward();
				} else {
					controller.stop();
				}
			}
			
			public void onJoystickBackward(final boolean flag) {
				if (flag) {
					controller.moveBackward();
				} else {
					controller.stop();
				}
			}
		});
		joystick.requestFocusInWindow();
		return createModule(joystick, "Controls");
	}
	
	private final Component group(final int axis, final Component... components) {
		final JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, axis));
		
		for (final Component component : components) {
			panel.add(component);
		}
		
		return panel;
	}
	
	private final void initComponents() {
		setLayout(new BorderLayout(0, 0));
		
		final JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(250, 250));
		
		final Component moduleComm = createModuleCommunication(500, 250);
		final Component moduleJoystick = createModuleJoystick(250, 250);
		
		add(group(BoxLayout.PAGE_AXIS,
				group(BoxLayout.LINE_AXIS,
						panel,
						moduleJoystick),
				moduleComm));
	}
	
}