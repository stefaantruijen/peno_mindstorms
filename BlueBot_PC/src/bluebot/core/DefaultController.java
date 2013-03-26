package bluebot.core;


import static bluebot.io.protocol.Packet.*;

import java.io.IOException;
import java.util.List;

import peno.htttp.Callback;
import peno.htttp.PlayerClient;
import peno.htttp.PlayerHandler;

import bluebot.game.Game;
import bluebot.game.GameAdapter;
import bluebot.game.GameException;
import bluebot.game.World;
import bluebot.graph.Tile;
import bluebot.io.ClientTranslator;
import bluebot.io.Communicator;
import bluebot.io.Connection;
import bluebot.io.ConnectionListener;
import bluebot.io.protocol.Packet;
import bluebot.io.protocol.PacketHandler;
import bluebot.io.protocol.impl.ConfigPacket;
import bluebot.io.protocol.impl.DebugPacket;
import bluebot.io.protocol.impl.ErrorPacket;
import bluebot.io.protocol.impl.ItemPacket;
import bluebot.io.protocol.impl.MQMessagePacket;
import bluebot.io.protocol.impl.MessagePacket;
import bluebot.io.protocol.impl.MotionPacket;
import bluebot.io.protocol.impl.SeesawPacket;
import bluebot.io.protocol.impl.SensorPacket;
import bluebot.io.protocol.impl.TilePacket;



/**
 * 
 * @author Ruben Feyen
 */
public class DefaultController extends AbstractController {
	
	private Communicator communicator;
	private Game game;
	private ClientTranslator translator;
	private World world;
	
	
	public DefaultController(final World world, final Connection connection)
			throws IOException {
		this.communicator = new Communicator(connection, createPacketHandler());
		this.translator = new ClientTranslator(connection);
		this.world = world;
		
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
		
		final Game game = this.game;
		if (game != null) {
			this.game = null;
			game.stop();
		}
		
		getTranslator().disconnect();
	}
	
	public void doCalibrate() {
		getTranslator().doCalibrate();
	}
	
	public Game doGame(final String gameId, final String playerId,
			final PlayerHandler handler) throws GameException, IOException {
		final Game game = new Game(gameId,
				playerId,
				new PlayerMonitor(handler),
				getWorld());
		System.out.println("JOIN");
		game.init(new Callback<Void>() {
			public void onFailure(final Throwable error) {
				System.out.println("FAILURE");
				fireError(error.getMessage());
			}
			
			public void onSuccess(final Void result) {
				System.out.println("SUCCESS");
				DefaultController.this.game = game;
				game.start();
			}
		});
		return game;
	}
	
	public void doMaze(final int playerNumber, final int itemNumber) {
		getTranslator().doMaze(playerNumber, itemNumber);
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
	
	private final Game getGame() {
		return game;
	}
	
	private final PlayerClient getGameClient() {
		try {
			return getGame().getClient();
		} catch (final NullPointerException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private final ClientTranslator getTranslator() {
		return translator;
	}
	
	public World getWorld() {
		return world;
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
		
		final Game game = this.game;
		if (game != null) {
			this.game = null;
			game.stop();
		}
	}
	
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
				case OP_ITEM:
					handlePacketItem((ItemPacket)packet);
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
				case OP_SEESAW:
					handlePacketSeesaw((SeesawPacket)packet);
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
		
		private final void handlePacketItem(final ItemPacket packet) {
			//	TODO
			final PlayerClient client = getGameClient();
			if (client == null) {
				return;
			}
			
			try {
				if (!client.hasTeamNumber()) {
					client.joinTeam(packet.getTeamId());
				}
				if (!client.hasFoundObject()) {
					client.foundObject();
				}
			} catch (final IllegalStateException e) {
				e.printStackTrace();
			} catch (final IOException e) {
				e.printStackTrace();
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
		
		private final void handlePacketSeesaw(final SeesawPacket packet) {
			final PlayerClient client = getGameClient();
			if (client == null) {
				return;
			}
			
			try {
				final int barcode = packet.getBarcode();
				if (packet.isLocked()) {
					if (!client.hasLockOnSeesaw()) {
						client.lockSeesaw(barcode);
//						client.requestSeesawLock(barcode, new Callback<Boolean>() {
//							public void onFailure(final Throwable error) {
//								error.printStackTrace();
//							}
//							
//							public void onSuccess(final Boolean result) {
//								System.out.println("Seesaw lock:  " + result);
//							}
//						});
					}
				} else if (client.hasLockOnSeesaw(barcode)) {
					client.unlockSeesaw();
				}
			} catch (final IllegalStateException e) {
				//	This should not be possible!
				e.printStackTrace();
			} catch (final IOException e) {
				e.printStackTrace();
			}
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
			final Tile tile = packet.getTile();
			fireTileUpdated(tile);
			
			final Game game = getGame();
			if (game == null) {
				return;
			}
			
			game.updateTile(tile);
			
			if (!tile.isExplored()) {
				return;
			}
			
			final PlayerClient client = game.getClient();
			if (client == null) {
				return;
			}
			
			if (!client.hasTeamPartner()) {
				return;
			}
			
			//	TODO
			/*
			try {
				client.sendTiles(new peno.htttp.Tile(tile.getX(), tile.getY(),
						null));
			} catch (final IOException e) {
				e.printStackTrace();
			}
			*/
		}
		
	}
	
	
	
	
	
	private final class PlayerMonitor extends GameAdapter implements PlayerHandler {
		
		private PlayerHandler delegate;
		private int objectNumber;
		private int playerNumber;
		
		
		public PlayerMonitor(final PlayerHandler delegate) {
			this.delegate = delegate;
		}
		
		
		
		public void gameRolled(final int playerNumber, final int objectNumber) {
			boolean ready;
			try {
				delegate.gameRolled(playerNumber, objectNumber);
				ready = true;
			} catch (final Exception e) {
				ready = false;
			}
			
			try {
				getGameClient().setReady(ready);
			} catch (final IOException e) {
				e.printStackTrace();
			}
			
			this.objectNumber = objectNumber;
			this.playerNumber = playerNumber;
		}
		
		@Override
		public void gameStarted() {
			doMaze(playerNumber, objectNumber);
		}
		
		public void teamConnected(final String partnerId) {
			//	TODO
		}
		
		public void teamPosition(final double x, final double y, final double angle) {
			//	TODO
		}
		
		public void teamTilesReceived(final List<peno.htttp.Tile> tiles) {
			//	TODO
		}
		
	}
	
}
