package bluebot.io.protocol;


import static bluebot.io.protocol.Packet.*;

import java.io.DataInput;
import java.io.EOFException;
import java.io.IOException;

import bluebot.io.protocol.impl.MovePacket;
import bluebot.io.protocol.impl.PolygonPacket;
import bluebot.io.protocol.impl.StopPacket;



/**
 * 
 * @author Ruben Feyen
 */
public class PacketReader {
	
	private DataInput input;
	
	
	public PacketReader(final DataInput input) {
		this.input = input;
	}
	
	
	
	private final byte readOpcode() throws IOException {
		return input.readByte();
	}
	
	public Packet readPacket() throws IOException {
		final byte opcode;
		try {
			opcode = readOpcode();
		} catch (final EOFException e) {
			// TODO: This could possibly happen when payloads are read before written
			//        Investigate to make sure there is no possible bug
			return null;
		}
		
		switch (opcode) {
			case OP_MOVE:
				return new MovePacket(input);
			case OP_POLYGON:
				return new PolygonPacket(input);
			case OP_STOP:
				return StopPacket.SINGLETON;
			default:
				throw new ProtocolException("Invalid packet opcode:  " + opcode);
		}
	}
	
}