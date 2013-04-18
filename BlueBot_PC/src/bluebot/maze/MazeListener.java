package bluebot.maze;

import java.util.EventListener;

import bluebot.graph.Tile;

/**
 * Event listener interface for maze events
 * 
 * @author Ruben Feyen
 */
public interface MazeListener extends EventListener{
	
	public void onTileUpdate(Tile tile);
}
