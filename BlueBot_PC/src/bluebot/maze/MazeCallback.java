package bluebot.maze;


import bluebot.graph.Tile;



/**
 * 
 * @author Ruben Feyen
 */
public interface MazeCallback {
	
	/**
	 * "Ik wil over de wip rijden"
	 * @param barcode
	 * @return true: toestemming om over de wip rijden
	 * 		   false: geen toestemming
	 */
	public boolean lockSeesaw(int barcode);
	
	public void notifyGameOver();
	/**
	 * wordt gestuurd indien robot object heeft
	 */
	public void notifyObjectFound();
	
	/**
	 * zend tile naar teammate
	 * @param tile
	 */
	public void sendTile(Tile tile);
	
	/**
	 * Over de wip gereden
	 */
	public void unlockSeesaw();
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param angle heading van de body
	 */
	public void updatePosition(long x, long y, double angle);
	
}
