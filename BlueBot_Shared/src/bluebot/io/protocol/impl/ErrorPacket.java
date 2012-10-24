package bluebot.io.protocol.impl;


import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import bluebot.io.protocol.Packet;



/**
 * 
 * @author Ruben Feyen
 */
public class ErrorPacket extends Packet {
	
	private String msg;
	
	
	public ErrorPacket(final DataInput input) throws IOException {
		super(input);
	}
	public ErrorPacket(final String msg) {
		setMessage(msg);
	}
	
	
	
	public String getMessage() {
		return msg;
	}
	
	public int getOpcode() {
		return OP_ERROR;
	}
	
	protected void readPayload(final DataInput input) throws IOException {
		setMessage(input.readUTF());
	}
	
	private final void setMessage(final String msg) {
		this.msg = msg;
	}
	
	@Override
	public String toString() {
		return ("Error:  " + getMessage());
	}
	
	protected void writePayload(final DataOutput output) throws IOException {
		output.writeUTF(getMessage());
	}
	
}