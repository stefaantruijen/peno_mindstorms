package bluebot.io;


import java.io.IOException;

import bluebot.io.protocol.Packet;
import bluebot.io.protocol.PacketHandler;



/**
 * The {@link Communicator} is responsible for dispatching incoming {@link Packet}s
 * from a {@link Connection} to a {@link PacketHandler}
 * 
 * @author Ruben Feyen
 */
public class Communicator implements Runnable {
	
	private Connection connection;
	private PacketHandler handler;
	private Thread thread;
	
	
	public Communicator(final Connection connection, final PacketHandler handler) {
		this.connection = connection;
		this.handler = handler;
	}
	
	
	
	/**
	 * Executes the dispatching loop
	 * 
	 * @see {@link #start()}
	 * @see {@link #stop()}
	 */
	public final void run() {
		while (!Thread.interrupted()) {
			try {
				System.out.println("READ PACKET");
				handler.handlePacket(connection.readPacket());
			} catch (final IOException e) {
				e.printStackTrace();
				// TODO: Handle the error
			}
		}
	}
	
	/**
	 * Starts dispatching packets
	 */
	public synchronized void start() {
		if (thread == null) {
			thread = new Thread(this);
			thread.setDaemon(true);
			thread.start();
		}
	}
	
	/**
	 * Stops dispatching packets
	 */
	public synchronized void stop() {
		if (thread != null) {
			thread.interrupt();
			thread = null;
		}
	}
	
}