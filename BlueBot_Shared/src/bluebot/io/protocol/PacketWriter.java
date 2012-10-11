package bluebot.io.protocol;


import java.io.DataOutputStream;
import java.io.IOException;



/**
 * 
 * @author Ruben Feyen
 */
public class PacketWriter {
	
	private DataOutputStream output;
	
	
	public PacketWriter(final DataOutputStream output) {
		this.output = output;
	}
	
	
	
	public void writePacket(final Packet packet) throws IOException {
		synchronized (output) {
			packet.write(output);
			output.flush();
		}
	}
	
}