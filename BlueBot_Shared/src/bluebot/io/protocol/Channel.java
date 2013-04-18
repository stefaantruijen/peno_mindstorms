package bluebot.io.protocol;


import static bluebot.io.protocol.Packet.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import bluebot.io.protocol.impl.BarcodePacket;
import bluebot.io.protocol.impl.CommandPacket;
import bluebot.io.protocol.impl.ConfigPacket;
import bluebot.io.protocol.impl.DebugPacket;
import bluebot.io.protocol.impl.ErrorPacket;
import bluebot.io.protocol.impl.ItemPacket;
import bluebot.io.protocol.impl.MQMessagePacket;
import bluebot.io.protocol.impl.MessagePacket;
import bluebot.io.protocol.impl.MotionPacket;
import bluebot.io.protocol.impl.MovePacket;
import bluebot.io.protocol.impl.SeesawPacket;
import bluebot.io.protocol.impl.SensorPacket;
import bluebot.io.protocol.impl.StopPacket;



/**
 * Provides bi-directional transportation of {@link Packet} objects
 * 
 * @author Ruben Feyen
 */
public class Channel {
	
	private DataInputStream input;
	private DataOutputStream output;
	
	
	public Channel(final DataInputStream input, final DataOutputStream output) {
		this.input = input;
		this.output = output;
	}
	public Channel(final InputStream input, final OutputStream output) {
		this(new DataInputStream(input), new DataOutputStream(output));
	}
	
	
	
	/**
	 * Reads an incoming packet
	 * 
	 * @return a {@link Packet} object
	 * 
	 * @throws IOException if an I/O error occurs
	 */
	public Packet readPacket() throws IOException {
		synchronized (input) {
			final int opcode = input.readUnsignedByte();
			switch (opcode) {
				case OP_COMMAND:
					return new CommandPacket(input);
				case OP_CONFIG:
					return new ConfigPacket(input);
				case OP_DEBUG:
					return new DebugPacket(input);
				case OP_ERROR:
					return new ErrorPacket(input);
				case OP_ITEM:
					return new ItemPacket(input);
				case OP_MESSAGE:
					return new MessagePacket(input);
				case OP_MOTION:
					return new MotionPacket(input);
				case OP_MOVE:
					return new MovePacket(input);
				case OP_MQMESSAGE:
					return new MQMessagePacket(input);
				case OP_SEESAW:
					return new SeesawPacket(input);
				case OP_SENSOR:
					return new SensorPacket(input);
				case OP_STOP:
					return StopPacket.SINGLETON;
				case OP_BARCODE:
					return new BarcodePacket(input);
				default:
					throw new ProtocolException("Invalid packet opcode:  " + opcode);
			}
		}
	}
	
	/**
	 * Writes an outgoing packet
	 * 
	 * @param packet - a {@link Packet} object
	 * 
	 * @throws IOException if an I/O error occurs
	 */
	public void writePacket(final Packet packet) throws IOException {
		synchronized (output) {
			packet.write(output);
			output.flush();
		}
	}
	
}