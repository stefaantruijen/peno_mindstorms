package bluebot.io.protocol.impl;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import bluebot.io.protocol.Packet;

public class PickUpPacket extends Packet {
		
	public PickUpPacket(final DataInput input) throws IOException {
		super(input);
	}
	public PickUpPacket() {
		
	}
	
	@Override
	public boolean isVerbose() {
		return true;
	}
	
	public int getOpcode() {
		return OP_PICKUP;
	}
	
	protected void readPayload(final DataInput input) throws IOException {
		//ignored
	}
	
	protected void writePayload(final DataOutput output) throws IOException {
		//ignored
	}
	
	@Override
	public String toString() {
		return "PickUp";
	}
}
