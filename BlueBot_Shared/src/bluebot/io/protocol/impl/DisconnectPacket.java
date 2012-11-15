package bluebot.io.protocol.impl;


import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import bluebot.io.protocol.Packet;



/**
 * {@link Packet} implementation representing the DISCONNECT message
 * 
 * @author Ruben Feyen
 */
public class DisconnectPacket extends Packet {
	
	/**
	 * The singleton instance of the {@link DisconnectPacket} class
	 */
	public static final DisconnectPacket SINGLETON = new DisconnectPacket();
	
	
	private DisconnectPacket() {
		// hidden
	}
	
	
	
	public int getOpcode() {
		return OP_DISCONNECT;
	}
	
	protected void readPayload(final DataInput input) throws IOException {
		// No payload
	}
	
	protected void writePayload(final DataOutput output) throws IOException {
		// No payload
	}
	
}