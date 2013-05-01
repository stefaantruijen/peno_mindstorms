package bluebot.maze;


import bluebot.graph.Tile;



/**
 * 
 * @author Ruben Feyen
 */
public interface MazeCallback {
	
	public boolean lockSeesaw(int barcode);
	
	public void notifyGameOver();
	
	public void notifyObjectFound();
	
	public void sendTile(Tile tile);
	
	public void unlockSeesaw();
	
	public void updatePosition(long x, long y, double angle);
	
}
