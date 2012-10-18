package bluebot.core;

import bluebot.io.protocol.Packet;
import bluebot.io.protocol.PacketHandler;
import bluebot.io.protocol.impl.MovePacket;
import bluebot.util.PacketQueue;



/**
 * 
 * @author Ruben Feyen
 */
public class BlueBot implements PacketHandler, Runnable {
	
	private PilotController pc;
	private PacketQueue queue;
	private Thread thread;
	
	
	public BlueBot() {
		this(new PilotController());
	}
	public BlueBot(final PilotController pc) {
		this.pc = pc;
		this.queue = new PacketQueue();
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
			case MovePacket.MOVE_BACKWARD:
				if (packet.isQuantified()) {
					pc.moveBackward(packet.getQuantity());
				} else {
					pc.backward();
				}
				break;
				
			case MovePacket.MOVE_FORWARD:
				if (packet.isQuantified()) {
					pc.moveForward(packet.getQuantity());
				} else {
					pc.forward();
				}
				break;
				
			case MovePacket.TURN_LEFT:
				if (packet.isQuantified()) {
					pc.turnLeft(packet.getQuantity());
				} else {
					pc.left();
				}
				break;
				
			case MovePacket.TURN_RIGHT:
				if (packet.isQuantified()) {
					pc.turnRight(packet.getQuantity());
				} else {
					pc.right();
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
		pc.stop();
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
	
	public synchronized void start() {
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}
	
	public synchronized void stop() {
		if (thread != null) {
			thread.interrupt();
			thread = null;
		}
	}
	
}