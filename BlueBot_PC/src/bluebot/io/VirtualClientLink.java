package bluebot.io;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;



/**
 * 
 * @author Ruben Feyen
 */
public class VirtualClientLink extends ClientLink {
	
	private Buffer input, output;
	
	
	public VirtualClientLink(final Buffer input,
			final Buffer output) {
		this.input = input;
		this.output = output;
	}
	
	
	
	protected InputStream createInputRaw() {
		return new BufferInputStream(input);
	}
	
	protected OutputStream createOutputRaw() {
		return new BufferOutputStream(output);
	}
	
	
	
	
	
	
	
	
	
	
	private static final class BufferInputStream extends InputStream {
		
		private Buffer buffer;
		
		
		public BufferInputStream(final Buffer buffer) {
			this.buffer = buffer;
		}
		
		
		
		public int read() throws IOException {
			return buffer.read();
		}
		
	}
	
	
	
	
	
	private static final class BufferOutputStream extends OutputStream {
		
		private Buffer buffer;
		
		
		public BufferOutputStream(final Buffer buffer) {
			this.buffer = buffer;
		}
		
		
		
		public void write(final int b) throws IOException {
			buffer.write(b);
		}
		
	}
	
}
