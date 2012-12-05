package bluebot.ui;


import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import bluebot.ConfigListener;
import bluebot.core.Controller;
import bluebot.core.ControllerListener;



/**
 * 
 * @author Ruben Feyen
 */
public class ControllerFrame extends JFrame implements ControllerListener {
	private static final long serialVersionUID = 1L;
	
	private BarcodeComponent barcode;
	private VisualizationComponent canvas;
	private Controller controller;

	public ControllerFrame(final Controller controller) {
		super(MainFrame.TITLE);
		this.controller = controller;

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		initComponents();
		pack();
		setFocusTraversalKeysEnabled(false);
		setFocusTraversalPolicyProvider(true);
		setLocationRelativeTo(null);
		setResizable(false);
		
		controller.addListener(this);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(final WindowEvent event) {
				controller.dispose();
			}
		});
	}

	private final Component createModule(final JComponent content,
			final String title) {
		content.setBorder(BorderFactory.createEtchedBorder());
		return content;
	}

	private final Component createModuleCommands() {
		final Font font = new Font(Font.SANS_SERIF, Font.BOLD, 12);
		
		final JButton buttonCalibrate = new JButton("Calibrate");
		buttonCalibrate.setFocusable(false);
		buttonCalibrate.setFont(font);
		buttonCalibrate.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent event) {
				controller.doCalibrate();
			}
		});
		
		final JButton buttonMaze = new JButton("Maze");
		buttonMaze.setFocusable(false);
		buttonMaze.setFont(font);
		buttonMaze.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent event) {
				controller.doMaze(0);
//				final int pathfinder = new MazeDialog(ControllerFrame.this).display();
//				if (pathfinder > 0) {
//					controller.doMaze(pathfinder);
//				}
			}
		});
		
		final JButton buttonOrientate = new JButton("Orientate");
		buttonOrientate.setFocusable(false);
		buttonOrientate.setFont(font);
		buttonOrientate.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent event) {
				controller.doWhiteLineOrientation();
			}
		});
		
		final JButton buttonPolygon = new JButton("Polygon");
		buttonPolygon.setFocusable(false);
		buttonPolygon.setFont(font);
		buttonPolygon.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent event) {
				final PolygonDialog dialog = new PolygonDialog(ControllerFrame.this);
				if (dialog.display()) {
					controller.doPolygon(dialog.getCorners(),
							dialog.getLength());
				}
			}
		});
		
		final JButton buttonReset = new JButton("Reset");
		buttonReset.setFocusable(false);
		buttonReset.setFont(font);
		buttonReset.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent event) {
				canvas.reset();
				controller.reset();
			}
		});
		
		final JButton buttonTile = new JButton("Tile");
		buttonTile.setFocusable(false);
		buttonTile.setFont(font);
		buttonTile.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent event) {
				controller.doTile();
			}
		});
		
		final JPanel panel = new JPanel();
		
		final GridBagLayout layout = new GridBagLayout();
		layout.columnWeights = new double[] { 1D, 1D, 1D, 1D, 1D, 1D };
		layout.rowHeights = new int[] { 64 };
		layout.rowWeights = new double[] { 1D };
		panel.setLayout(layout);
		
		final GridBagConstraints gbc = SwingUtils.createGBC();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets.set(2, 2, 2, 2);
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		panel.add(buttonCalibrate, gbc);
		
		gbc.gridx++;
		panel.add(buttonMaze, gbc);
		
		gbc.gridx++;
		panel.add(buttonOrientate, gbc);
		
//		gbc.gridx++;
//		panel.add(buttonPolygon, gbc);
		
		gbc.gridx++;
		panel.add(buttonReset, gbc);
		
