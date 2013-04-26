package bluebot.io.protocol.impl;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import bluebot.io.protocol.Packet;

public class ModifyOrientationPacket extends Packet {
	
	
	public ModifyOrientationPacket(final DataInput input) throws IOException {
		super(input);
	}
	public ModifyOrientationPacket() {
		
	}
	
	@Override
	public boolean isVerbose() {
		return true;
	}
	
	public int getOpcode() {
		return OP_MODIFYORIENTATION;
	}
	
	protected void readPayload(final DataInput input) throws IOException {
		//ignored
	}
	
	protected void writePayload(final DataOutput output) throws IOException {
		//ignored
	}
	
	@Override
	public String toString() {
		return "ModifyOrientation";
	}
}
