package bluebot.io.protocol.impl;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import bluebot.io.protocol.Packet;

public class MQMessagePacket extends Packet {
	
	private String msg;
	
	/**
	 * Creates a new MQMessagePacket with the given input as message.
	 * @param input		The message
	 * @throws IOException
	 */
	public MQMessagePacket(final DataInput input) throws IOException {
		super(input);
	}
	
	/**
	 * Creates a new MQMessagePacket with the given message.
	 * @param msg	The message
	 */
	public MQMessagePacket(final String msg) {
		setMessage(msg);
	}
	
	public String getMessage() {
		return msg;
	}
	
	public int getOpcode() {
		return OP_MQMESSAGE;
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
