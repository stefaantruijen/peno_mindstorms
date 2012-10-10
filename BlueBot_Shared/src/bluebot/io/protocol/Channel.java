package bluebot.io.protocol;


import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;



/**
 * 
 * @author Ruben Feyen
 */
public class Channel {
	
	private PacketReader reader;
	private PacketWriter writer;
	
	
	public Channel(final DataInput input, final DataOutput output) {
		this.reader = new PacketReader(input);
		this.writer = new PacketWriter(output);
	}
	public Channel(final InputStream input, final OutputStream output) {
		this((DataInput)new DataInputStream(input), (DataOutput)new DataOutputStream(output));
	}
	
	
	
	public Packet readPacket() throws IOException {
		synchronized (reader) {
			return reader.readPacket();
		}
	}
	
	public void writePacket(final Packet packet) throws IOException {
		synchronized (writer) {
			writer.writePacket(packet);
		}
	}
	
}