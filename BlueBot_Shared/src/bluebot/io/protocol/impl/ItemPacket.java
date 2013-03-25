package bluebot.io.protocol.impl;


import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import bluebot.io.protocol.Packet;



/**
 * 
 * @author Ruben Feyen
 */
public class ItemPacket extends Packet {
	
	private int team;
	
	
	public ItemPacket(final DataInput input) throws IOException {
		super(input);
	}
	public ItemPacket(final int team) {
		setTeamId(team);
	}
	
	
	
	public int getOpcode() {
		return OP_ITEM;
	}
	
	public int getTeamId() {
		return team;
	}
	
	protected void readPayload(final DataInput input) throws IOException {
		setTeamId(input.readUnsignedByte());
	}
	
	private final void setTeamId(final int team) {
		this.team = team;
	}
	
	protected void writePayload(final DataOutput output) throws IOException {
		output.writeByte(getTeamId());
	}
	
}
