package bluebot.io.protocol;


import java.util.HashMap;



/**
 * 
 * @author Ruben Feyen
 */
public class DelegatingPacketHandler implements PacketHandler {
	
	private HashMap<Byte, PacketHandler> handlers;
	
	
	public DelegatingPacketHandler() {
		this.handlers = new HashMap<>();
	}
	
	
	
	public void handlePacket(final Packet packet) {
		final PacketHandler handler = handlers.get(packet.getOpcode());
		if (handler != null) {
			handler.handlePacket(packet);
		}
	}
	
	public void registerPacketHandler(final byte opcode, final PacketHandler handler) {
		handlers.put(opcode, handler);
	}
	
}