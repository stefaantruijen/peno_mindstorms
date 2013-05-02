package bluebot.ui;


import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import peno.htttp.Callback;

import bluebot.Application;
import bluebot.Operator;
import bluebot.OperatorListener;
import bluebot.core.ControllerListener;
import bluebot.game.Game;
import bluebot.game.GameCallback;
import bluebot.graph.Tile;
import bluebot.sensors.SensorType;
import bluebot.ui.TerminalComponent.SuggestionProvider;
import bluebot.ui.util.RabbitListCellRenderer;
import bluebot.ui.util.RabbitListModel;



/**
 * 
 * @author Ruben Feyen
 */
public class ControllerFrame extends RenderingFrame implements ControllerListener {
	private static final long serialVersionUID = 1L;
	
	private Application application;
	private BarcodeComponent barcode;
	private DefaultVisualizationComponent canvas;
	private GaugeComponent gauge;
	private Operator operator;
	private RabbitListModel rabbit;
	private SensorsComponent sensors;
	private UpdateThread updater;
	
	
	public ControllerFrame(final Application application, final Operator operator) {
		super(application.getGameId());
		this.application = application;
		this.operator = operator;
		
		setPreferredSize(getPreferredSize());
		initComponents();
		pack();
		setFocusTraversalKeysEnabled(false);
		setFocusTraversalPolicyProvider(true);
		
//		controller.addListener(this);
		initOperator();
	}
	
