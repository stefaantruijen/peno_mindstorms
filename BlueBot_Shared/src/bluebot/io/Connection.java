package bluebot.io;


import java.io.IOException;

import bluebot.io.protocol.Channel;
import bluebot.io.protocol.Packet;
import bluebot.util.AbstractEventDispatcher;



/**
 * 
 * @author Ruben Feyen
 */
public abstract class Connection extends AbstractEventDispatcher<ConnectionListener> {
	
	private Channel channel;
	
	
	public Connection(final Channel channel) {
		this.channel = channel;
	}
	
	
	
	public abstract void close() throws IOException;
	
	private final void fireMessageIncoming(final String msg) {
		for (final ConnectionListener listener : getListeners()) {
			listener.onMessageIncoming(msg);
		}
	}
	
	private final void fireMessageOutgoing(final String msg) {
		for (final ConnectionListener listener : getListeners()) {
			listener.onMessageOutgoing(msg);
		}
	}
	
	private final Channel getChannel() throws IOException {
		if (channel == null) {
			throw new IOException("The connection has been closed");
		}
		return channel;
	}
	
	public Packet readPacket() throws IOException {
		final Packet packet = getChannel().readPacket();
		fireMessageIncoming(packet.toString());
		return packet;
	}
	
	public void writePacket(final Packet packet) throws IOException {
		getChannel().writePacket(packet);
		fireMessageOutgoing(packet.toString());
	}
	
}