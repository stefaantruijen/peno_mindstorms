package bluebot.maze;


import bluebot.MotionListener;
import bluebot.graph.Tile;



/**
 * Event listener interface for maze events
 * 
 * @author Ruben Feyen
 */
public interface MazeListener extends MotionListener {
	
	public void onTileUpdate(Tile tile);
	
}