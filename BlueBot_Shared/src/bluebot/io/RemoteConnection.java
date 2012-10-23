package bluebot.io;


import java.io.IOException;

import bluebot.io.protocol.Channel;
import bluebot.io.protocol.Packet;



/**
 * Represents a remote {@link Connection}
 * 
 * @author Ruben Feyen
 */
public abstract class RemoteConnection extends AbstractConnection {
	
	private Channel channel;
	
	
	public RemoteConnection(final Channel channel) {
		this.channel = channel;
	}
	
	
	
	/**
	 * Returns the communication channel
	 * 
	 * @return a {@link Channel} object
	 * 
	 * @throws IOException if the connection has been closed
	 */
	private final Channel getChannel() throws IOException {
		if (channel == null) {
			throw new IOException("The connection has been closed");
		}
		return channel;
	}
	
	protected Packet read() throws IOException {
		return getChannel().readPacket();
	}
	
	protected void write(final Packet packet) throws IOException {
		getChannel().writePacket(packet);
	}
	
}