package bluebot.io;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;



/**
 * 
 * @author Ruben Feyen
 */
public abstract class Link {
	
	private DataInputStream input;
	private DataOutputStream output;
	
	
	
	public void close() throws IOException {
		final DataOutputStream output = this.output;
		if (output != null) {
			output.close();
			this.output = null;
		}
		
		final DataInputStream input = this.input;
		if (input != null) {
			input.close();
			this.input = null;
		}
	}
	
	protected abstract DataInputStream createInput();
	
	protected abstract DataOutputStream createOutput();
	
	public DataInputStream getInput() {
		if (input == null) {
			input = createInput();
		}
		return input;
	}
	
	public DataOutputStream getOutput() {
		if (output == null) {
			output = createOutput();
		}
		return output;
	}
	
}
