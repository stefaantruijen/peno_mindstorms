package bluebot.io.protocol;


import java.io.DataOutputStream;
import java.io.IOException;



/**
 * The {@link PacketWriter} writes {@link Packet} objects to a {@link DataOutputStream}
 * 
 * @author Ruben Feyen
 */
public class PacketWriter {
	
	private DataOutputStream output;
	
	
	public PacketWriter(final DataOutputStream output) {
		this.output = output;
	}
	
	
	
	/**
	 * Writes a packet to the underlying stream
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