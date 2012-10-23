package bluebot.io;


import java.io.IOException;

import bluebot.io.protocol.Packet;
import bluebot.util.EventDispatcher;



/**
 * Represents a connection between two devices
 * 
 * @author Ruben Feyen
 */
public interface Connection extends EventDispatcher<ConnectionListener> {
	
	/**
	 * Closes the connection
	 * 
	 * @throws IOException if an I/O error occurs
	 */
	public void close() throws IOException;
	
	/**
	 * Reads an incoming packet
	 * 
	 * @return a {@link Packet} object
	 * 
	 * @throws IOException if an I/O error occurs
	 */
	public Packet readPacket() throws IOException;
	
	/**
	 * Writes an outgoing packet
	 * 
	 * @param packet - a {@link Packet} object
	 * 
	 * @throws IOException if an I/O error occurs
	 */
	public void writePacket(Packet packet) throws IOException;
	
}