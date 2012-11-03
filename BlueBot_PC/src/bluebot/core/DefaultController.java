package bluebot.core;


import static bluebot.io.protocol.Packet.*;

import bluebot.io.ClientTranslator;
import bluebot.io.Communicator;
import bluebot.io.Connection;
import bluebot.io.ConnectionListener;
import bluebot.io.protocol.Packet;
import bluebot.io.protocol.PacketHandler;
import bluebot.io.protocol.impl.ConfigPacket;
import bluebot.io.protocol.impl.ErrorPacket;
import bluebot.io.protocol.impl.MessagePacket;
import bluebot.io.protocol.impl.MotionPacket;
import bluebot.io.protocol.impl.SensorPacket;
import bluebot.io.protocol.impl.TilePacket;



/**
 * 
 * @author Ruben Feyen
 */
public class DefaultController extends AbstractController {
	
	private Communicator communicator;
	private ClientTranslator translator;
	
	
	public DefaultController(final Connection connection) {
		this.communicator = new Communicator(connection, createPacketHandler());
		this.translator = new ClientTranslator(connection);
		
		this.communicator.start();
	}
	
	
	
	public void addListener(final ConnectionListener listener) {
		getCommunicator().addListener(listener);
	}
	
	protected PacketHandler createPacketHandler() {
		return new ControllerHandler();
	}
	
	public void doCalibrate() {
		getTranslator().doCalibrate();
	}
	
	public void doPolygon(final int corners, final float length) {
		getTranslator().doPolygon(corners, length);
	}
	
	public void doWhiteLineOrientation() {
		getTranslator().doWhiteLineOrientation();
	}
	
	private final Communicator getCommunicator() {
		return communicator;
	}
	
	private final ClientTranslator getTranslator() {
		return translator;
	}
	
	public void moveBackward() {
		getTranslator().moveBackward();
	}
	
	public void moveBackward(final float distance) {
		getTranslator().moveBackward(distance);
	}
	
	public void moveForward() {
		getTranslator().moveForward();
	}
	
	public void moveForward(final float distance) {
		getTranslator().moveForward(distance);
	}
	
	public void removeListener(final ConnectionListener listener) {
		getCommunicator().removeListener(listener);
	}
	
	public void setSpeed(final int percentage) {
		getTranslator().setSpeed(percentage);
	}
	
	public void stop() {
		getTranslator().stop();
	}

	@Override
	public void turnLeft() {
		getTranslator().turnLeft();
	}
	
	public void turnLeft(final float angle) {
		getTranslator().turnLeft(angle);
	}
	
	public void turnRight() {
		getTranslator().turnRight();
	}
	
	public void turnRight(final float angle) {
		getTranslator().turnRight(angle);
	}
	
	
	
	
	
	
	
	
	
	
	private final class ControllerHandler implements PacketHandler {
		
		public void handlePacket(final Packet packet) {
			switch (packet.getOpcode()) {
				case OP_CONFIG:
					handlePacketConfig((ConfigPacket)packet);
					break;
				case OP_ERROR:
					fireError(((ErrorPacket)packet).getMessage());
					break;
				case OP_MESSAGE:
					final MessagePacket p = (MessagePacket)packet;
					fireMessage(p.getMessage(), p.getTitle());
					break;
				case OP_MOTION:
					handlePacketMotion((MotionPacket)packet);
					break;
				case OP_SENSOR:
					handlePacketSensor((SensorPacket)packet);
					break;
				case OP_TILE:
					handlePacketTile((TilePacket)packet);
					break;
			}
		}
		
		private final void handlePacketConfig(final ConfigPacket packet) {
			switch (packet.getId()) {
				case ConfigPacket.ID_SPEED:
					fireSpeedChanged(packet.getValue().intValue());
					break;
			}
		}
		
		private final void handlePacketMotion(final MotionPacket packet) {
			fireMotion(packet.getX(), packet.getY(), packet.getHeading());
		}
		
		private final void handlePacketSensor(final SensorPacket packet) {
			switch (packet.getSensorType()) {
				case LIGHT:
					fireSensorLight(packet.getSensorValue());
					break;
				case ULTRA_SONIC:
					fireSensorUltraSonic(packet.getSensorValue());
					break;
			}
		}
		
		private final void handlePacketTile(final TilePacket packet) {
			fireTileUpdated(packet.getTile());
		}
		
	}
	
}