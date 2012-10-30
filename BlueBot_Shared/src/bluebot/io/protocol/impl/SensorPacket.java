package bluebot.io.protocol.impl;


import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import bluebot.io.protocol.Packet;
import bluebot.sensors.SensorType;



/**
 * 
 * @author Ruben Feyen
 */
public class SensorPacket extends Packet {
	
	private int value;
	private SensorType type;
	
	
	public SensorPacket(final DataInput input) throws IOException {
		super(input);
	}
	public SensorPacket(final SensorType type) {
		this(type, -1);
	}
	public SensorPacket(final SensorType type, final int value) {
		setSensorType(type);
		setSensorValue(value);
	}
	
	
	
	public int getOpcode() {
		return OP_SENSOR;
	}
	
	public SensorType getSensorType() {
		return type;
	}
	
	public int getSensorValue() {
		return value;
	}
	
	@Override
	public boolean isVerbose() {
		return false;
	}
	
	protected void readPayload(final DataInput input) throws IOException {
		setSensorType(SensorType.values()[input.readUnsignedByte()]);
		setSensorValue(input.readInt());
	}
	
	private final void setSensorType(final SensorType type) {
		this.type = type;
	}
	
	private final void setSensorValue(final int value) {
		this.value = value;
	}
	
	protected void writePayload(final DataOutput output) throws IOException {
		output.writeByte(getSensorType().ordinal() & 0xFF);
		output.writeInt(getSensorValue());
	}
	
}