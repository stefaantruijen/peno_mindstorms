package bluebot.io.protocol;



/**
 * Represents an entity capable of handling packets
 * 
 * @author Ruben Feyen
 */
public interface PacketHandler {
	
	/**
	 * This method is called whenever a packet requires handling
	 * 
	 * @param packet - a {@link Packet} object
	 */
	public void handlePacket(Packet packet);
	
}