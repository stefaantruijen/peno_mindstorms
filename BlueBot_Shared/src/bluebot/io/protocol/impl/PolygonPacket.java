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
	
	private int corners;
	private float length;
	
	
	public PolygonPacket(final DataInput input) throws IOException {
		super(input);
	}
	public PolygonPacket(final int corners, final float length) {
		setCorners(corners);
		setLength(length);
	}
	
	
	
	public int getCorners() {
		return corners;
	}
	
	public float getLength() {
		return length;
	}
	
	public int getOpcode() {
		return OP_POLYGON;
	}
	
	protected void readPayload(final DataInput input) throws IOException {
		
	}
	
	private final void setCorners(final int corners) {
		this.corners = corners;
	}
	
	private final void setLength(final float length) {
		this.length = length;
	}
	
	@Override
	public String toString() {
		return new StringBuilder("Polygon (")
		.append(getCorners()).append(" x ").append(getLength()).append(')')
		.toString();
	}
	
	protected void writePayload(final DataOutput output) throws IOException {
		output.writeInt(getCorners());
		output.writeFloat(getLength());
	}
	
}