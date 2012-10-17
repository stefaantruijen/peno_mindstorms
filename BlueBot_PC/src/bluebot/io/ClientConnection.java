package bluebot.io;


import java.io.IOException;

import bluebot.io.protocol.Channel;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;



/**
 * Represents a client-side {@link Connection}
 * 
 * @author Ruben Feyen
 */
public class ClientConnection extends Connection {
	
	private NXTComm nxtc;
	
	
	public ClientConnection(final NXTComm nxtc) {
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
	 * Factory method for the {@link ClientConnection} class
	 * 
	 * @param name - the name of the NXT brick to connect to
	 * 
	 * @return a {@link ClientConnection} object
	 * 
	 * @throws NXTCommException if the connection to the NXT brick fails for any reason
	 */
	public static ClientConnection create(final String name) throws NXTCommException {
		final NXTComm nxtc = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
		final NXTInfo[] devices = nxtc.search(name);
		if (devices.length != 1) {
			throw new NXTCommException("Unable to identify NXT brick by name:  " + name);
		}
		nxtc.open(devices[0]);
		return new ClientConnection(nxtc);
	}
	
	private static final Channel createChannel(final NXTComm nxtc) {
		return new Channel(nxtc.getInputStream(), nxtc.getOutputStream());
	}
	
}