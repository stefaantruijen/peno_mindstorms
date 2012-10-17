package bluebot.util;


import java.util.LinkedList;

import bluebot.io.protocol.Packet;



/**
 * 
 * @author Ruben Feyen
 */
public class PacketQueue {
	
	private LinkedList<Packet> queue = new LinkedList<Packet>();
	
	
	
	public void clear() {
		synchronized (queue) {
			queue.clear();
		}
	}
	
	public Packet pull() throws InterruptedException {
		synchronized (queue) {
			while (queue.isEmpty()) {
				queue.wait();
			}
			return queue.remove(0);
		}
	}
	
	public void push(final Packet packet) {
		synchronized (queue) {
			queue.add(packet);
			queue.notify();
		}
	}
	
}