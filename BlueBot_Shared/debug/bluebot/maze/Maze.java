package bluebot.maze;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



/**
 * 
 * @author Ruben Feyen
 */
public class Maze {
	
	private HashMap<Integer, Tile> map;
	
	
	public Maze() {
		map = new HashMap<Integer, Tile>();
	}
	
	
	
	public Tile addTile(final int x, final int y) {
		Tile tile = getTile(x, y);
		if (tile == null) {
			tile = new Tile(x, y);
			initBorders(tile);
			map.put(key(x, y), tile);
		}
		return tile;
	}
	
	public List<Tile> getNeighbors(final Tile tile) {
		final ArrayList<Tile> neighbors = new ArrayList<Tile>();
		
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
		return map.get(key(x, y));
	}
	
	public List<Tile> getTiles() {
		return new ArrayList<Tile>(map.values());
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
	
	private static final Integer key(final int x, final int y) {
		return Integer.valueOf(Tile.hashCode(x, y));
	}
	
}