package bluebot.io.protocol.impl;


import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import bluebot.io.protocol.Packet;
import bluebot.util.Utils;



/**
 * 
 * @author Ruben Feyen
 */
public class MotionPacket extends Packet {
	
	private float heading, x, y;
	
	
	public MotionPacket(final DataInput input) throws IOException {
		super(input);
	}
	public MotionPacket(final float x, final float y, final float heading) {
		setX(x);
		setY(y);
		setHeading(heading);
	}
	
	
	
	public float getHeading() {
		return heading;
	}
	
	public int getOpcode() {
		return OP_MOTION;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	@Override
	public boolean isVerbose() {
		return false;
	}
	
	protected void readPayload(final DataInput input) throws IOException {
		setX(input.readFloat());
		setY(input.readFloat());
		setHeading(input.readFloat());
	}
	
	private final void setHeading(final float heading) {
		this.heading = Utils.clampAngleDegrees(heading);
	}
	
	private final void setX(final float x) {
		this.x = x;
	}
	
	private final void setY(final float y) {
		this.y = y;
	}
	
	protected void writePayload(final DataOutput output) throws IOException {
		output.writeFloat(getX());
		output.writeFloat(getY());
		output.writeFloat(getHeading());
	}
	
}