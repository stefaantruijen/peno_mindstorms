package bluebot.io.protocol.impl;


import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import bluebot.io.protocol.Packet;



/**
 * 
 * @author Ruben Feyen
 */
public class MessagePacket extends Packet {
	
	private String msg;
	private String title;
	
	
	public MessagePacket(final DataInput input) throws IOException {
		super(input);
	}
	public MessagePacket(final String msg, final String title) {
		setMessage(msg);
		setTitle(title);
	}
	
	
	
	public String getMessage() {
		return msg;
	}
	
	public int getOpcode() {
		return OP_MESSAGE;
	}
	
	public String getTitle() {
		return title;
	}
	
	protected void readPayload(final DataInput input) throws IOException {
		setTitle(input.readUTF());
		setMessage(input.readUTF());
	}
	
	private final void setMessage(final String msg) {
		this.msg = msg;
	}
	
	private final void setTitle(final String title) {
		this.title = title;
	}
	
	protected void writePayload(final DataOutput output) throws IOException {
		output.writeUTF(getTitle());
		output.writeUTF(getMessage());
	}
	
}