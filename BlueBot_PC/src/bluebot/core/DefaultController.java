package bluebot.core;


import static bluebot.io.protocol.Packet.*;

import java.io.IOException;

import peno.htttp.Callback;

import bluebot.DriverException;
import bluebot.actions.ActionException;
import bluebot.actionsimpl.MazeActionV2;
import bluebot.game.Game;
import bluebot.game.GameCallback;
import bluebot.game.GameException;
import bluebot.game.World;
import bluebot.io.ClientTranslator;
import bluebot.io.Communicator;
import bluebot.io.Connection;
import bluebot.io.ConnectionListener;
import bluebot.io.protocol.Packet;
import bluebot.io.protocol.PacketHandler;
import bluebot.io.protocol.impl.BarcodePacket;
import bluebot.io.protocol.impl.ConfigPacket;
import bluebot.io.protocol.impl.DebugPacket;
import bluebot.io.protocol.impl.ErrorPacket;
import bluebot.io.protocol.impl.MessagePacket;
import bluebot.io.protocol.impl.MotionPacket;
import bluebot.io.protocol.impl.SensorPacket;
import bluebot.maze.MazeListener;



/**
 * 
 * @author Ruben Feyen
 */
public class DefaultController extends AbstractController {
	
	private Communicator communicator;
	private ClientTranslator translator;
	private World world;
	private int receivedBarcode;
	
	
	public DefaultController(final World world, final Connection connection)
			throws IOException {
		this.communicator = new Communicator(connection, createPacketHandler());
		this.translator = new ClientTranslator(connection);
		this.world = world;
		this.receivedBarcode = Integer.MIN_VALUE;
		
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
	
	public Game doGame(final String gameId, final String playerId,
			final GameCallback callback) throws GameException, IOException {
		final Game game = new Game(this, gameId, playerId, callback);
		System.out.println("JOIN");
		game.init(new Callback<Void>() {
			public void onFailure(final Throwable error) {
				System.out.println("FAILURE");
				fireError(error.getMessage());
			}
			
			public void onSuccess(final Void result) {
				System.out.println("SUCCESS");
				game.start();
			}
		});
		return game;
	}
	
	public MazeActionV2 doMaze(final int playerNumber, final int objectNumber,
			final MazeListener listener) {
		final MazeActionV2 maze =
				new MazeActionV2(this, playerNumber, objectNumber, listener);
		
		final Thread thread = new Thread(new Runnable() {
			public void run() {
				try {
					maze.execute();
				}  catch (final DriverException e) {
					e.printStackTrace();
				} catch (final InterruptedException e) {
					//	ignored
				}
			}
		});
		thread.setDaemon(true);
		thread.start();
		
		return maze;
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
	
	public World getWorld() {
		return world;
	}
	
	public void init() {
		setSpeed(100);
	}
	
	public void moveBackward() {
		getTranslator().moveBackward();
	}
	
	public void moveBackward(final float distance,boolean b) {
		getTranslator().moveBackward(distance,b);
	}
	
	public void moveForward() {
		getTranslator().moveForward();
	}
	
	public void moveForward(final float distance,final boolean b) {
		getTranslator().moveForward(distance,b);
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
	
	public void turnLeft() {
		getTranslator().turnLeft();
	}
	
	public void turnLeft(final float angle,boolean b) {
		getTranslator().turnLeft(angle,b);
	}
	
	public void turnRight() {
		getTranslator().turnRight();
	}
	
	public void turnRight(final float angle,boolean b) {
		getTranslator().turnRight(angle,b);
	}
	
	public int getReceivedBarcode(){
		int result = Integer.MIN_VALUE;
		if(receivedBarcode!=Integer.MIN_VALUE){
			result=receivedBarcode;
			receivedBarcode = Integer.MIN_VALUE;
		}
		return result;
	}
	
	public void setReceivedBarcode(int barcode) {
		this.receivedBarcode = barcode;
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
				case OP_MOTION:
					handlePacketMotion((MotionPacket)packet);
					break;
				case OP_SENSOR:
					handlePacketSensor((SensorPacket)packet);
					break;
				case OP_BARCODE:
					handleBarcode((BarcodePacket)packet);
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
				case SPEED:
					break;
				default:
					break;
			}
		}
		
		
		private final void handleBarcode(BarcodePacket barcode){			
			setReceivedBarcode(barcode.getBarcode());
		}
		
	}
	
	public void doSeesaw() {
		getTranslator().doSeeSaw();
	}
	
	public void doReadBarcode() {
		getTranslator().readBarcode();
	}
	
	public void setStartLocation(int x, int y, float heading) {
		getTranslator().setStartLocation(x,y,heading);
	}


	/**
	 * Send a packet to do the pickUpAction
	 */
	@Override
	public void doPickUp() {
		getTranslator().doPickUp();
	}
	
	/**
	 * Send a packet to turn the head clockwise
	 */
	public void turnHeadClockwise(int offset) {
		getTranslator().turnHeadClockwise(offset);
	}
	
	/**
	 * Send a packet to turn the head counterclockwise.
	 */
	public void turnHeadCounterClockwise(int offset) {
		getTranslator().turnHeadcounterClockwise(offset);
	}
	
	/**
	 * Modifies the orientation of the robot to the closest of the following values: 0,90,180,270
	 */
	public void modifyOrientation(){
		getTranslator().modifyOrientation();
	}
	

}