	private final Component createModuleCLI() {
		final TerminalComponent cli = new TerminalComponent();
//		cli.setPreferredSize(new Dimension(1, 1));
		cli.addListener(new TerminalListener() {
			public void onTerminalCommand(final TerminalComponent component,
					final String[] command) {
				final String cmd = command[0];
				if ((cmd == null) || cmd.isEmpty()) {
					// ignored
				} else if (cmd.equals("calibrate")) {
					launch(new Runnable() {
						public void run() {
							try {
								operator.doCalibrate();
							} catch (final Exception e) {
								SwingUtils.showError(e.getMessage());
							}
						}
					});
				} else if (cmd.equals("game")) {
					try {
						doGame(command);
					} catch (final Exception e) {
						cli.echo("Syntax:  game <player-id>");
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
								operator.moveBackward(distance,false);
								break;
							case 'F':
							case 'f':
								operator.moveForward(distance,false);
								break;
							default:
								throw new IllegalArgumentException();
						}
					} catch (final Exception e) {
						cli.echo("Syntax:  move <f|b> [<distance>]");
					}
				} else if (cmd.equals("orientate")) {
					launch(new Runnable() {
						public void run() {
							try {
								operator.doWhiteLine();
							} catch (final Exception e) {
								SwingUtils.showError(e.getMessage());
							}
						}
					});
//				} else if (cmd.equals("polygon")) {
//					try {
//						controller.doPolygon(Integer.parseInt(command[1]),
//								Float.parseFloat(command[2]));
//					} catch (final Exception e) {
//						cli.echo("Syntax:  polygon <corners> <length>");
//					}
				} else if (cmd.equals("reset")) {
					canvas.reset();
					operator.resetOrientation();
				} else if (cmd.equals("set")) {
					try {
						final String setting = command[1].toLowerCase();
						if (setting.isEmpty()) {
							throw new IllegalArgumentException();
						} else if (setting.equals("speed")) {
							operator.setSpeed(Integer.parseInt(command[2]));
						}
					} catch (final Exception e) {
						cli.echo("Syntax:  set <setting> <value>");
					}
				} else if (cmd.equals("speed")) {
					try {
						operator.setSpeed(Integer.parseInt(command[1]));
					} catch (final Exception e) {
						cli.echo("Syntax:  speed <percentage>");
					}
				} else if (cmd.equals("stop")) {
					operator.stop();
//				} else if (cmd.equals("tile")) {
//					controller.doTile();
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
								operator.turnLeft(angle,false);
								break;
							case 'R':
							case 'r':
								operator.turnRight(angle,false);
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
//						"polygon",
						"reset",
						"stop",
						"set",
//						"tile",
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
	
	@SuppressWarnings("unused")
	private final Component createModuleCommunication() {
		final CommunicationTable table = new CommunicationTable();
//		controller.addListener(table.getModel());
		
		final JScrollPane scroll = table.createScrollPane();
		scroll.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		return scroll;
	}

	private final Component createModuleControls() {
		gauge = new GaugeComponent();
		
		final JoystickComponent joystick = new JoystickComponent(operator);
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
		panel.add(gauge, gbc);
		
		gbc.gridx++;
		panel.add(joystick, gbc);
		
		gauge.addListener(new GaugeListener() {
			public void onValueChanged(final int value) {
				joystick.setEnabled(value > 0);
			}
			
			public void onValueRequested(final int value) {
				operator.setSpeed(value);
			}
		});
		/*
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

			@Override
			public void onSensorSpeed(int value) {
				// ignored
				
			}
		});
		*/
		
		joystick.requestFocusInWindow();
		ControllerFrame.this.setFocusTraversalPolicy(
				new SingleFocusTraversalPolicy(joystick));
		
		return panel;
	}
	
	@SuppressWarnings("unused")
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
		sensors = new SensorsComponent();
//		controller.addListener(sensors);
		return sensors;
	}
	
	private final Component createModuleVisualization() {
		canvas = new DefaultVisualizationComponent();
//		canvas = new VisualizationComponent2D();
//		canvas = new VisualizationComponent3D();
//		controller.addListener(canvas);
		canvas.setBackground(Color.WHITE);
		
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
//		tabs.addTab("icon_bluetooth", createModuleCommunication());
		return tabs;
	}
	
	private final Component createTabsRight() {
		final JTabbedPane tabs = new IconTabbedPane();
		tabs.addTab("icon_cli", createModuleCLI());
		tabs.addTab("icon_sensors", createModuleSensors());
//		tabs.addTab("icon_rabbitmq", createModuleRabbitMQ());
		return tabs;
	}
	
	private final void doGame(final String[] args) {
		final String playerId = args[1];
		
		final Game game;
		try {
			game = new Game(operator, application.getGameId(), playerId, new GameCallback() {
				public boolean prepareForGameStart(int playerNumber, int objectNumber) {
					final Tile start = application.getWorld().getStart(playerNumber);
					if (start == null) {
						onError("Unable to determine start location");
						return false;
					}
					
					final float heading;
					switch (start.getStartOrientation()) {
						case NORTH:
							heading = 0F;
							break;
						case EAST:
							heading = 90F;
							break;
						case SOUTH:
							heading = 180F;
							break;
						case WEST:
							heading = 270F;
							break;
						default:
							throw new IllegalArgumentException("Invalid initial orientation");
					}
					operator.setStartLocation(start.getX(), start.getY(), heading);
					
					final String msg = String.format("Place the robot on %s facing %s",
							start, start.getStartOrientation());
					onMessage(msg, "The game has been rolled");
					return true;
				}
			});
			
			game.init(new Callback<Void>() {
				public void onFailure(final Throwable error) {
					System.out.println("FAILURE");
					onError(error.getMessage());
				}
				
				public void onSuccess(final Void result) {
					System.out.println("SUCCESS");
					setTitle(application.getGameId() + " - " + playerId);
					canvas.setGame(game);
					game.start();
				}
			});
		} catch (final Exception e) {
			e.printStackTrace();
			onError(e.getMessage());
		}
	}
	
	@Override
	public Dimension getPreferredSize() {
		final DisplayMode mode = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getDefaultScreenDevice().getDisplayMode();
		return new Dimension((mode.getWidth() * 2 / 3), (mode.getHeight() * 2 / 3));
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
	
	private final void initOperator() {
		operator.addListener(new OperatorMonitor());
		
		operator.setSpeed(100);
	}
	
	private static final void launch(final Runnable task) {
		final Thread thread = new Thread(task);
		thread.setDaemon(true);
		thread.start();
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
	
	protected void startRendering() {
		canvas.setOperator(operator);
		canvas.startRendering();
		
		if (updater == null) {
			updater = new UpdateThread();
			updater.start();
		}
	}
	
	protected void stopRendering() {
		canvas.stopRendering();
		canvas.reset();
		
		if (updater != null) {
			updater.running = false;
			updater = null;
		}
		
		operator.dispose();
	}
	
	
	
	
	
	
	
	
	
	
	private final class OperatorMonitor implements OperatorListener {
		
		public void onSpeedChanged(final int percentage) {
			gauge.setValue(percentage);
		}
		
	}
	
	
	
	
	
	private final class UpdateThread extends Thread {
		
		private boolean running = true;
		
		
		private UpdateThread() {
			super(UpdateThread.class.getSimpleName());
			setDaemon(true);
		}
		
		
		
		@Override
		public void run() {
			long next = System.currentTimeMillis();
			while (running) {
				while (System.currentTimeMillis() < next) {
					try {
						Thread.sleep(10L);
					} catch (final InterruptedException e) {
						System.out.println("GUI UPDATE THREAD INTERRUPTED");
						running = false;
						return;
					}
				}
				
				update(operator.readSensors());
				next += 100L;
			}
		}
		
		private final void update(final int[] values) {
			sensors.onSensorValueLight(values[SensorType.LIGHT.ordinal()]);
			sensors.onSensorValueUltraSonic(values[SensorType.ULTRA_SONIC.ordinal()]);
			
			gauge.setInfrared(values[SensorType.INFRARED.ordinal()]);
		}
		
	}
	
}