package bluebot.io;


import java.io.IOException;

import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTConnection;
import bluebot.io.protocol.Channel;



/**
 * Represents a server-side {@link Connection}
 * 
 * @author Ruben Feyen
 */
public class ServerConnection extends RemoteConnection {
	
	private NXTConnection nxtc;
	
	
	private ServerConnection(final NXTConnection nxtc) {
		super(createChannel(nxtc));
		this.nxtc = nxtc;
	}
	
	
	
	public synchronized void close() throws IOException {
		if (nxtc != null) {
			try {
				nxtc.close();
			} finally {
				nxtc = null;
			}
		}
	}
	
	/**
	 * Factory method for the {@link ServerConnection} class
	 * 
	 * @return a {@link ServerConnection} object
	 */
	public static ServerConnection create() {
		return new ServerConnection(Bluetooth.waitForConnection());
	}
	
	private static final Channel createChannel(final NXTConnection nxtc) {
		return new Channel(nxtc.openInputStream(), nxtc.openOutputStream());
	}
	
}