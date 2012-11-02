package bluebot.util;


import java.util.Iterator;
import java.util.LinkedList;



/**
 * Implementation of a Blocking FIFO queue
 * 
 * @author Ruben Feyen
 */
public class BlockingQueue<T> implements Iterable<T> {
	
	private LinkedList<T> queue = new LinkedList<T>();
	
	
	
	/**
	 * Clears the queue
	 */
	public void clear() {
		synchronized (queue) {
			queue.clear();
		}
	}
	
	public Iterator<T> iterator() {
		return queue.iterator();
	}
	
	/**
	 * Pulls the next element from the queue.
	 * This call blocks until an element is available.
	 * 
	 * @return the first element in the queue
	 * 
	 * @throws InterruptedException if interrupted while blocked
	 */
	public T pull() throws InterruptedException {
		synchronized (queue) {
			while (queue.isEmpty()) {
				queue.wait();
			}
			return queue.remove(0);
		}
	}
	
	/**
	 * Pushes an element onto the back of the queue
	 * 
	 * @param element - the element to be pushed
	 */
	public void push(final T element) {
		synchronized (queue) {
			queue.add(element);
			queue.notify();
		}
	}
	
}