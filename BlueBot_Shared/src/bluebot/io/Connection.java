package bluebot.io;


import java.io.IOException;

import bluebot.io.protocol.Channel;
import bluebot.io.protocol.Packet;
import bluebot.util.AbstractEventDispatcher;



/**
 * Represents a link between two actors capable of transferring packets
 * 
 * @author Ruben Feyen
 */
public abstract class Connection extends AbstractEventDispatcher<ConnectionListener> {
	
	private Channel channel;
	
	
	public Connection(final Channel channel) {
		this.channel = channel;
	}
	
	
	
	/**
	 * Closes the connection
	 * 
	 * @throws IOException if an I/O error occurs
	 */
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
	
	/**
	 * Reads an incoming packet
	 * 
	 * @return a {@link Packet} object
	 * 
	 * @throws IOException if an I/O error occurs
	 */
	public Packet readPacket() throws IOException {
		final Packet packet = getChannel().readPacket();
		fireMessageIncoming(packet.toString());
		return packet;
	}
	
	/**
	 * Writes an outgoing packet
	 * 
	 * @param packet - a {@link Packet} object
	 * 
	 * @throws IOException if an I/O error occurs
	 */
	public void writePacket(final Packet packet) throws IOException {
		getChannel().writePacket(packet);
		fireMessageOutgoing(packet.toString());
	}
	
}