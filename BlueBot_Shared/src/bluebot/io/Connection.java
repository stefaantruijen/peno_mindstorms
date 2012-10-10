package bluebot.io;


import java.io.IOException;

import bluebot.io.protocol.Channel;
import bluebot.io.protocol.Packet;



/**
 * 
 * @author Ruben Feyen
 */
public abstract class Connection {
	
	private Channel channel;
	
	
	public Connection(final Channel channel) {
		this.channel = channel;
	}
	
	
	
	public abstract void close() throws IOException;
	
	private final Channel getChannel() throws IOException {
		if (channel == null) {
			throw new IOException("The connection has been closed");
		}
		return channel;
	}
	
	public Packet readPacket() throws IOException {
		return getChannel().readPacket();
	}
	
	public void writePacket(final Packet packet) throws IOException {
		getChannel().writePacket(packet);
	}
	
}