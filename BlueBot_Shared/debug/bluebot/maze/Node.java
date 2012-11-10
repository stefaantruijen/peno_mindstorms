package bluebot.maze;


import bluebot.graph.Tile;



/**
 * 
 * @author Ruben Feyen
 */
public class Node {
	
	public Node back;
	public Node next;
	public int score_f;
	public int score_g;
	public Tile tile;
	
	
	public Node() {}
	public Node(final Tile tile) {
		this();
		this.tile = tile;
	}
	
	
	
	private final void append(final Node node) {
		if (next != null) {
			node.next = next;
		}
		next = node;
	}
	
	public void appendSorted(final Node node) {
		final int score = node.score_f;
		
		Node prev = this;
		while ((prev.next != null) && (prev.next.score_f < score)) {
			prev = prev.next;
		}
		
		prev.append(node);
	}
	
	public Node removeNext() {
		if (next == null) {
			return null;
		}
		
		final Node node = next;
		next = node.next;
		return node;
	}
	
}