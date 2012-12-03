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
	
	private int pathfinder;
	
	
	public MazePacket(final DataInput input) throws IOException {
		super(input);
	}
	public MazePacket(final int pathfinder) {
		setPathFinder(pathfinder);
	}
	
	
	
	public int getOpcode() {
		return OP_MAZE;
	}
	
	public int getPathFinder() {
		return pathfinder;
	}
	
	protected void readPayload(final DataInput input) throws IOException {
		setPathFinder(input.readInt());
	}
	
	private final void setPathFinder(final int pathfinder) {
		this.pathfinder = pathfinder;
	}
	
	protected void writePayload(final DataOutput output) throws IOException {
		output.writeInt(getPathFinder());
	}
	
}