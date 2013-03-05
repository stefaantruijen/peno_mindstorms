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
	private int[] ids;
	
	
	public MazePacket(final DataInput input) throws IOException {
		super(input);
	}
	public MazePacket(final int[] playerIds, final int playerId) {
		setPlayerId(playerId);
		setPlayerIds(playerIds);
	}
	
	
	
	public int getOpcode() {
		return OP_MAZE;
	}
	
	public int getPlayerId() {
		return id;
	}
	
	public int[] getPlayerIds() {
		return ids.clone();
	}
	
	protected void readPayload(final DataInput input) throws IOException {
		setPlayerId(input.readInt());
		
		final int[] ids = new int[input.readUnsignedShort()];
		for (int i = 0; i < ids.length; i++) {
			ids[i] = input.readInt();
		}
		setPlayerIds(ids);
	}
	
	private final void setPlayerId(final int id) {
		this.id = id;
	}
	
	private final void setPlayerIds(final int[] ids) {
		this.ids = ids.clone();
	}
	
	protected void writePayload(final DataOutput output) throws IOException {
		output.writeInt(getPlayerId());
		
		final int[] ids = getPlayerIds();
		output.writeShort(ids.length);
		for (final int id : ids) {
			output.writeInt(id);
		}
	}
	
}