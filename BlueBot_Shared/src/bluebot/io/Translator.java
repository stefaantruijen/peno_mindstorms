package bluebot.io;


import java.io.IOException;

import bluebot.io.protocol.Packet;
import bluebot.io.protocol.PacketFactory;



/**
 * Represents an API-to-protocol translation service
 * 
 * @author Ruben Feyen
 */
public abstract class Translator {
	
	private Connection connection;
	
	
	public Translator(final Connection connection) {
		this.connection = connection;
	}
	
	
	
	/**
	 * Returns the connection
	 * 
	 * @return a {@link Connection} object
	 */
	private final Connection getConnection() {
		return connection;
	}
	
	/**
	 * Returns the {@link PacketFactory}
	 * 
	 * @return a {@link PacketFactory} object
	 */
	protected PacketFactory getPacketFactory() {
		return PacketFactory.getPacketFactory();
	}
	
	/**
	 * Sends a packet over the connection
	 * 
	 * @param packet - a {@link Packet} object
	 */
	protected void sendPacket(final Packet packet) {
		try {
			getConnection().writePacket(packet);
		} catch (final IOException e) {
			// TODO(?): Handle the exception
			e.printStackTrace();
		}
	}
	
}