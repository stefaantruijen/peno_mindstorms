package bluebot.io;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;



/**
 * 
 * @author Ruben Feyen
 */
public abstract class ClientLink extends Link {
	
	protected DataInputStream createInput() {
		return new DataInputStream(createInputRaw());
	}
	
	protected abstract InputStream createInputRaw();
	
	protected DataOutputStream createOutput() {
		return new DataOutputStream(createOutputRaw());
	}
	
	protected abstract OutputStream createOutputRaw();
	
}
