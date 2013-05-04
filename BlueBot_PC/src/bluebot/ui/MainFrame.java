package bluebot.ui;


import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import lejos.pc.comm.NXTCommException;

import bluebot.Application;
import bluebot.Operator;
import bluebot.OperatorFactory;
import bluebot.game.World;



/**
 * 
 * @author Ruben Feyen
 */
public class MainFrame extends AbstractFrame {
	private static final long serialVersionUID = 1L;
	
	public static final String DEFAULT_BRICK_NAME = "BlueBot";
	public static final String TITLE = "P&O BlueBot";
	
	private Application application;
	
	
	public MainFrame(final Application application) {
		super(application.getGameId());
		this.application = application;
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		initComponents();
		pack();
	}
	
	
	
	private final void connectToBrick() {
		final String name = JOptionPane.showInputDialog(
				"What is the name of the NXT brick?", DEFAULT_BRICK_NAME);
		if ((name != null) && !name.isEmpty()) {
			connectToBrick(name);
		}
	}
	
	private final void connectToBrick(final String name) {
		try {
			showController(OperatorFactory.connectToBrick(name));
		} catch (final IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this,
					e.getMessage(),
					"Connection failed",
					JOptionPane.ERROR_MESSAGE);
			return;
		} catch (final NXTCommException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this,
					e.getMessage(),
					"Connection failed",
					JOptionPane.ERROR_MESSAGE);
			return;
		} catch (final Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this,
					e.getMessage(),
					"Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
	}
	
	private final ControllerFrame connectToSimulator() {
		try {
			return showController(OperatorFactory.connectToSimulator(getWorld()));
		} catch (final Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this,
					e.getMessage(),
					"Error",
					JOptionPane.ERROR_MESSAGE);
			return null;
		}
	}
	
	private static final JButton createButton(final String text) {
		final JButton button = new JButton(text);
		button.setMinimumSize(new Dimension(1, 64));
		return button;
	}
	
	private final Application getApplication() {
		return application;
	}
	
	private final World getWorld() {
		return getApplication().getWorld();
	}
	
	private final void initComponents() {
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
		
		/*
		final JButton btnTestDummy = createButton("Connect to Test Dummy");
		btnTestDummy.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent event) {
				connectToTestDummy();
			}
		});
		*/
		
		final JButton btnDebug = createButton("DEBUG (4x sim)");
		btnDebug.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent event) {
				final String[] playerIds = { "A", "B", "C", "D" };
				for (final String playerId : playerIds) {
					final ControllerFrame gui = connectToSimulator();
					if (gui != null) {
						gui.doGame(playerId);
					}
				}
			}
		});
		
		final GridBagLayout layout = new GridBagLayout();
		layout.columnWeights = new double[] { 1D };
		layout.rowHeights = new int[] { 64, 64, 64 };
		layout.rowWeights = new double[] { 1D, 1D, 1D };
		setLayout(layout);
		
		final GridBagConstraints gbc = SwingUtils.createGBC();
		gbc.fill = GridBagConstraints.BOTH;
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		add(btnBrick, gbc);
		
		gbc.gridy++;
		add(btnSim, gbc);
		
		gbc.gridy++;
		add(btnDebug, gbc);
	}
	
	/**
	 * IMPORTANT:	This method has to be invoked on the EDT
	 * 
	 * @param operator - an {@link Operator}
	 * 
	 * @return the resulting {@link ControllerFrame}
	 */
	private final ControllerFrame showController(final Operator operator) {
		final ControllerFrame frame = new ControllerFrame(application, operator);
		frame.setVisible(true);
		return frame;
	}
	
}