//		gbc.gridx++;
//		panel.add(buttonTile, gbc);
		
		return createModule(panel, "Commands");
	}

	private final Component createModuleCommunication() {
		final CommunicationTable table = new CommunicationTable();
		controller.addListener(table.getModel());

		final JScrollPane scroll = table.createScrollPane();
		scroll.setMinimumSize(new Dimension(1, 1));
		return createModule(scroll, "Communication");
	}

	private final Component createModuleControls() {
		final GaugeComponent speed = new GaugeComponent(controller);
		
		final JoystickComponent joystick = new JoystickComponent(controller);
		joystick.setEnabled(false);
		
		final GridBagLayout layout = new GridBagLayout();
		layout.columnWeights = new double[] { 0D, 0D };
		layout.rowWeights = new double[] { 0D };
		
		final JPanel panel = new JPanel(layout);
		
		final GridBagConstraints gbc = SwingUtils.createGBC();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets.set(5, 5, 5, 5);
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		panel.add(speed, gbc);
		
		gbc.gridx++;
		panel.add(joystick, gbc);
		
		speed.addListener(new GaugeListener() {
			public void onValueChanged(final int value) {
				joystick.setEnabled(value > 0);
			}
		});
		controller.addListener(new ConfigListener() {
			public void onSpeedChanged(final int percentage) {
				speed.setValue(percentage);
			}
		});
		
		joystick.requestFocusInWindow();
		ControllerFrame.this.setFocusTraversalPolicy(
				new SingleFocusTraversalPolicy(joystick));
		
		return createModule(panel, "Controls");
	}

	private final Component createModuleSensors() {
		final SensorsComponent sensors = new SensorsComponent();
		controller.addListener(sensors);
		return createModule(sensors, "Sensors");
	}
	
	private final Component createModuleVisualization() {
		canvas = new VisualizationComponent2D();
//		canvas = new VisualizationComponent3D();
		controller.addListener(canvas);
		
		barcode = new BarcodeComponent();
		barcode.setPreferredSize(new Dimension(-1, 24));
		
		final GridBagLayout layout = new GridBagLayout();
		layout.columnWeights = new double[] { 1D };
		layout.rowWeights = new double[] { 1D, 0D };
		
		final GridBagConstraints gbc = new GridBagConstraints();
		
		final JPanel panel = new JPanel(layout);
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets.set(0, 0, 5, 0);
		panel.add(canvas, gbc);
		
		gbc.gridy++;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets.set(5, 5, 5, 5);
		panel.add(barcode, gbc);
		
		return createModule(panel, "Visualization");
	}
	
	private final Component createTabs() {
		final JTabbedPane tabs = new JTabbedPane(JTabbedPane.TOP);
		tabs.addTab("Communication", createModuleCommunication());
		tabs.addTab("Visualization", createModuleVisualization());
		tabs.setSelectedIndex(1);
		return tabs;
	}

	private final void initComponents() {
		final GridBagLayout layout = new GridBagLayout();
		layout.columnWeights = new double[] { 0D, 0D };
		layout.rowWeights = new double[] { 0D, 0D, 1D };
		setLayout(layout);
		
		final GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets.set(5, 5, 5, 5);
		
		gbc.gridheight = 3;
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		add(createTabs(), gbc);
		
		gbc.gridheight = 1;
		
		gbc.gridx++;
		add(createModuleControls(), gbc);
		
		gbc.gridy++;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		add(createModuleCommands(), gbc);
		
		gbc.gridy++;
		gbc.fill = GridBagConstraints.BOTH;
		add(createModuleSensors(), gbc);
	}
	
	public void onError(final String msg) {
		JOptionPane.showMessageDialog(this, msg, "Error",
				JOptionPane.ERROR_MESSAGE);
	}
	
	public void onMessage(final String msg, final String title) {
		if (title.toLowerCase().equals("barcode")) {
			final int split = msg.indexOf(':');
			barcode.setBarcode(Integer.parseInt(msg.substring(0, split)));
			barcode.setText(msg.substring(split + 1));
		} else {
			JOptionPane.showMessageDialog(this, msg, title,
					JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
}