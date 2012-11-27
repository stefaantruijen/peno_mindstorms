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
	
	private float body, head, x, y;
	
	
	public MotionPacket(final DataInput input) throws IOException {
		super(input);
	}
	public MotionPacket(final float x, final float y,
			final float body, final float head) {
		setX(x);
		setY(y);
		setHeadingBody(body);
		setHeadingHead(head);
	}
	
	
	
	public float getHeadingBody() {
		return body;
	}
	
	public float getHeadingHead() {
		return head;
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
	
	protected void readPayload(final DataInput input) throws IOException {
		setX(input.readFloat());
		setY(input.readFloat());
		setHeadingBody(input.readFloat());
		setHeadingHead(input.readFloat());
	}
	
	private final void setHeadingBody(final float heading) {
		this.body = Utils.clampAngleDegrees(heading);
	}
	
	private final void setHeadingHead(final float heading) {
		this.head = Utils.clampAngleDegrees(heading);
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
		output.writeFloat(getHeadingBody());
		output.writeFloat(getHeadingHead());
	}
	
}