package bluebot.io.protocol;


import bluebot.io.protocol.impl.CommandPacket;
import bluebot.io.protocol.impl.ErrorPacket;
import bluebot.io.protocol.impl.MovePacket;
import bluebot.io.protocol.impl.SensorPacket;
import bluebot.io.protocol.impl.StopPacket;
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
	
	public Packet createCommandCalibrate() {
		return createCommand(CommandPacket.CALIBRATE);
	}
	
	public Packet createCommandWhiteLineOrientation() {
		return createCommand(CommandPacket.WHITE_LINE_ORIENTATION);
	}
	
	public Packet createError(final String msg) {
		return new ErrorPacket(msg);
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