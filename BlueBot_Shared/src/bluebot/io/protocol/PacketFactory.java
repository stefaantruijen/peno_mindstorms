package bluebot.io.protocol;


import bluebot.io.protocol.impl.MovePacket;
import bluebot.io.protocol.impl.StopPacket;



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
	
	
	
	/**
	 * Creates a movement packet
	 * 
	 * @param direction - the desired direction (2/4/6/8)
	 * @param quantity - the desired quantity (in mm or degrees)
	 * 
	 * @return a {@link Packet} object
	 */
	public Packet createMove(final int direction, final float quantity) {
		return new MovePacket((byte)direction, quantity);
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