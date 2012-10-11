package bluebot.io.protocol.impl;


import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import bluebot.io.protocol.Packet;



/**
 * 
 * @author Ruben Feyen
 */
public class StopPacket extends Packet {
	
	public static final StopPacket SINGLETON = new StopPacket();
	
	
	private StopPacket() {
		// hidden
	}
	
	
	
	public byte getOpcode() {
		return OP_STOP;
	}
	
	protected void readPayload(final DataInput input) throws IOException {
		// ignored
	}
	
	protected void writePayload(final DataOutput output) throws IOException {
		// ignored
	}
	
}