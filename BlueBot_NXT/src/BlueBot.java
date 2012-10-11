import lejos.nxt.Button;

import bluebot.io.Communicator;
import bluebot.io.Connection;
import bluebot.io.ServerConnection;
import bluebot.io.protocol.Packet;
import bluebot.io.protocol.PacketHandler;
import bluebot.io.protocol.impl.MovePacket;



/**
 * 
 * @author Ruben Feyen
 */
public class BlueBot implements PacketHandler {
	
	private PilotController pc;
	
	
	public BlueBot() {
		this(new PilotController());
	}
	public BlueBot(final PilotController pc) {
		this.pc = pc;
	}
	
	
	
	public void handlePacket(final Packet packet) {
		switch (packet.getOpcode()) {
			case Packet.OP_MOVE:
				handlePacketMove((MovePacket)packet);
				break;
			case Packet.OP_STOP:
				handlePacketStop();
				break;
		}
	}
	
	private final void handlePacketMove(final MovePacket packet) {
		switch (packet.getDirection()) {
			case 2:
				pc.moveBackward(packet.getQuantity());
				break;
			case 4:
				pc.turnLeft(packet.getQuantity());
				break;
			case 6:
				pc.turnRight(packet.getQuantity());
				break;
			case 8:
				pc.moveForward(packet.getQuantity());
				break;
			default:
				// TODO: send error
				System.err.println("Invalid direction:  " + packet.getDirection());
				break;
		}
	}
	
	private final void handlePacketStop() {
		pc.stop();
	}
	
	public static void main(final String... args) {
		try {
			System.out.println("Connecting ...");
			final Connection connection = ServerConnection.create();
			System.out.println("Connected!");
			
			final BlueBot bot = new BlueBot();
			
			final Communicator communicator = new Communicator(connection, bot);
			communicator.start();
			System.out.println("Listening ...");
			Button.waitForAnyPress();
			communicator.stop();
			System.out.println("Done!");
		} catch (final Throwable e) {
			e.printStackTrace();
		}
	}
	
}