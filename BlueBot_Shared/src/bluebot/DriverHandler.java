package bluebot;


import bluebot.io.protocol.Packet;
import bluebot.io.protocol.PacketHandler;
import bluebot.io.protocol.impl.CommandPacket;
import bluebot.io.protocol.impl.ConfigPacket;
import bluebot.io.protocol.impl.MovePacket;
import bluebot.util.BlockingQueue;



/**
 * {@link PacketHandler} implementation for the {@link Driver} class
 * 
 * @author Ruben Feyen
 */
public class DriverHandler implements PacketHandler, Runnable {
	
	private Driver driver;
	private BlockingQueue<Packet> queue;
	private Thread thread;
	
	
	public DriverHandler(final Driver driver) {
		this.driver = driver;
		this.queue = new BlockingQueue<Packet>();
	}
	
	
	
	public void handlePacket(final Packet packet) {
		switch (packet.getOpcode()) {
			case Packet.OP_STOP:
				handlePacketStop();
				break;
			
			default:
				queue.push(packet);
				break;
		}
	}
	
	private final void handlePacket0(final Packet packet) {
		switch (packet.getOpcode()) {
			case Packet.OP_COMMAND:
				handlePacketCommand((CommandPacket)packet);
				break;
			case Packet.OP_CONFIG:
				handlePacketConfig((ConfigPacket)packet);
				break;
			case Packet.OP_MOVE:
				handlePacketMove((MovePacket)packet);
				break;
			case Packet.OP_STOP:
				handlePacketStop();
				break;
		}
	}
	
	private final void handlePacketCommand(final CommandPacket packet) {
		final String command = packet.getCommand();
		if ((command == null) || command.isEmpty()) {
			// ignored
		} else if (command.equals(CommandPacket.CALIBRATE)) {
			driver.calibrate();
		} else if (command.equals(CommandPacket.WHITE_LINE_ORIENTATION)) {
			driver.doWhiteLineOrientation();
		}
	}
	
	private final void handlePacketConfig(final ConfigPacket packet) {
		switch (packet.getId()) {
			case ConfigPacket.ID_SPEED:
				final int speed = packet.getValue().intValue();
				if (speed > 0) {
					switch (speed) {
						case 1:
							driver.setSpeedLow();
							break;
						case 2:
							driver.setSpeedMedium();
							break;
						default:
							driver.setSpeedHigh();
							break;
					}
				} else {
					driver.stop();
				}
				break;
		}
	}
	
	private final void handlePacketMove(final MovePacket packet) {
		switch (packet.getDirection()) {
			case MovePacket.MOVE_BACKWARD:
				if (packet.isQuantified()) {
					driver.moveBackward(packet.getQuantity(), true);
				} else {
					driver.moveBackward();
				}
				break;
				
			case MovePacket.MOVE_FORWARD:
				if (packet.isQuantified()) {
					driver.moveForward(packet.getQuantity(), true);
				} else {
					driver.moveForward();
				}
				break;
				
			case MovePacket.TURN_LEFT:
				if (packet.isQuantified()) {
					driver.turnLeft(packet.getQuantity(), true);
				} else {
					driver.turnLeft();
				}
				break;
				
			case MovePacket.TURN_RIGHT:
				if (packet.isQuantified()) {
					driver.turnRight(packet.getQuantity(), true);
				} else {
					driver.turnRight();
				}
				break;
				
			default:
				// TODO: send error
				System.err.println("Invalid direction:  " + packet.getDirection());
				break;
		}
	}
	
	private final void handlePacketStop() {
		queue.clear();
		driver.stop();
	}
	
	public void run() {
		try {
			for (;;) {
				handlePacket0(queue.pull());
			}
		} catch (final InterruptedException e) {
			// ignored
		}
	}
	
	/**
	 * Starts handling packets
	 */
	public synchronized void start() {
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}
	
	/**
	 * Stops handling packets
	 */
	public synchronized void stop() {
		if (thread != null) {
			thread.interrupt();
			thread = null;
		}
	}
	
}