package bluebot.core;


import static bluebot.io.protocol.Packet.*;

import bluebot.io.ClientTranslator;
import bluebot.io.Communicator;
import bluebot.io.Connection;
import bluebot.io.ConnectionListener;
import bluebot.io.protocol.Packet;
import bluebot.io.protocol.PacketHandler;
import bluebot.io.protocol.impl.ErrorPacket;
import bluebot.io.protocol.impl.SensorPacket;
import bluebot.sensors.SensorListener;
import bluebot.util.AbstractEventDispatcher;



/**
 * 
 * @author Ruben Feyen
 */
public class DefaultController extends AbstractController {
	
	private Communicator communicator;
	private SensorDispatcher sensors;
	private ClientTranslator translator;
	
	
	public DefaultController(final Connection connection) {
		this.communicator = new Communicator(connection, createPacketHandler());
		this.sensors = new SensorDispatcher();
		this.translator = new ClientTranslator(connection);
		
		this.communicator.start();
	}
	
	
	
	public void addListener(final ConnectionListener listener) {
		getCommunicator().addListener(listener);
	}
	
	public void addListener(final SensorListener listener) {
		sensors.addListener(listener);
	}
	
	protected PacketHandler createPacketHandler() {
		return new BrainzHandler();
	}
	
	public void doCalibrate() {
		getTranslator().doCalibrate();
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
	
	public void removeListener(final SensorListener listener) {
		sensors.removeListener(listener);
	}
	
	public void setSpeedHigh() {
		getTranslator().setSpeedHigh();
	}
	
	public void setSpeedLow() {
		getTranslator().setSpeedLow();
	}
	
	public void setSpeedMedium() {
		getTranslator().setSpeedMedium();
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
	
	
	
	
	
	
	
	
	
	
	private final class BrainzHandler implements PacketHandler {
		
		public void handlePacket(final Packet packet) {
			switch (packet.getOpcode()) {
				case OP_ERROR:
					fireError(((ErrorPacket)packet).getMessage());
					break;
				case OP_SENSOR:
					sensors.handlePacket((SensorPacket)packet);
					break;
			}
		}
		
	}
	
	
	
	
	
	private static final class SensorDispatcher extends AbstractEventDispatcher<SensorListener> {
		
		public void fireLight(final int value) {
			for (final SensorListener listener : getListeners()) {
				listener.onSensorValueLight(value);
			}
		}
		
		public void fireUltraSonic(final int value) {
			for (final SensorListener listener : getListeners()) {
				listener.onSensorValueUltraSonic(value);
			}
		}
		
		public void handlePacket(final SensorPacket packet) {
			switch (packet.getSensorType()) {
				case LIGHT:
					fireLight(packet.getSensorValue());
					break;
				case ULTRA_SONIC:
					fireUltraSonic(packet.getSensorValue());
					break;
			}
		}
		
	}

}