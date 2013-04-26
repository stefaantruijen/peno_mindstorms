package bluebot.io.protocol.impl;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import bluebot.io.protocol.Packet;

public class StartLocationPacket extends Packet {
	
	private int x,y;
	private float heading;
	
	public StartLocationPacket(int x, int y, float heading){
		this.x = x;
		this.y = y;
		this.heading = heading;
	}

	@Override
	public int getOpcode() {
		return Packet.OP_SETSTARTLOCATION;
	}

	@Override
	protected void readPayload(DataInput input) throws IOException {
		this.x = input.readInt();
		this.y = input.readInt();
		this.heading = input.readFloat();
	}

	@Override
	protected void writePayload(DataOutput output) throws IOException {
		output.writeInt(this.x);
		output.writeInt(this.y);
		output.writeFloat(this.heading);
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public float getHeading() {
		return heading;
	}

	public void setHeading(float heading) {
		this.heading = heading;
	}

}
