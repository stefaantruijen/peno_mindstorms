package bluebot.io.protocol.impl;


import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import bluebot.io.protocol.Packet;



/**
 * {@link Packet} implementation for debug messages
 * 
 * @author Ruben Feyen
 */
public class DebugPacket extends Packet {
	
	private String msg;
	
	
	public DebugPacket(final DataInput input) throws IOException {
		super(input);
	}
	public DebugPacket(final String msg) {
		setMessage(msg);
	}
	
	
	
	public String getMessage() {
		return msg;
	}
	
	public int getOpcode() {
		return OP_DEBUG;
	}
	
	protected void readPayload(final DataInput input) throws IOException {
		setMessage(input.readUTF());
	}
	
	private final void setMessage(final String msg) {
		this.msg = msg;
	}
	
	protected void writePayload(final DataOutput output) throws IOException {
		output.writeUTF(getMessage());
	}
	
}