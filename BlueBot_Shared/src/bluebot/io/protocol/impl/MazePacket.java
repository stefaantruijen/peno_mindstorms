package bluebot.io.protocol.impl;


import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import bluebot.io.protocol.Packet;



/**
 * 
 * @author Ruben Feyen
 */
public class MazePacket extends Packet {
	
	private int id;
	
	
	public MazePacket(final DataInput input) throws IOException {
		super(input);
	}
	public MazePacket(final int playerId) {
		setPlayerId(playerId);
	}
	
	
	
	public int getOpcode() {
		return OP_MAZE;
	}
	
	public int getPlayerId() {
		return id;
	}
	
	protected void readPayload(final DataInput input) throws IOException {
		setPlayerId(input.readInt());
	}
	
	private final void setPlayerId(final int id) {
		this.id = id;
	}
	
	protected void writePayload(final DataOutput output) throws IOException {
		output.writeInt(getPlayerId());
	}
	
}