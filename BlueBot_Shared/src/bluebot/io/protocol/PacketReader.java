package bluebot.io.protocol;


import static bluebot.io.protocol.Packet.*;

import java.io.DataInputStream;
import java.io.IOException;

import bluebot.io.protocol.impl.MovePacket;
import bluebot.io.protocol.impl.StopPacket;



/**
 * The {@link PacketReader} reads {@link Packet} objects from a {@link DataInputStream}
 * 
 * @author Ruben Feyen
 */
public class PacketReader {
	
	private DataInputStream input;
	
	
	public PacketReader(final DataInputStream input) {
		this.input = input;
	}
	
	
	
	/**
	 * Reads the opcode of a packet from the underlying stream
	 * 
	 * @return an <code>int</code> ranging from 0 to 255 (inclusive)
	 * 
	 * @throws IOException if an I/O error occurs
	 */
	private final int readOpcode() throws IOException {
		return input.readUnsignedByte();
	}
	
	/**
	 * Reads a packet from the underlying stream
	 * 
	 * @return a {@link Packet} object
	 * 
	 * @throws IOException if an I/O error occurs
	 */
	public Packet readPacket() throws IOException {
		synchronized (input) {
			final int opcode = readOpcode();
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