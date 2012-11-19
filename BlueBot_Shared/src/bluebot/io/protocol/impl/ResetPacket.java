package bluebot.io.protocol.impl;


import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import bluebot.io.protocol.Packet;



/**
 * {@link Packet} implementation for the RESET message
 * 
 * @author Ruben Feyen
 */
public class ResetPacket extends Packet {
	
	public static final ResetPacket SINGLETON = new ResetPacket();
	
	
	private ResetPacket() {
		// hidden
	}
	
	
	
	public int getOpcode() {
		return OP_RESET;
	}
	
	protected void readPayload(final DataInput input) throws IOException {
		// ignored
	}
	
	protected void writePayload(final DataOutput output) throws IOException {
		// ignored
	}
	
}