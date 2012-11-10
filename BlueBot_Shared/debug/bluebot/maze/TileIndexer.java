package bluebot.maze;


import bluebot.graph.Tile;



/**
 * 
 * @author Ruben Feyen
 */
public class TileIndexer {
	
	private int height;
	private int offsetX;
	private int offsetY;
	private int width;
	
	
	public TileIndexer(final Maze maze) {
		int maxX, maxY, minX, minY;
		
		maxX = maxY = 0;
		minX = minY = 0;
		
		int x, y;
		for (final Tile tile : maze.getTiles()) {
			x = tile.getX();
			y = tile.getY();
			
			if (x < minX) {
				minX = x;
			}
			if (x > maxX) {
				maxX = x;
			}
			
			if (y < minY) {
				minY = y;
			}
			if (y > maxY) {
				maxY = y;
			}
		}
		
		width = (1 + maxX - minX);
		height = (1 + maxY - minY);
		
		offsetX = -minX;
		offsetY = -minY;
	}
	
	
	
	private final int getIndex(final int x, final int y) {
		return (((y + offsetY) * height) + (x + offsetX));
	}
	
	public int getIndex(final Tile tile) {
		return getIndex(tile.getX(), tile.getY());
	}
	
	public int getMaximum() {
		return ((width * height) - 1);
	}
	
}