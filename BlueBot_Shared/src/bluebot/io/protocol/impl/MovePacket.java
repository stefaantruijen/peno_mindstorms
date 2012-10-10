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
	private float quantity;
	
	
	public MovePacket(final DataInput input) throws IOException {
		super(input);
	}
	public MovePacket(final byte direction, final float quantity) {
		setDirection(direction);
		setQuantity(quantity);
	}
	
	
	
	public byte getDirection() {
		return direction;
	}
	
	public float getQuantity() {
		return quantity;
	}
	
	public byte getOpcode() {
		return OP_MOVE;
	}
	
	protected void readPayload(final DataInput input) throws IOException {
		setDirection(input.readByte());
		setQuantity(input.readFloat());
	}
	
	private final void setDirection(final byte direction) {
		this.direction = direction;
	}
	
	private final void setQuantity(final float quantity) {
		this.quantity = quantity;
	}
	
	protected void writePayload(final DataOutput output) throws IOException {
		output.writeByte(getDirection());
		output.writeFloat(getQuantity());
	}
	
}