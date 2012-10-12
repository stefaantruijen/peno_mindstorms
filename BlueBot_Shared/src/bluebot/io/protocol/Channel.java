package bluebot.io.protocol;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;



/**
 * Provides bi-directional transportation of {@link Packet} objects
 * 
 * @author Ruben Feyen
 */
public class Channel {
	
	private PacketReader reader;
	private PacketWriter writer;
	
	
	public Channel(final DataInputStream input, final DataOutputStream output) {
		this.reader = new PacketReader(input);
		this.writer = new PacketWriter(output);
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
		return reader.readPacket();
	}
	
	/**
	 * Writes an outgoing packet
	 * 
	 * @param packet - a {@link Packet} object
	 * 
	 * @throws IOException if an I/O error occurs
	 */
	public void writePacket(final Packet packet) throws IOException {
		writer.writePacket(packet);
	}
	
}