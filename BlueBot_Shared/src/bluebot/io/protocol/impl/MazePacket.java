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
	
	private int itemNumber, playerNumber;
	
	
	public MazePacket(final DataInput input) throws IOException {
		super(input);
	}
	public MazePacket(final int playerNumber, final int itemNumber) {
		setPlayerNumber(playerNumber);
		setItemNumber(itemNumber);
	}
	
	
	
	public int getItemNumber() {
		return itemNumber;
	}
	
	public int getOpcode() {
		return OP_MAZE;
	}
	
	public int getPlayerNumber() {
		return playerNumber;
	}
	
	protected void readPayload(final DataInput input) throws IOException {
		setPlayerNumber(input.readUnsignedByte());
		setItemNumber(input.readUnsignedByte());
	}
	
	private final void setItemNumber(final int itemNumber) {
		this.itemNumber = itemNumber;
	}
	
	private final void setPlayerNumber(final int playerNumber) {
		this.playerNumber = playerNumber;
	}
	
	protected void writePayload(final DataOutput output) throws IOException {
		output.writeByte(getPlayerNumber());
		output.writeByte(getItemNumber());
	}
	
}