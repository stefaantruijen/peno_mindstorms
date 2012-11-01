package bluebot.core;


import static bluebot.io.protocol.Packet.*;

import bluebot.ConfigListener;
import bluebot.graph.Tile;
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
import bluebot.maze.MazeListener;
import bluebot.sensors.SensorListener;
import bluebot.util.AbstractEventDispatcher;



/**
 * 
 * @author Ruben Feyen
 */
public class DefaultController extends AbstractController {
	
	private Communicator communicator;
	private ConfigDispatcher config;
	private MazeDispatcher maze;
	private SensorDispatcher sensors;
	private ClientTranslator translator;
	
	
	public DefaultController(final Connection connection) {
		this.communicator = new Communicator(connection, createPacketHandler());
		this.config = new ConfigDispatcher();
		this.maze = new MazeDispatcher();
		this.sensors = new SensorDispatcher();
		this.translator = new ClientTranslator(connection);
		
		this.communicator.start();
	}
	
	
	
	public void addListener(final ConfigListener listener) {
		config.addListener(listener);
	}
	
	public void addListener(final ConnectionListener listener) {
		getCommunicator().addListener(listener);
	}
	
	public void addListener(final MazeListener listener) {
		maze.addListener(listener);
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
	
	public void removeListener(final ConfigListener listener) {
		config.removeListener(listener);
	}
	
	public void removeListener(final ConnectionListener listener) {
		getCommunicator().removeListener(listener);
	}
	
	public void removeListener(final MazeListener listener) {
		maze.removeListener(listener);
	}
	
	public void removeListener(final SensorListener listener) {
		sensors.removeListener(listener);
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
	
	
	
	
	
	
	
	
	
	
	private final class BrainzHandler implements PacketHandler {
		
		public void handlePacket(final Packet packet) {
			switch (packet.getOpcode()) {
				case OP_CONFIG:
					config.handlePacket((ConfigPacket)packet);
					break;
				case OP_ERROR:
					fireError(((ErrorPacket)packet).getMessage());
					break;
				case OP_MESSAGE:
					final MessagePacket p = (MessagePacket)packet;
					fireMessage(p.getMessage(), p.getTitle());
					break;
				case OP_MOTION:
					maze.handlePacket((MotionPacket)packet);
					break;
				case OP_SENSOR:
					sensors.handlePacket((SensorPacket)packet);
					break;
				case OP_TILE:
					maze.handlePacket((TilePacket)packet);
					break;
			}
		}
		
	}
	
	
	
	
	
	private static final class ConfigDispatcher extends AbstractEventDispatcher<ConfigListener> {
		
		private final void fireSpeedChanged(final int percentage) {
			for (final ConfigListener listener : getListeners()) {
				listener.onSpeedChanged(percentage);
			}
		}
		
		public void handlePacket(final ConfigPacket packet) {
			switch (packet.getId()) {
				case ConfigPacket.ID_SPEED:
					fireSpeedChanged(packet.getValue().intValue());
					break;
			}
		}
		
	}
	
	
	
	
	
	private static final class MazeDispatcher extends AbstractEventDispatcher<MazeListener> {
		
		private final void fireMotion(final float x, final float y,
				final float heading) {
			for (final MazeListener listener : getListeners()) {
				listener.onMotion(x, y, heading);
			}
		}
		
		private final void fireTileUpdated(final Tile tile) {
			for (final MazeListener listener : getListeners()) {
				listener.onTileUpdate(tile);
			}
		}
		
		public void handlePacket(final MotionPacket packet) {
			fireMotion(packet.getX(), packet.getY(), packet.getHeading());
		}
		
		public void handlePacket(final TilePacket packet) {
			fireTileUpdated(packet.getTile());
		}
		
	}
	
	
	
	
	
	private static final class SensorDispatcher extends AbstractEventDispatcher<SensorListener> {
		
		private final void fireLight(final int value) {
			for (final SensorListener listener : getListeners()) {
				listener.onSensorValueLight(value);
			}
		}
		
		private final void fireUltraSonic(final int value) {
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