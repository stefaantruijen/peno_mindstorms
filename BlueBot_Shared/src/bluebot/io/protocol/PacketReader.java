package bluebot.io.protocol;


import static bluebot.io.protocol.Packet.*;

import java.io.DataInputStream;
import java.io.IOException;

import bluebot.io.protocol.impl.MovePacket;
import bluebot.io.protocol.impl.StopPacket;



/**
 * 
 * @author Ruben Feyen
 */
public class PacketReader {
	
	private DataInputStream input;
	
	
	public PacketReader(final DataInputStream input) {
		this.input = input;
	}
	
	
	
	private final byte readOpcode() throws IOException {
		return input.readByte();
	}
	
	public Packet readPacket() throws IOException {
		synchronized (input) {
			final byte opcode = readOpcode();
			switch (opcode) {
				case OP_MOVE:
					return new MovePacket(input);
				case OP_STOP:
					return StopPacket.SINGLETON;
				default:
					throw new ProtocolException("Invalid packet opcode:  " + opcode);
			}
		}
	}
	
}