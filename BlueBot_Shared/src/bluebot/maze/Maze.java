package bluebot.maze;


import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import bluebot.graph.Border;
import bluebot.graph.Tile;



/**
 * Represents a maze
 * 
 * @author Ruben Feyen
 */
public class Maze {
	
//	private HashMap<Integer, Tile> map;
	private Hashtable<Integer, Tile> table;
	
	
	public Maze() {
//		map = new HashMap<Integer, Tile>();
		table = new Hashtable<Integer, Tile>();
	}
	
	
	
	public Tile addTile(final int x, final int y) {
		Tile tile = getTile(x, y);
		if (tile == null) {
			tile = new Tile(x, y);
			initBorders(tile);
			table.put(key(x, y), tile);
		}
		return tile;
	}
	
	public List<Tile> getNeighbors(final Tile tile) {
		final ArrayList<Tile> neighbors = new ArrayList<Tile>(4);
		
		final int x = tile.getX();
		final int y = tile.getY();
		
		Tile neighbor;
		
		// North
		if (tile.getBorderNorth() == Border.OPEN) {
			neighbor = getTile(x, y + 1);
			if (neighbor != null) {
				neighbors.add(neighbor);
			}
		}
		
		// East
		if (tile.getBorderEast() == Border.OPEN) {
			neighbor = getTile(x + 1, y);
			if (neighbor != null) {
				neighbors.add(neighbor);
			}
		}
		
		// South
		if (tile.getBorderSouth() == Border.OPEN) {
			neighbor = getTile(x, y - 1);
			if (neighbor != null) {
				neighbors.add(neighbor);
			}
		}
		
		// West
		if (tile.getBorderWest() == Border.OPEN) {
			neighbor = getTile(x - 1, y);
			if (neighbor != null) {
				neighbors.add(neighbor);
			}
		}
		
		return neighbors;
	}
	
	public Tile getTile(final int x, final int y) {
		return table.get(key(x, y));
	}
	
	public List<Tile> getTiles() {
		final ArrayList<Tile> tiles = new ArrayList<Tile>();
		
		final Enumeration<Integer> keys = table.keys();
		while (keys.hasMoreElements()) {
			tiles.add(table.get(keys.nextElement()));
		}
		
		return tiles;
	}
	
	private final void initBorders(final Tile tile) {
		final int x = tile.getX();
		final int y = tile.getY();
		
		Tile neighbor;
		
		// North
		neighbor = getTile(x, y + 1);
		if (neighbor != null) {
			tile.setBorderNorth(neighbor.getBorderSouth());
		}
		
		// East
		neighbor = getTile(x + 1, y);
		if (neighbor != null) {
			tile.setBorderEast(neighbor.getBorderWest());
		}
		
		// South
		neighbor = getTile(x, y - 1);
		if (neighbor != null) {
			tile.setBorderSouth(neighbor.getBorderNorth());
		}
		
		// West
		neighbor = getTile(x - 1, y);
		if (neighbor != null) {
			tile.setBorderWest(neighbor.getBorderEast());
		}
	}
	
	/*
	public boolean isExplored() {
		for (final Tile tile : getTiles()) {
			if (!tile.isExplored()) {
				return false;
			}
		}
		return true;
	}
	*/
	
	private static final Integer key(final int x, final int y) {
		return Integer.valueOf(Tile.hashCode(x, y));
	}
	
}