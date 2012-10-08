package ui;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import lejos.pc.comm.NXTCommException;

import bluebot.core.Controller;
import bluebot.core.RemoteController;
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
			connection = Connection.create(name);
		} catch (final NXTCommException e) {
			JOptionPane.showMessageDialog(this,
					e.getMessage(),
					"Connection failed",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		final Controller controller = new RemoteController(connection);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new ControllerFrame(controller).setVisible(true);
			}
		});
	}
	
	private final void connectToSimulator() {
		// TODO: Add more jokes
		final String[] jokes = {
			"Dit zijn we echt nog nooit tegengekomen!",
			"Onze hond heeft de simulator opgegeten.",
			"Een ontbrekende simulator presteert gemiddeld beter\ndan die van de andere teams",
			"Waar rook is, is vuur.\nWaar een knop is, is helaas geen simulator.",
			"Et tu, simulator?",
			"\"Ik ben het zat om altijd maar te moeten doen alsof.\"\n    - De Simulator"
		};
		
		final String joke = jokes[(int)(Math.random() * jokes.length)];
		JOptionPane.showMessageDialog(this,
				joke,
				"The simulator is unavailable",
				JOptionPane.WARNING_MESSAGE);
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
	
}