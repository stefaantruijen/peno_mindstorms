package bluebot.util;


import java.util.LinkedList;

import bluebot.io.protocol.Packet;



/**
 * Blocking FIFO queue implementation for {@link Packet} objects
 * 
 * @author Ruben Feyen
 */
public class PacketQueue {
	
	private LinkedList<Packet> queue = new LinkedList<Packet>();
	
	
	
	/**
	 * Clears the queue
	 */
	public void clear() {
		synchronized (queue) {
			queue.clear();
		}
	}
	
	/**
	 * Pulls the next packet from the queue.
	 * This call blocks until a packet is available.
	 * 
	 * @return a {@link Packet} object
	 * 
	 * @throws InterruptedException if interrupted while blocked
	 */
	public Packet pull() throws InterruptedException {
		synchronized (queue) {
			while (queue.isEmpty()) {
				queue.wait();
			}
			return queue.remove(0);
		}
	}
	
	/**
	 * Pushes a packet onto the back of the queue
	 * 
	 * @param packet - a {@link Packet} object
	 */
	public void push(final Packet packet) {
		synchronized (queue) {
			queue.add(packet);
			queue.notify();
		}
	}
	
}