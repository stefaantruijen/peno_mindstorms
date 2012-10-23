package bluebot.io;


import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

import bluebot.io.Connection;
import bluebot.io.protocol.Packet;



/**
 * Represents a virtual {@link Connection}
 * 
 * @author Ruben Feyen
 */
public class VirtualConnection {
	
	private LinkedBlockingQueue<Packet> client2server;
	private LinkedBlockingQueue<Packet> server2client;
	
	
	public VirtualConnection() {
		client2server = new LinkedBlockingQueue<Packet>();
		server2client = new LinkedBlockingQueue<Packet>();
	}
	
	
	
	/**
	 * Creates the client-side connection
	 * 
	 * @return a {@link Connection} object
	 */
	public Connection createClient() {
		final LocalConnection connection = new LocalConnection();
		connection.input = server2client;
		connection.output = client2server;
		return connection;
	}
	
	/**
	 * Creates the server-side connection
	 * 
	 * @return a {@link Connection} object
	 */
	public Connection createServer() {
		final LocalConnection connection = new LocalConnection();
		connection.input = client2server;
		connection.output = server2client;
		return connection;
	}
	
	
	
	
	
	
	
	
	
	
	/**
	 * this class provides the actual implementation
	 * of the local connection for both client and server
	 * 
	 * @author Ruben Feyen
	 */
	private final class LocalConnection extends AbstractConnection {
		
		private LinkedBlockingQueue<Packet> input;
		private LinkedBlockingQueue<Packet> output;
		
		
		
		public void close() {
			// ignored
		}

		@Override
		protected Packet read() throws IOException {
			try {
				return input.take();
			} catch (final InterruptedException e) {
				throw new IOException(e);
			}
		}

		@Override
		protected void write(final Packet packet) {
			output.offer(packet);
		}
		
	}
	
}