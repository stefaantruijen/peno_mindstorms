package bluebot.io.protocol;


import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;



/**
 * Represents a data packet of the protocol
 * 
 * @author Ruben Feyen
 */
public abstract class Packet {
	
	public static final int OP_DISCONNECT 				= 0x00;
	public static final int OP_STOP       				= 0x01;
	public static final int OP_MOVE       				= 0x02;
	public static final int OP_RESET     				= 0x03;
	public static final int OP_COMMAND    				= 0x10;
	public static final int OP_POLYGON    				= 0x11;
	public static final int OP_MAZE       				= 0x12;
	public static final int OP_MOTION    				= 0x20;
	public static final int OP_SENSOR     				= 0x30;
	public static final int OP_CONFIG     				= 0x40;
	public static final int OP_TILE       				= 0x50;
	public static final int OP_SEESAW     				= 0x61;
	public static final int OP_ITEM       				= 0x62;
	public static final int OP_ERROR      				= 0xE0;
	public static final int OP_MESSAGE    				= 0xE1;
	public static final int OP_DEBUG      				= 0xE2;
	public static final int OP_MQMESSAGE  				= 0xE3;
	public static final int OP_BARCODE	  				= 0xE4;
	public static final int OP_READBARCODE				= 0xE5;
	public static final int OP_PICKUP	  				= 0xE6;
	public static final int OP_TURNHEADCLOCKWISE 		= 0xE7;
	public static final int OP_TURNHEADCOUNTERCLOCKWISE = 0xE8;
	public static final int OP_MODIFYORIENTATION		= 0xE9;
	public static final int OP_SETSTARTLOCATION			= 0xF0;
	
	
	protected Packet() {}
	protected Packet(final DataInput input) throws IOException {
		readPayload(input);
	}
	
	
	
	/**
	 * Returns the opcode of the packet
	 * 
	 * @return an <code>int</code> value ranging from 0 to 255 (inclusive)
	 */
	public abstract int getOpcode();
	
	/**
	 * Determines whether or not this packet should be announced
	 * 
	 * @return <code>TRUE</code> if announcement is desired,
	 * 			<code>FALSE</code> otherwise
	 */
	public boolean isVerbose() {
		return false;
	}
	
	/**
	 * Reads the payload from the input
	 * 
	 * @param input - a {@link DataInput}
	 * 
	 * @throws IOException if an I/O error occurs
	 */
	protected abstract void readPayload(DataInput input) throws IOException;
	
	@Override
	public String toString() {
		return new StringBuilder("Packet[")
		.append(getOpcode())
		.append(']')
		.toString();
	}
	
	/**
	 * Writes the packet to the output
	 * 
	 * @param output - a {@link DataOutput}
	 * 
	 * @throws IOException if an I/O error occurs
	 */
	public void write(final DataOutput output) throws IOException {
		writeOpcode(output);
		writePayload(output);
	}
	
	/**
	 * Writes the opcode of the packet to the output
	 * 
	 * @param output - a {@link DataOutput}
	 * 
	 * @throws IOException if an I/O error occurs
	 */
	private final void writeOpcode(final DataOutput output) throws IOException {
		output.writeByte(getOpcode());
	}
	
	/**
	 * Writes the payload of the packet to the output
	 * 
	 * @param output - a {@link DataOutput}
	 * 
	 * @throws IOException if an I/O error occurs
	 */
	protected abstract void writePayload(DataOutput output) throws IOException;
	
}