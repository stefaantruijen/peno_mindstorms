package bluebot.io.protocol.impl;


import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import bluebot.io.protocol.Packet;



/**
 * 
 * @author Ruben Feyen
 */
public class ConfigPacket extends Packet {
	
	public static final int ID_SPEED = 0x01;
	
	private boolean fp;
	private int id;
	private Number value;
	
	
	public ConfigPacket(final DataInput input) throws IOException {
		super(input);
	}
	public ConfigPacket(final int id, final double value) {
		this(id, Double.valueOf(value), true);
	}
	public ConfigPacket(final int id, final long value) {
		this(id, Long.valueOf(value), false);
	}
	private ConfigPacket(final int id, final Number value, final boolean fp) {
		setFloatingPoint(fp);
		setId(id);
		setValue(value);
	}
	
	
	
	public int getId() {
		return id;
	}
	
	public int getOpcode() {
		return OP_CONFIG;
	}
	
	public Number getValue() {
		return value;
	}
	
	public boolean isFloatingPoint() {
		return fp;
	}
	
	protected void readPayload(final DataInput input) throws IOException {
		setId(input.readUnsignedByte());
		setFloatingPoint(input.readBoolean());
		setValue(readValue(input));
	}
	
	private final Number readValue(final DataInput input) throws IOException {
		if (isFloatingPoint()) {
			return Double.valueOf(input.readDouble());
		} else {
			return Long.valueOf(input.readLong());
		}
	}
	
	private final void setFloatingPoint(final boolean fp) {
		this.fp = fp;
	}
	
	private final void setId(final int id) {
		this.id = id;
	}
	
	private final void setValue(final Number value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("SET ");
		sb.append(toString(getId())).append(" = ");
		if (isFloatingPoint()) {
			sb.append(getValue().doubleValue());
		} else {
			sb.append(getValue().longValue());
		}
		return sb.toString();
	}
	
	protected String toString(final int id) {
		switch (id) {
			case ID_SPEED:
				return "speed";
			default:
				return "???";
		}
	}
	
	protected void writePayload(final DataOutput output) throws IOException {
		output.writeByte(getId());
		if (isFloatingPoint()) {
			output.writeBoolean(true);
			output.writeDouble(getValue().doubleValue());
		} else {
			output.writeBoolean(false);
			output.writeLong(getValue().longValue());
		}
	}
	
}