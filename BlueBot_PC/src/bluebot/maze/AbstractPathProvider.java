package bluebot.maze;


import java.util.LinkedList;

import bluebot.graph.Border;
import bluebot.graph.Orientation;
import bluebot.graph.Tile;



/**
 * 
 * @author Ruben Feyen
 */
public abstract class AbstractPathProvider {
	
	protected abstract Tile getCurrentTile();
	
	protected abstract Orientation getCurrentDirection();
	
	private final Tile getNeighbor(final Tile tile, final Orientation dir) {
		final int x = tile.getX();
		final int y = tile.getY();
		switch (dir) {
			case NORTH:
				if (tile.getBorderNorth() == Border.OPEN) {
					return getTile(x, (y + 1));
				}
				break;
				
			case EAST:
				if (tile.getBorderEast() == Border.OPEN) {
					return getTile((x + 1), y);
				}
				break;
				
			case SOUTH:
				if (tile.getBorderSouth() == Border.OPEN) {
					return getTile(x, (y - 1));
				}
				break;
				
			case WEST:
				if (tile.getBorderWest() == Border.OPEN) {
					return getTile((x - 1), y);
				}
				break;
				
			default:
				throw new RuntimeException("Invalid direction:  " + dir);
		}
		return null;
	}
	
	public Tile[] getPathToNextTile() {
		Tile tile = getCurrentTile();
		if (isCandidate(tile)) {
			return new Tile[] { tile };
		}
		
		final LinkedList<Node> nodes = new LinkedList<Node>();
		final LinkedList<Tile> tiles = new LinkedList<Tile>();
		tiles.add(tile);
		
		Orientation dir = getCurrentDirection();
		Node node = new Node(tile, dir);
		
		Orientation[] dirs = new Orientation[] {
				dir,
				dir.rotateCW(),
				dir.rotateCCW(),
				null
		};
		dirs[3] = dirs[1].rotateCW();
		for (final Orientation d : dirs) {
			final Tile t = getNeighbor(tile, d);
			if (t != null) {
				nodes.add(new Node(t, d, node));
				tiles.add(t);
			}
		}
		
		while (!nodes.isEmpty()) {
			node = nodes.remove(0);
			tile = node.tile;
			if (isCandidate(tile)) {
				return node.getPath();
			}
			
			dirs[0] = dir = node.dir;
			dirs[1] = dir.rotateCW();
			dirs[2] = dir.rotateCCW();
			for (final Orientation d : dirs) {
				final Tile t = getNeighbor(tile, d);
				if ((t != null) && !tiles.contains(t)) {
					nodes.add(new Node(t, d, node));
					tiles.add(t);
				}
			}
		}
		
		return null;
	}
	
	protected abstract Tile getTile(int x, int y);
	
	private final boolean isCandidate(final Tile tile) {
		if (!tile.isExplored()) {
			return true;
		}
		if (tile.canHaveBarcode()) {
			return (tile.getBarCode() < 0);
		}
		return false;
	}
	
	
	
	
	
	
	
	
	
	
	private static final class Node {
		
		private final Orientation dir;
		private final Node parent;
		private final Tile tile;
		
		
		private Node(final Tile tile, final Orientation dir) {
			this(tile, dir, null);
		}
		private Node(final Tile tile, final Orientation dir, final Node parent) {
			this.dir = dir;
			this.parent = parent;
			this.tile = tile;
		}
		
		
		
		public Tile[] getPath() {
			final int length = getPathLength();
			final Tile[] path = new Tile[length];
			
			Node node = this;
			for (int i = (length - 1); i >= 0; i--) {
				path[i] = node.tile;
				node = node.parent;
			}
			
			return path;
		}
		
		private final int getPathLength() {
			int length = 1;
			if (parent != null) {
				length += parent.getPathLength();
			}
			return length;
		}
		
	}
	
}