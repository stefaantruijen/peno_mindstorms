package bluebot.io.protocol.impl;


import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import bluebot.io.protocol.Packet;



/**
 * Implementation of the {@link Packet} class representing the STOP packet
 * 
 * @author Ruben Feyen
 */
public class StopPacket extends Packet {
	
	/**
	 * The singleton instance of the {@link StopPacket} class
	 */
	public static final StopPacket SINGLETON = new StopPacket();
	
	
	private StopPacket() {
		// hidden
	}
	
	
	
	public int getOpcode() {
		return OP_STOP;
	}
	
	protected void readPayload(final DataInput input) throws IOException {
		// ignored
	}
	
	@Override
	public String toString() {
		return "Stop";
	}
	
	protected void writePayload(final DataOutput output) throws IOException {
		// ignored
	}
	
}