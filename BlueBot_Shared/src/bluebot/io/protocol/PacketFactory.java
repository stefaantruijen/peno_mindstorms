package bluebot.io.protocol;


import bluebot.graph.Tile;
import bluebot.io.protocol.impl.CommandPacket;
import bluebot.io.protocol.impl.ConfigPacket;
import bluebot.io.protocol.impl.DebugPacket;
import bluebot.io.protocol.impl.DisconnectPacket;
import bluebot.io.protocol.impl.ErrorPacket;
import bluebot.io.protocol.impl.MQMessagePacket;
import bluebot.io.protocol.impl.MazePacket;
import bluebot.io.protocol.impl.MessagePacket;
import bluebot.io.protocol.impl.MotionPacket;
import bluebot.io.protocol.impl.MovePacket;
import bluebot.io.protocol.impl.PolygonPacket;
import bluebot.io.protocol.impl.ResetPacket;
import bluebot.io.protocol.impl.SeesawPacket;
import bluebot.io.protocol.impl.SensorPacket;
import bluebot.io.protocol.impl.StopPacket;
import bluebot.io.protocol.impl.TilePacket;
import bluebot.sensors.SensorType;



/**
 * Factory class for {@link Packet} objects
 * 
 * @author Ruben Feyen
 */
public class PacketFactory {
	
	private static PacketFactory singleton;
	
	
	private PacketFactory() {
		// hidden
	}
	
	
	
	private final Packet createCommand(final String command) {
		return new CommandPacket(command);
	}
	
	/**
	 * Creates a command packet for the calibration algorithm
	 * 
	 * @return a {@link Packet} object
	 */
	public Packet createCommandCalibrate() {
		return createCommand(CommandPacket.CALIBRATE);
	}
	
	/**
	 * Creates a command packet for the maze algorithm
	 * 
	 * @param playerIds - an array containing all possible player IDs
	 * @param playerId - an <code>int</code> representing the player ID
	 * 
	 * @return a {@link Packet} object
	 */
	public Packet createCommandMaze(final int[] playerIds, final int playerId) {
		return new MazePacket(playerIds, playerId);
	}
	
	/**
	 * Creates a command packet for the "Check tile" algorithm
	 * 
	 * @return a {@link Packet} object
	 */
	public Packet createCommandTile() {
		return createCommand(CommandPacket.TILE);
	}
	
	/**
	 * Creates a command packet for the white-line-orientation algorithm
	 * 
	 * @return a {@link Packet} object
	 */
	public Packet createCommandWhiteLineOrientation() {
		return createCommand(CommandPacket.WHITE_LINE_ORIENTATION);
	}
	
	/**
	 * Creates a speed config packet
	 * 
	 * @param percentage - the percentage (of the maximum speed)
	 * 
	 * @return a {@link Packet} object
	 */
	public Packet createConfigSpeed(final int percentage) {
		return new ConfigPacket(ConfigPacket.ID_SPEED, percentage);
	}
	
	/**
	 * Creates a debug (message) packet
	 * 
	 * @param msg - the debug message
	 * 
	 * @return a {@link Packet} object
	 */
	public Packet createDebug(final String msg) {
		return new DebugPacket(msg);
	}
	
	/**
	 * Creates a disconnect packet
	 * 
	 * @return a {@link Packet} object
	 */
	public Packet createDisconnect() {
		return DisconnectPacket.SINGLETON;
	}
	
	/**
	 * Creates an error packet
	 * 
	 * @param msg - the (error) message
	 * 
	 * @return a {@link Packet} object
	 */
	public Packet createError(final String msg) {
		return new ErrorPacket(msg);
	}
	
	/**
	 * Creates a message packet
	 * 
	 * @param msg - the message
	 * @param title - a title for the message
	 * 
	 * @return a {@link Packet} object
	 */
	public Packet createMessage(final String msg, final String title) {
		return new MessagePacket(msg, title);
	}
	
	/**
	 * Creates an MQmessage packet
	 * 
	 * @param msg - the message
	 * 
	 * @return a {@link Packet} object
	 */
	public Packet createMQMessage(final String msg) {
		return new MQMessagePacket(msg);
	}
	
	
	/**
	 * Creates a motion (update) packet
	 * 
	 * @param x - the position on the X axis
	 * @param y - the position on the Y axis
	 * @param body - the heading of the body
	 * @param head - the heading of the head
	 * 
	 * @return a {@link Packet} object
	 */
	public Packet createMotion(final float x, final float y,
			final float body, final float head) {
		return new MotionPacket(x, y, body, head);
	}
	
	private final Packet createMove(final int direction) {
		return new MovePacket(direction);
	}
	
