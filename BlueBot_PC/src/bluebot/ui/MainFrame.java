package bluebot.ui;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import simulator.SimulatorController;

import lejos.pc.comm.NXTCommException;

import bluebot.core.Controller;
import bluebot.core.RemoteController;
import bluebot.io.ClientConnection;
import bluebot.io.Connection;



/**
 * 
 * @author Ruben Feyen
 */
public class MainFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	public static final String TITLE = "P&O BlueBot";
	
	
	public MainFrame() {
		super(TITLE);
		initComponents();
		pack();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setLocationRelativeTo(null);
	}
	
	
	
	private final void connectToBrick() {
		final String name = JOptionPane.showInputDialog("What is the name of the NXT brick?");
		if ((name != null) && !name.isEmpty()) {
			connectToBrick(name);
		}
	}
	
	private final void connectToBrick(final String name) {
		final Connection connection;
		try {
			connection = ClientConnection.create(name);
		} catch (final NXTCommException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this,
					e.getMessage(),
					"Connection failed",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		showController(new RemoteController(connection));
	}
	
	private final void connectToSimulator() {
		showController(new SimulatorController());
	}
	
	private static final JButton createButton(final String text) {
		final JButton button = new JButton(text);
		button.setPreferredSize(new Dimension(256, 64));
		return button;
	}
	
	private final void initComponents() {
		setLayout(new BorderLayout(0, 0));
		
		final JButton btnBrick = createButton("Connect to NXT brick");
		btnBrick.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent event) {
				connectToBrick();
			}
		});
		
		final JButton btnSim = createButton("Connect to simulator");
		btnSim.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent event) {
				connectToSimulator();
			}
		});
		
		add(btnBrick, BorderLayout.NORTH);
		add(btnSim, BorderLayout.SOUTH);
	}
	
	private final void showController(final Controller controller) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				final JFrame frame =
						new ControllerFrame(controller);
//						new ControllerFrame2(controller);
				frame.setVisible(true);
			}
		});
	}
	
}