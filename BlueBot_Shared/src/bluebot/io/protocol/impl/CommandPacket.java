package bluebot.io.protocol.impl;


import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import bluebot.io.protocol.Packet;



/**
 * 
 * @author Ruben Feyen
 */
public class CommandPacket extends Packet {
	
	public static final String CALIBRATE = "Calibrate";
	public static final String MAZE = "Maze";
	public static final String TILE = "ScanTile";
	public static final String WHITE_LINE_ORIENTATION = "WhiteLine";
	
	private String command;
	
	
	
	public CommandPacket(final DataInput input) throws IOException {
		super(input);
	}
	public CommandPacket(final String command) {
		setCommand(command);
	}
	
	
	
	public String getCommand() {
		return command;
	}
	
	public int getOpcode() {
		return OP_COMMAND;
	}
	
	@Override
	public boolean isVerbose() {
		return true;
	}
	
	protected void readPayload(final DataInput input) throws IOException {
		setCommand(input.readUTF());
	}
	
	private final void setCommand(final String command) {
		this.command = command;
	}
	
	@Override
	public String toString() {
		return ("Command:  " + getCommand());
	}
	
	protected void writePayload(final DataOutput output) throws IOException {
		output.writeUTF(getCommand());
	}
	
}