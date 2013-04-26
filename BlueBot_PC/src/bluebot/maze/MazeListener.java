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
	
	/**
	 * Updates the current position
	 * 
	 * @param x - the relative X coordinate
	 * @param y - the relative Y coordinate
	 * @param angle - the rotation, measured counter-clockwise from the X axis
	 */
	public void updatePosition(long x, long y, double angle);
	
}
