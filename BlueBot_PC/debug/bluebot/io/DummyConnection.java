package bluebot.io;


import java.io.IOException;

import bluebot.io.protocol.Packet;



/**
 * 
 * @author Ruben Feyen
 */
public class DummyConnection extends Connection {
	
	public DummyConnection() {
		super(null);
	}
	
	
	
	public void close() throws IOException {
		// ignored
	}
	
	@Override
	public Packet readPacket() {
		for (;;) {
			try {
				Thread.sleep(100);
			} catch (final InterruptedException e) {
				// ignored
			}
		}
	}
	
	@Override
	public void writePacket(final Packet packet) {
		System.out.println("WRITE:  " + packet);
	}
	
}