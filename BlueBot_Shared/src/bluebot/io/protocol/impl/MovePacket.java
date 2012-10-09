package bluebot.io.protocol.impl;


import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import bluebot.io.protocol.Packet;



/**
 * 
 * @author Ruben Feyen
 */
public class MovePacket extends Packet {
	
	private byte direction;
	
	
	public MovePacket(final DataInput input) throws IOException {
		super(input);
	}
	public MovePacket(final byte direction) {
		setDirection(direction);
	}
	
	
	
	public byte getDirection() {
		return direction;
	}
	
	public byte getOpcode() {
		return OP_MOVE;
	}
	
	protected void readPayload(final DataInput input) throws IOException {
		setDirection(input.readByte());
	}
	
	private final void setDirection(final byte direction) {
		this.direction = direction;
	}
	
	protected void writePayload(final DataOutput output) throws IOException {
		output.writeByte(getDirection());
	}
	
}