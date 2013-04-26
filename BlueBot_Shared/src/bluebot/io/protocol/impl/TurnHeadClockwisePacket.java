package bluebot.io.protocol.impl;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import bluebot.io.protocol.Packet;

public class TurnHeadClockwisePacket extends Packet {
	
	private int offset;
	
	public TurnHeadClockwisePacket(final DataInput input) throws IOException {
		super(input);
	}
	public TurnHeadClockwisePacket(int offset) {
		this.offset = offset;
	}
	public int getOffset() {
		return this.offset;
	}
	
	@Override
	public boolean isVerbose() {
		return true;
	}
	
	public int getOpcode() {
		return OP_TURNHEADCLOCKWISE;
	}
	
	protected void readPayload(final DataInput input) throws IOException {
		this.offset = input.readInt();
	}
	
	protected void writePayload(final DataOutput output) throws IOException {
		output.writeInt(offset);
	}
	
	@Override
	public String toString() {
		return "TurnHeadCounterClockwise";
	}
}
