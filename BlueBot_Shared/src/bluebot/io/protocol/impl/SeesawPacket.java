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
	public SeesawPacket(final int barcode, final boolean locked) {
		setBarcode(barcode);
		setLocked(locked);
	}
	
	
	
	public int getBarcode() {
		return barcode;
	}
	
	public int getOpcode() {
		return OP_SEESAW;
	}
	
	public boolean isLocked() {
		return locked;
	}
	
	protected void readPayload(final DataInput input) throws IOException {
		setBarcode(input.readUnsignedByte());
		setLocked(input.readBoolean());
	}
	
	private final void setBarcode(final int barcode) {
		this.barcode = barcode;
	}
	
	private final void setLocked(final boolean locked) {
		this.locked = locked;
	}
	
	protected void writePayload(final DataOutput output) throws IOException {
		output.writeByte(getBarcode());
		output.writeBoolean(isLocked());
	}
	
}
