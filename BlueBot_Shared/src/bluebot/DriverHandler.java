package bluebot;


import bluebot.actions.ActionQueue;
import bluebot.actions.impl.CalibrationAction;
import bluebot.actions.impl.MovementAction;
import bluebot.actions.impl.PickUpAction;
import bluebot.actions.impl.PolygonAction;
import bluebot.actions.impl.ReadBarcodeAction;
import bluebot.actions.impl.SeesawAction;
import bluebot.actions.impl.WhiteLineAction;
import bluebot.io.protocol.Packet;
import bluebot.io.protocol.PacketHandler;
import bluebot.io.protocol.impl.CommandPacket;
import bluebot.io.protocol.impl.ConfigPacket;
import bluebot.io.protocol.impl.MovePacket;
import bluebot.io.protocol.impl.PolygonPacket;
import bluebot.io.protocol.impl.ReadBarcodePacket;
import bluebot.io.protocol.impl.SeesawPacket;
import bluebot.io.protocol.impl.TurnHeadClockwisePacket;
import bluebot.io.protocol.impl.TurnHeadCounterClockwisePacket;



/**
 * {@link PacketHandler} implementation for the {@link Driver} class
 * 
 * @author Ruben Feyen
 */
public class DriverHandler implements PacketHandler {
	
	private Driver driver;
	private ActionQueue queue;
	
	
	public DriverHandler(final Driver driver) {
		this.driver = driver;
		this.queue = new ActionQueue(driver);
	}
	
	
	
	public void handlePacket(final Packet packet) {
		switch (packet.getOpcode()) {
			case Packet.OP_COMMAND:
				handlePacketCommand((CommandPacket)packet);
				break;
			case Packet.OP_CONFIG:
				handlePacketConfig((ConfigPacket)packet);
				break;
			case Packet.OP_DISCONNECT:
				handlePacketDisconnect();
				break;
			case Packet.OP_SEESAW:
				handlePacketSeeSaw((SeesawPacket)packet);
				break;
			case Packet.OP_READBARCODE:
				handlePacketReadBarcode((ReadBarcodePacket)packet);
				break;
			case Packet.OP_MOVE:
				handlePacketMove((MovePacket)packet);
				break;
			case Packet.OP_POLYGON:
				handlePacketPolygon((PolygonPacket)packet);
				break;
			case Packet.OP_RESET:
				handlePacketReset();
				break;
			case Packet.OP_STOP:
				handlePacketStop();
				break;
			case Packet.OP_PICKUP:
				handlePacketPickUp();
				break;
			case Packet.OP_TURNHEADCLOCKWISE:
				handlePacketTurnHeadClockwise((TurnHeadClockwisePacket)packet);
				break;
			case Packet.OP_TURNHEADCOUNTERCLOCKWISE:
				handlePacketTurnHeadCounterClockwise((TurnHeadCounterClockwisePacket)packet);
				break;
			case Packet.OP_MODIFYORIENTATION:
				handlePacketModifyOrientation();
				break;
		}
	}
	
	private void handlePacketModifyOrientation() {
		driver.modifyOrientation();
	}

	private void handlePacketTurnHeadClockwise(final TurnHeadClockwisePacket packet) {
		driver.turnHeadClockWise(packet.getOffset());
	}

	private void handlePacketTurnHeadCounterClockwise(final TurnHeadCounterClockwisePacket packet) {
		driver.turnHeadCounterClockWise(packet.getOffset());
	}

	private void handlePacketPickUp() {
		queue.queue(new PickUpAction());
	}



	private final void handlePacketCommand(final CommandPacket packet) {
		final String command = packet.getCommand();
		if ((command == null) || command.isEmpty()) {
			// ignored
		} else if (command.equals(CommandPacket.CALIBRATE)) {
			queue.queue(new CalibrationAction());
		} else if (command.equals(CommandPacket.WHITE_LINE_ORIENTATION)) {
			queue.queue(new WhiteLineAction());
		}
	}
	
	private final void handlePacketConfig(final ConfigPacket packet) {
		switch (packet.getId()) {
			case ConfigPacket.ID_SPEED:
				final int percentage = packet.getValue().intValue();
				driver.setSpeed(percentage);
				if (percentage <= 0) {
					handlePacketStop();
				}
				break;
		}
	}
	
	private final void handlePacketDisconnect() {
		stop();
		driver.dispose();
	}
	
	private final void handlePacketSeeSaw(final SeesawPacket packet) {
		queue.queue(new SeesawAction());
	}
	

	private void handlePacketReadBarcode(final ReadBarcodePacket packet) {
		queue.queue(new ReadBarcodeAction());
	}

	
	private final void handlePacketMove(final MovePacket packet) {
		if (packet.isQuantified()) {
			queue.queue(new MovementAction(packet.getDirection(), packet.getQuantity()));
		} else {
			switch (packet.getDirection()) {
				case MovePacket.MOVE_BACKWARD:
					driver.moveBackward();
					break;
					
				case MovePacket.MOVE_FORWARD:
					driver.moveForward();
					break;
					
				case MovePacket.TURN_LEFT:
					driver.turnLeft();
					break;
					
				case MovePacket.TURN_RIGHT:
					driver.turnRight();
					break;
					
				default:
					driver.sendError("Invalid direction:  " + packet.getDirection());
					break;
			}
		}
	}
	
	private final void handlePacketPolygon(final PolygonPacket packet) {
		queue.queue(new PolygonAction(packet.getCorners(), packet.getLength()));
	}
	
	private final void handlePacketReset() {
		stop();
		driver.resetOrientation();
	}
	
	private final void handlePacketStop() {
		queue.abort();
		driver.stop();
	}
	
	public void start() {
		queue.start();
	}
	
	public void stop() {
		queue.abort();
		queue.stop();
	}
	
}
