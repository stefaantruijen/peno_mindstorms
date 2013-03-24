package bluebot.core;


import static bluebot.io.protocol.Packet.*;

import java.io.IOException;

import bluebot.game.Game;
import bluebot.game.GameException;
import bluebot.io.ClientTranslator;
import bluebot.io.Communicator;
import bluebot.io.Connection;
import bluebot.io.ConnectionListener;
import bluebot.io.protocol.Packet;
import bluebot.io.protocol.PacketHandler;
import bluebot.io.protocol.impl.ConfigPacket;
import bluebot.io.protocol.impl.DebugPacket;
import bluebot.io.protocol.impl.ErrorPacket;
import bluebot.io.protocol.impl.MQMessagePacket;
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
	
	
	public DefaultController(final Connection connection) throws IOException {
//		rabbit.registerListener(RabbitConfig.MONITOR_KEY, new Listener() {
//			public void onMessage(final Date time,
//					final String key, final String msg) {
//				fireMessageIncoming(new RabbitMessage(msg, key));
//			}
//		});
		
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
	
	@Override
	public void dispose() {
		super.dispose();
		getTranslator().disconnect();
	}
	
	public void doCalibrate() {
		getTranslator().doCalibrate();
	}
	
	public Game doGame(final String gameId, final String playerId)
			throws GameException, IOException {
//		final Game game = new Game(this, gameId, playerId);
//		game.init(new Callback<Void>() {
//			public void onFailure(final Throwable error) {
//				fireError(error.getMessage());
//			}
//			
//			public void onSuccess(final Void result) {
//				//	ignored
//			}
//		});
//		return game;
		//	TODO
		return null;
	}
	
	public void doMaze(final int[] playerIds, final int playerId) {
		getTranslator().doMaze(playerIds, playerId);
	}
	
	public void doPolygon(final int corners, final float length) {
		getTranslator().doPolygon(corners, length);
	}
	
	public void doTile() {
		getTranslator().doTile();
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
	
	public void init() {
		setSpeed(100);
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
	
	@Override
	public void removeListeners() {
		super.removeListeners();
		getCommunicator().removeListeners();
	}
	
	public void reset() {
		getTranslator().reset();
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
				case OP_DEBUG:
					// Print debug messages to console
					System.out.println("[DEBUG]  " + ((DebugPacket)packet).getMessage());
					break;
				case OP_ERROR:
					fireError(((ErrorPacket)packet).getMessage());
					break;
				case OP_MESSAGE:
					final MessagePacket p = (MessagePacket)packet;
					fireMessage(p.getMessage(), p.getTitle());
					break;
				case OP_MQMESSAGE:
					handlePacketMQMessage((MQMessagePacket)packet);
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
		
		private final void handlePacketMQMessage(final MQMessagePacket packet) {
//			sendMessageRabbitMQ(packet.getMessage());
			//	TODO
		}
		
		private final void handlePacketMotion(final MotionPacket packet) {
			fireMotion(packet.getX(), packet.getY(),
					packet.getHeadingBody(),
					packet.getHeadingHead());
		}
		
		private final void handlePacketSensor(final SensorPacket packet) {
			switch (packet.getSensorType()) {
				case INFRARED:
					fireSensorInfrared(packet.getSensorValue());
					break;
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
