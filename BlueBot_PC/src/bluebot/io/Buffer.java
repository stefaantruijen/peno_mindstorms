package bluebot.io;


import java.io.IOException;
import java.nio.BufferOverflowException;



/**
 * 
 * @author Ruben Feyen
 */
public class Buffer {
	
	private final Object lock = new Object();
	
	private byte[] buffer;
	private int head, tail;
	
	
	public Buffer(final int capacity) {
		this.buffer = new byte[capacity];
	}
	
	
	
	private final boolean isEmpty() {
		return (head == tail);
	}
	
	private final boolean isFull() {
		int limit = (head - 1);
		if (limit < 0) {
			limit += buffer.length;
		}
		return (tail == limit);
	}
	
	public int read() throws IOException {
		synchronized (lock) {
			if (isEmpty()) {
				try {
					lock.wait();
				} catch (final InterruptedException e) {
					throw new IOException(e);
				}
				return read();
			}
			
			try {
				return (buffer[head] & 0xFF);
			} finally {
				if (++head >= buffer.length) {
					head = 0;
				}
			}
		}
	}
	
	public void write(final int b) throws IOException {
		synchronized (lock) {
			if (isFull()) {
				throw new BufferOverflowException();
			}
			
			buffer[tail] = (byte)b;
			if (++tail >= buffer.length) {
				tail = 0;
			}
			
			lock.notify();
		}
	}
	
}