	/**
	 * Creates a movement packet
	 * 
	 * @param direction - the desired direction
	 * @param quantity - the desired quantity (in mm or degrees)
	 * 
	 * @return a {@link Packet} object
	 */
	private final Packet createMove(final int direction, final float quantity) {
		return new MovePacket(direction, quantity);
	}
	
	/**
	 * Creates a packet to move backward (continuously)
	 * 
	 * @return a {@link Packet} object
	 */
	public Packet createMoveBackward() {
		return createMove(MovePacket.MOVE_BACKWARD);
	}
	
	/**
	 * Creates a packet to move backward over a given distance
	 * 
	 * @param distance - the desired distance
	 * 
	 * @return a {@link Packet} object
	 */
	public Packet createMoveBackward(final float distance) {
		return createMove(MovePacket.MOVE_BACKWARD, distance);
	}
	
	/**
	 * Creates a packet to move forward (continuously)
	 * 
	 * @return a {@link Packet} object
	 */
	public Packet createMoveForward() {
		return createMove(MovePacket.MOVE_FORWARD);
	}
	
	/**
	 * Creates a packet to move forward over a given distance
	 * 
	 * @param distance - the desired distance
	 * 
	 * @return a {@link Packet} object
	 */
	public Packet createMoveForward(final float distance) {
		return new MovePacket(MovePacket.MOVE_FORWARD, distance);
	}
	
	/**
	 * Creates a polygon packet
	 * 
	 * @param corners - the number of corners of the polygon
	 * @param length - the length of the side of the polygon
	 * 
	 * @return a {@link Packet} object
	 */
	public Packet createPolygon(final int corners, final float length) {
		return new PolygonPacket(corners, length);
	}
	
	/**
	 * Creates a reset packet
	 * 
	 * @return a {@link Packet} object
	 */
	public Packet createReset() {
		return ResetPacket.SINGLETON;
	}
	
	/**
	 * Creates a seesaw packet
	 * 
	 * @param barcode - the barcode of the seesaw (open side)
	 * @param locked - determines whether we drive on or off the seesaw
	 * 
	 * @return a {@link Packet} object
	 */
	public Packet createSeesaw(final int barcode, final boolean locked) {
		return new SeesawPacket(barcode, locked);
	}
	
	/**
	 * Creates a sensor (value) request packet
	 * 
	 * @param type - the type of sensor
	 * 
	 * @return a {@link Packet} object
	 */
	public Packet createSensorRequest(final SensorType type) {
		return new SensorPacket(type);
	}
	
	/**
	 * Creates a sensor (value) response packet
	 * 
	 * @param type - the type of sensor
	 * @param value - the value read from the sensor
	 * 
	 * @return a {@link Packet} object
	 */
	public Packet createSensorResponse(final SensorType type, final int value) {
		return new SensorPacket(type, value);
	}
	
	/**
	 * Creates a stop packet
	 * 
	 * @return a {@link Packet} object
	 */
	public Packet createStop() {
		return StopPacket.SINGLETON;
	}
	
	/**
	 * Creates a tile (update) packet
	 * 
	 * @param tile - the updated {@link Tile}
	 * 
	 * @return a {@link Packet} object
	 */
	public Packet createTile(final Tile tile) {
		return new TilePacket(tile);
	}
	
	/**
	 * Creates a packet to turn left (continuously)
	 * 
	 * @return a {@link Packet} object
	 */
	public Packet createTurnLeft() {
		return new MovePacket(MovePacket.TURN_LEFT);
	}
	
	/**
	 * Creates a packet to turn left over a given angle
	 * 
	 * @param degrees - the desired angle (in degrees)
	 * 
	 * @return a {@link Packet} object
	 */
	public Packet createTurnLeft(final float degrees) {
		return new MovePacket(MovePacket.TURN_LEFT, degrees);
	}
	
	/**
	 * Creates a packet to turn right (continuously)
	 * 
	 * @return a {@link Packet} object
	 */
	public Packet createTurnRight() {
		return new MovePacket(MovePacket.TURN_RIGHT);
	}
	
	/**
	 * Creates a packet to turn right over a given angle
	 * 
	 * @param degrees - the desired angle (in degrees)
	 * 
	 * @return a {@link Packet} object
	 */
	public Packet createTurnRight(final float degrees) {
		return new MovePacket(MovePacket.TURN_RIGHT, degrees);
	}
	
	/**
	 * Returns the (current) {@link PacketFactory} instance
	 * 
	 * @return a {@link PacketFactory} object
	 */
	public static PacketFactory getPacketFactory() {
		if (singleton == null) {
			singleton = new PacketFactory();
		}
		return singleton;
	}
	
}
