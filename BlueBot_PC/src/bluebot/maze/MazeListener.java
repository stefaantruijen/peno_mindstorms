package bluebot.maze;

import bluebot.graph.Tile;

/**
 * Event listener interface for maze events
 * 
 * @author Ruben Feyen
 */
public interface MazeListener {
	
	public void onTileUpdate(Tile tile);
}
