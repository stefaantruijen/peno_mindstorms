package bluebot.io.protocol.impl;


import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import bluebot.io.protocol.Packet;



/**
 * 
 * @author Ruben Feyen
 */
public class SeesawPacket extends Packet {
	
	private int barcode;
	private boolean locked;
	
	
	public SeesawPacket(final DataInput input) throws IOException {
		super(input);
	}
	public SeesawPacket() {
		
	}
	
	@Override
	public boolean isVerbose() {
		return true;
	}
	
	public int getOpcode() {
		return OP_SEESAW;
	}
	
	protected void readPayload(final DataInput input) throws IOException {
		//ignored
	}
	
	protected void writePayload(final DataOutput output) throws IOException {
		//ignored
	}
	
	@Override
	public String toString() {
		return "SeeSaw";
	}
}
