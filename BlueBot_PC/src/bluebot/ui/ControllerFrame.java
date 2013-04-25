package bluebot.ui;


import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import bluebot.ConfigListener;
import bluebot.core.Controller;
import bluebot.core.ControllerListener;
import bluebot.game.Game;
import bluebot.game.GameCallback;
import bluebot.graph.Tile;
import bluebot.sensors.SensorListener;
import bluebot.ui.TerminalComponent.SuggestionProvider;
import bluebot.ui.util.RabbitListCellRenderer;
import bluebot.ui.util.RabbitListModel;



/**
 * 
 * @author Ruben Feyen
 */
public class ControllerFrame extends JFrame implements ControllerListener {
	private static final long serialVersionUID = 1L;
	
	private BarcodeComponent barcode;
	private DefaultVisualizationComponent canvas;
	private Controller controller;
	private RabbitListModel rabbit;
	
	
	public ControllerFrame(final Controller controller) {
		super(MainFrame.TITLE);
		this.controller = controller;

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		initComponents();
		pack();
		setFocusTraversalKeysEnabled(false);
		setFocusTraversalPolicyProvider(true);
		setLocationRelativeTo(null);
//		setResizable(false);
		
		controller.addListener(this);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(final WindowEvent event) {
				controller.dispose();
				
				canvas.stopRendering();
				canvas.reset();
			}
			
			@Override
			public void windowOpened(final WindowEvent event) {
				canvas.startRendering();
			}
		});
		
		controller.init();
	}
	
	private final Component createModuleCLI() {
		final TerminalComponent cli = new TerminalComponent();
		cli.addListener(new TerminalListener() {
			public void onTerminalCommand(final TerminalComponent component,
					final String[] command) {
				final String cmd = command[0];
				if ((cmd == null) || cmd.isEmpty()) {
					// ignored
				} else if (cmd.equals("calibrate")) {
					controller.doCalibrate();
				} else if (cmd.equals("game")) {
					try {
						doGame(command);
					} catch (final Exception e) {
						cli.echo("Syntax:  game <game-id> <player-id>");
						e.printStackTrace();
					}
				} else if (cmd.equals("move")) {
					try {
						final float distance;
						if (command.length < 3) {
							distance = Tile.SIZE;
						} else {
							distance = Float.parseFloat(command[2]);
						}
						switch (command[1].charAt(0)) {
							case 'B':
							case 'b':
								controller.moveBackward(distance);
								break;
							case 'F':
							case 'f':
								controller.moveForward(distance);
								break;
							default:
								throw new IllegalArgumentException();
						}
					} catch (final Exception e) {
						cli.echo("Syntax:  move <f|b> [<distance>]");
					}
				} else if (cmd.equals("orientate")) {
					controller.doWhiteLineOrientation();
				} else if (cmd.equals("polygon")) {
					try {
						controller.doPolygon(Integer.parseInt(command[1]),
								Float.parseFloat(command[2]));
					} catch (final Exception e) {
						cli.echo("Syntax:  polygon <corners> <length>");
					}
				} else if (cmd.equals("reset")) {
					canvas.reset();
					controller.reset();
				} else if (cmd.equals("set")) {
					try {
						final String setting = command[1].toLowerCase();
						if (setting.isEmpty()) {
							throw new IllegalArgumentException();
						} else if (setting.equals("speed")) {
							controller.setSpeed(Integer.parseInt(command[2]));
						}
					} catch (final Exception e) {
						cli.echo("Syntax:  set <setting> <value>");
					}
				} else if (cmd.equals("speed")) {
					try {
						controller.setSpeed(Integer.parseInt(command[1]));
					} catch (final Exception e) {
						cli.echo("Syntax:  speed <percentage>");
					}
				} else if (cmd.equals("stop")) {
					controller.stop();
				} else if (cmd.equals("tile")) {
					controller.doTile();
				} else if (cmd.equals("turn")) {
					try {
						final float angle;
						if (command.length < 3) {
							angle = 90F;
						} else {
							angle = Float.parseFloat(command[2]);
						}
						switch (command[1].toLowerCase().charAt(0)) {
							case 'L':
							case 'l':
								controller.turnLeft(angle);
								break;
							case 'R':
							case 'r':
								controller.turnRight(angle);
								break;
							default:
								throw new IllegalArgumentException();
						}
					} catch (final Exception e) {
						cli.echo("Syntax:  turn <l|r> [<angle>]");
					}
				} else {
					cli.echo("Invalid command");
				}
			}
		});
		cli.setSuggestions(new SuggestionProvider() {
			public String provideSuggestion(String input) {
				final String[] commands = {
						"calibrate",
						"clear",
						"game",
						"move",
						"orientate",
						"polygon",
						"reset",
						"stop",
						"set",
						"tile",
						"turn"
				};
				for (final String command : commands) {
					if (command.startsWith(input)) {
						return command;
					}
				}
				return null;
			}
		});
		
		final JScrollPane scroll = new JScrollPane(cli,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		return scroll;
	}
	
	private final Component createModuleCommunication() {
		final CommunicationTable table = new CommunicationTable();
		controller.addListener(table.getModel());
		
		final JScrollPane scroll = table.createScrollPane();
		scroll.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		return scroll;
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
		gbc.insets.set(10, 10, 10, 10);
		
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
		controller.addListener(new SensorListener() {
			public void onSensorValueInfrared(final int value) {
				speed.setInfrared(value);
			}
			
			public void onSensorValueLight(final int value) {
				//	ignored
			}
			
			public void onSensorValueUltraSonic(final int value) {
				//	ignored
			}
		});
		
		joystick.requestFocusInWindow();
		ControllerFrame.this.setFocusTraversalPolicy(
				new SingleFocusTraversalPolicy(joystick));
		
		return panel;
	}
	
	private final Component createModuleRabbitMQ() {
		rabbit = new RabbitListModel();
		
		final JList list = new JList(rabbit);
		list.setCellRenderer(new RabbitListCellRenderer());
		
		final JScrollPane scroll = new JScrollPane(list,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		// The next line prevents the scroll pane from extending too far
		scroll.setPreferredSize(new Dimension(1, 1));
		return scroll;
	}

	private final Component createModuleSensors() {
		final SensorsComponent sensors = new SensorsComponent();
		controller.addListener(sensors);
		return sensors;
	}
	
	private final Component createModuleVisualization() {
		canvas = new DefaultVisualizationComponent();
//		canvas = new VisualizationComponent2D();
//		canvas = new VisualizationComponent3D();
//		controller.addListener(canvas);
		
		barcode = new BarcodeComponent();
		barcode.setPreferredSize(new Dimension(-1, 24));
		
		final GridBagLayout layout = new GridBagLayout();
		layout.columnWeights = new double[] { 1D };
		layout.rowWeights = new double[] { 1D, 0D };
		
		final JPanel panel = new JPanel(layout);
		
		final GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets.set(0, 0, 5, 0);
		panel.add(canvas, gbc);
		
		gbc.gridy++;
		gbc.insets.set(5, 5, 5, 5);
		panel.add(barcode, gbc);
		
		return panel;
	}
	
	private final Component createTabsLeft() {
		final JTabbedPane tabs = new IconTabbedPane();
		tabs.addTab("icon_visualization", createModuleVisualization());
		tabs.addTab("icon_bluetooth", createModuleCommunication());
		return tabs;
	}
	
	private final Component createTabsRight() {
		final JTabbedPane tabs = new IconTabbedPane();
		tabs.addTab("icon_cli", createModuleCLI());
		tabs.addTab("icon_sensors", createModuleSensors());
		tabs.addTab("icon_rabbitmq", createModuleRabbitMQ());
		return tabs;
	}
	
	private final void doGame(final String[] args) {
		final Game game;
		try {
			game = controller.doGame(args[1], args[2], new GameCallback() {
				public boolean prepareForGameStart(int playerNumber, int objectNumber) {
					final Tile start = controller.getWorld().getStart(playerNumber);
					if (start == null) {
						onError("Unable to determine start location");
						return false;
					}
					
					final String msg = String.format("Place the robot on %s facing %s",
							start, start.getStartOrientation());
					onMessage(msg, "The game has been rolled");
					return true;
				}
			});
		} catch (final Exception e) {
			e.printStackTrace();
			onError(e.getMessage());
			return;
		}
		
		canvas.setGame(game);
	}
	
	private final void initComponents() {
		final GridBagLayout layout = new GridBagLayout();
		layout.columnWeights = new double[] { 1D, 0D };
		layout.rowWeights = new double[] { 0D, 1D };
		setLayout(layout);
		
		final GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets.set(5, 5, 5, 5);
		
		gbc.gridheight = 2;
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		add(createTabsLeft(), gbc);
		
		gbc.gridheight = 1;
		
		gbc.gridx++;
		add(createModuleControls(), gbc);
		
		gbc.gridy++;
		add(createTabsRight(), gbc);
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