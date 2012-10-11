package bluebot.io.protocol;


import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;



/**
 * 
 * @author Ruben Feyen
 */
public abstract class Packet {
	
	public static final byte OP_STOP    = 0x01;
	public static final byte OP_MOVE    = 0x02;
	
	
	
	public abstract byte getOpcode();
	
	
	protected Packet() {}
	protected Packet(final DataInput input) throws IOException {
		readPayload(input);
	}
	
	
	
	protected abstract void readPayload(DataInput input) throws IOException;
	
	@Override
	public String toString() {
		return new StringBuilder("Packet[")
		.append(getOpcode())
		.append(']')
		.toString();
	}
	
	public void write(final DataOutput output) throws IOException {
		writeOpcode(output);
		writePayload(output);
	}
	
	private final void writeOpcode(final DataOutput output) throws IOException {
		output.writeByte(getOpcode());
	}
	
	protected abstract void writePayload(DataOutput output) throws IOException;
	
}