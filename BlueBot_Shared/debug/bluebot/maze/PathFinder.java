package bluebot.maze;


import java.util.ArrayList;
import java.util.BitSet;
import java.util.LinkedList;
import java.util.List;



/**
 * 
 * @author Ruben Feyen
 */
public class PathFinder {
	
	private TileSet closedSet;
	private Maze maze;
	private Node openList = new Node();
	private TileMap openMap;
	
	
	public PathFinder(final Maze maze) {
		final TileIndexer indexer = new TileIndexer(maze);
		
		this.closedSet = new TileSet(indexer);
		this.maze = maze;
		this.openMap = new TileMap(indexer);
	}
	
	
	
	private static final List<Tile> createPath(Node node) {
		final LinkedList<Tile> path = new LinkedList<Tile>();
		for (; node != null; node = node.back) {
//			path.addFirst(node.tile);
			path.add(node.tile);
		}
		return path;
	}
	
	private static final List<Tile> createPath(final Tile tile) {
		final ArrayList<Tile> path = new ArrayList<Tile>(1);
		path.add(tile);
		return path;
	}
	
	public List<Tile> findPath(final Tile start, final Tile goal) {
		if (start.equals(goal)) {
			return createPath(goal);
		}
		
		Node node = new Node(start);
		
		node.score_f = heuristic(start, goal);
		node.score_g = 0;
		openList.appendSorted(node);
		
		for (Node current; openList.next != null;) {
			current = openList.removeNext();
			
			final Tile tile = current.tile;
			if (tile.equals(goal)) {
				return createPath(current);
			}
			
			closedSet.add(tile);
			for (final Tile neighbor : getNeighbors(tile)) {
				if (!closedSet.contains(neighbor)) {
					final int tentative = (current.score_g + 1);
					node = openMap.get(neighbor);
					if (node == null) {
						node = new Node(neighbor);
						node.back = current;
						node.score_f = (tentative + heuristic(neighbor, goal));
						node.score_g = tentative;
						openList.appendSorted(node);
						openMap.set(neighbor, node);
					} else if (tentative <= node.score_g) {
						node.back = current;
						node.score_f = (tentative + heuristic(neighbor, goal));
						node.score_g = tentative;
					}
				}
			}
		}
		
		throw new RuntimeException("No path could be found");
	}
	
	private final List<Tile> getNeighbors(final Tile tile) {
		return maze.getNeighbors(tile);
	}
	
	private static final int heuristic(final Tile a, final Tile b) {
		return (Math.abs(b.getX() - a.getX())
				+ Math.abs(b.getY() - a.getY()));
	}
	
	
	
	
	
	
	
	
	
	
	private static final class TileMap {
		
		private TileIndexer indexer;
		private Node[] values;
		
		
		private TileMap(final TileIndexer indexer) {
			this.indexer = indexer;
			this.values = new Node[indexer.getMaximum() + 1];
		}
		
		
		
		public Node get(final Tile key) {
			return values[indexer.getIndex(key)];
		}
		
		public void set(final Tile key, final Node value) {
			values[indexer.getIndex(key)] = value;
		}
		
	}
	
	
	
	
	
	private static final class TileSet {
		
		private BitSet flags;
		private TileIndexer indexer;
		
		
		private TileSet(final TileIndexer indexer) {
			this.flags = new BitSet(indexer.getMaximum() + 1);
			this.indexer = indexer;
		}
		
		
		
		public void add(final Tile tile) {
			flags.set(index(tile));
		}
		
		public boolean contains(final Tile tile) {
			return flags.get(index(tile));
		}
		
		private final int index(final Tile tile) {
			return indexer.getIndex(tile);
		}
		
		@SuppressWarnings("unused")
		public void remove(final Tile tile) {
			flags.clear(index(tile));
		}
		
	}
	
}