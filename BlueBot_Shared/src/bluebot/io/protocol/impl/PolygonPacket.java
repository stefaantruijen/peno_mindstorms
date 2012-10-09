package bluebot.io.protocol.impl;


import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import bluebot.io.protocol.Packet;



/**
 * 
 * @author Ruben Feyen
 */
public class PolygonPacket extends Packet {
	
	private int corners, length;
	
	
	public PolygonPacket(final DataInput input) throws IOException {
		super(input);
	}
	public PolygonPacket(final int corners, final int length) {
		setNumberOfCorners(corners);
		setLengthOfSide(length);
	}
	
	public int getLengthOfSide() {
		return length;
	}
	
	public int getNumberOfCorners() {
		return corners;
	}
	
	public byte getOpcode() {
		return OP_POLYGON;
	}
	
	protected void readPayload(final DataInput input) throws IOException {
		setNumberOfCorners(input.readUnsignedByte());
		setLengthOfSide(input.readInt());
	}
	
	private final void setLengthOfSide(final int length) {
		this.length = length;
	}
	
	private final void setNumberOfCorners(final int corners) {
		this.corners = corners;
	}
	
	protected void writePayload(final DataOutput output) throws IOException {
		output.writeByte(getNumberOfCorners());
		output.writeInt(getLengthOfSide());
	}
	
}