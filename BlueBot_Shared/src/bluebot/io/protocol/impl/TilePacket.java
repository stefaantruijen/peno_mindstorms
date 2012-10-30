package bluebot.io.protocol.impl;


import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import bluebot.graph.Tile;
import bluebot.io.protocol.Packet;



/**
 * 
 * @author Ruben Feyen
 */
public class TilePacket extends Packet {
	
	private Tile tile;
	
	
	public TilePacket(final DataInput input) throws IOException {
		super(input);
	}
	public TilePacket(final Tile tile) {
		setTile(tile);
	}
	
	
	
	public int getOpcode() {
		return OP_TILE;
	}
	
	public Tile getTile() {
		return tile;
	}
	
	@Override
	public boolean isVerbose() {
		return false;
	}
	
	protected void readPayload(final DataInput input) throws IOException {
		setTile(Tile.read(input));
	}
	
	private final void setTile(final Tile tile) {
		this.tile = tile;
	}
	
	protected void writePayload(final DataOutput output) throws IOException {
		getTile().write(output);
	}
	
}