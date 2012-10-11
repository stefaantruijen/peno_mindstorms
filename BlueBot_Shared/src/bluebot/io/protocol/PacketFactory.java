package bluebot.io.protocol;


import bluebot.io.protocol.impl.MovePacket;
import bluebot.io.protocol.impl.StopPacket;



/**
 * 
 * @author Ruben Feyen
 */
public class PacketFactory {
	
	private static PacketFactory singleton;
	
	
	private PacketFactory() {
		// hidden
	}
	
	
	
	public Packet createMove(final int direction, final float quantity) {
		return new MovePacket((byte)direction, quantity);
	}
	
	public Packet createStop() {
		return StopPacket.SINGLETON;
	}
	
	public static PacketFactory getPacketFactory() {
		if (singleton == null) {
			singleton = new PacketFactory();
		}
		return singleton;
	}
	
}