package bluebot.io;


import java.io.IOException;

import bluebot.io.protocol.Packet;
import bluebot.util.AbstractEventDispatcher;



/**
 * Skeletal implementation of the {@link Connection} interface
 * 
 * @author Ruben Feyen
 */
public abstract class AbstractConnection
		extends AbstractEventDispatcher<ConnectionListener>
		implements Connection {
	
	/**
	 * Notifies all registered listeners about an incoming message
	 * 
	 * @param msg - the (incoming) message
	 */
	private final void fireMessageIncoming(final String msg) {
		for (final ConnectionListener listener : getListeners()) {
			listener.onMessageIncoming(msg);
		}
	}
	
	/**
	 * Notifies all registered listeners about an outgoing message
	 * 
	 * @param msg - the (outgoing) message
	 */
	private final void fireMessageOutgoing(final String msg) {
		for (final ConnectionListener listener : getListeners()) {
			listener.onMessageOutgoing(msg);
		}
	}
	
	/**
	 * This method provides the actual implementation for reading packets
	 */
	protected abstract Packet read() throws IOException;
	
	public Packet readPacket() throws IOException {
		final Packet packet = read();
		if (packet.isVerbose()) {
			fireMessageIncoming(packet.toString());
		}
		return packet;
	}
	
	/**
	 * This method provides the actual implementation for writing packets
	 */
	protected abstract void write(Packet packet) throws IOException;
	
	public void writePacket(final Packet packet) throws IOException {
		if (packet.isVerbose()) {
			fireMessageOutgoing(packet.toString());
		}
		write(packet);
	}
	
}