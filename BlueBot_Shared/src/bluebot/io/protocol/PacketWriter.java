package bluebot.io.protocol;


import java.io.DataOutput;
import java.io.IOException;



/**
 * 
 * @author Ruben Feyen
 */
public class PacketWriter {
	
	private DataOutput output;
	
	
	public PacketWriter(final DataOutput output) {
		this.output = output;
	}
	
	
	
	public void writePacket(final Packet packet) throws IOException {
		packet.write(output);
	}
	
}