package bluebot.io;


import java.io.IOException;

import bluebot.io.protocol.Channel;
import bluebot.io.protocol.Packet;
import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;



/**
 * 
 * @author Ruben Feyen
 */
public class Connection {
	
	private Channel channel;
	private NXTComm nxtc;
	
	
	public Connection(final NXTComm nxtc) {
		this.channel = new Channel(nxtc.getInputStream(), nxtc.getOutputStream());
		this.nxtc = nxtc;
	}
	
	
	
	public synchronized void close() throws IOException {
		if (nxtc != null) {
			try {
				nxtc.close();
			} finally {
				channel = null;
				nxtc = null;
			}
		}
	}
	
	public static Connection create(final String name) throws NXTCommException {
		final NXTComm nxtc = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
		final NXTInfo[] devices = nxtc.search(name);
		if (devices.length != 1) {
			throw new NXTCommException("Unable to identify NXT brick by name:  " + name);
		}
		nxtc.open(devices[0]);
		return new Connection(nxtc);
	}
	
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