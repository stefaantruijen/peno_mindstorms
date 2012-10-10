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
		System.out.println("Writing packet # " + packet.getOpcode());
		packet.write(output);
		output.flush();
	}
	
}