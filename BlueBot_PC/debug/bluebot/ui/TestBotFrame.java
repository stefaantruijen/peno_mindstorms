package bluebot.ui;


import static bluebot.io.protocol.PacketFactory.getPacketFactory;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import lejos.pc.comm.NXTCommException;

import bluebot.io.ClientConnector;
import bluebot.io.Connection;
import bluebot.io.protocol.Packet;
import bluebot.io.protocol.impl.MotionPacket;



/**
 * 
 * @author Ruben Feyen
 */
public class TestBotFrame extends JFrame implements Runnable {
	private static final long serialVersionUID = 1L;
	
	private static final Font DEFAULT_FONT =
			new Font(Font.SANS_SERIF, Font.BOLD, 16);
	
	private Connection connection;
	private int index;
	private JLabel[] labels;
	
	
	public TestBotFrame(final Connection connection) {
		super("TestBot");
		this.connection = connection;
		
		initComponents();
		pack();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		
		addKeyListener(new ThrottledKeyAdapter(new KeyAdapter() {
			@Override
			public void keyPressed(final KeyEvent event) {
				switch (event.getKeyCode()) {
					case KeyEvent.VK_DOWN:
//						sendPacket(getPacketFactory().createMoveBackward());
						sendPacket(getPacketFactory().createMoveBackward(100F));
						break;
					case KeyEvent.VK_LEFT:
//						sendPacket(getPacketFactory().createTurnLeft());
						sendPacket(getPacketFactory().createTurnLeft(90F));
						break;
					case KeyEvent.VK_RIGHT:
//						sendPacket(getPacketFactory().createTurnRight());
						sendPacket(getPacketFactory().createTurnRight(90F));
						break;
					case KeyEvent.VK_UP:
//						sendPacket(getPacketFactory().createMoveForward());
						sendPacket(getPacketFactory().createMoveForward(100F));
						break;
				}
			}
			
			@Override
			public void keyReleased(final KeyEvent event) {
//				sendPacket(getPacketFactory().createStop());
			}
		}));
	}
	
	
	
	private static final Connection connect() throws NXTCommException {
		return new ClientConnector().connectTo("BlueBot");
	}
	
	private static final JLabel createLabel() {
		final JLabel label = new JLabel();
		label.setBorder(BorderFactory.createEtchedBorder());
		label.setFont(DEFAULT_FONT);
		label.setHorizontalAlignment(JLabel.RIGHT);
		label.setPreferredSize(new Dimension(128, 32));
		return label;
	}
	
	private final void initComponents() {
		final GridBagLayout layout = new GridBagLayout();
		setLayout(layout);
		
		labels = new JLabel[6];
		for (int i = (labels.length - 1); i >= 0; i--) {
			labels[i] = createLabel();
		}
		
		final GridBagConstraints gbc = SwingUtils.createGBC();
		gbc.insets.set(5, 5, 5, 5);
		
		int index = 0;
		for (int y = 0; y < 2; y++) {
			gbc.gridx = 0;
			for (int x = 0; x < 3; x++) {
				add(labels[index++], gbc);
				gbc.gridx++;
			}
			gbc.gridy++;
		}
	}
	
	public static void main(final String... args) {
		try {
			final Connection connection;
			try {
				connection = connect();
			} catch (final NXTCommException e) {
				e.printStackTrace();
				showError(null, "Failed to connect to the NXT brick");
				return;
			}
			
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					RepeatingKeyReleasedEventsFix.install();
					
					final TestBotFrame frame = new TestBotFrame(connection);
					
					final Thread thread = new Thread(frame);
					thread.setDaemon(true);
					thread.start();
					
					frame.addWindowListener(new WindowAdapter() {
						@Override
						public void windowClosing(final WindowEvent event) {
							thread.interrupt();
						}
					});
					frame.setVisible(true);
				}
			});
		} catch (final Throwable e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		for (Packet packet; !Thread.interrupted();) {
			try {
				packet = connection.readPacket();
				if (packet instanceof MotionPacket) {
					final MotionPacket motion = (MotionPacket)packet;
					setText(0, motion.getX());
					setText(1, motion.getY());
					setText(2, motion.getHeading());
					if ((index += 3) >= labels.length) {
						index = 0;
					}
				}
			} catch (final IOException e) {
				System.out.println("Disconnected!");
				return;
			} catch (final NullPointerException e) {
				// connection == null
				return;
			}
		}
	}
	
	private final void sendPacket(final Packet packet) {
		if (connection != null) {
			try {
				connection.writePacket(packet);
			} catch (final IOException e) {
				showError(this, "Failed to send packet");
			}
		}
	}
	
	private final void setText(final int offset, final float value) {
		labels[index + offset].setText(Float.toString(value));
	}
	
	private static final void showError(final Component parent,
			final String msg) {
		JOptionPane.showMessageDialog(parent,
				msg,
				"Error",
				JOptionPane.ERROR_MESSAGE);
	}
	
}