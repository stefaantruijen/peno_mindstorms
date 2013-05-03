package bluebot.maze;


import bluebot.graph.Tile;



/**
 * 
 * @author Ruben Feyen
 */
public interface MazeCallback {
	
	/**
	 * This method should be called when the player has found his/her object
	 * 
	 * @param teamNumber - the number of the team
	 */
	public void joinTeam(int teamNumber);
	
	/**
	 * This method should be called when the player is going to driver over a seesaw
	 * 
	 * @param barcode - the barcode that was read in front of the seesaw
	 */
	public void lockSeesaw(int barcode);
	
	/**
	 * This method should be called when the player has 'touched' the teammate
	 */
	public void notifyGameOver();
	
	/**
	 * This method should be called when the player has found the object
	 */
	public void notifyObjectFound();
	
	/**
	 * This method sends the information of a {@link Tile} to the teammate
	 * 
	 * @param tile - the {@link Tile} to be sent
	 */
	public void sendTile(Tile tile);
	
	/**
	 * This method should be called when the player has driven over a seesaw
	 */
	public void unlockSeesaw();
	
	/**
	 * This method sends the position of the player
	 * 
	 * @param x - relative X (in tile coordinates)
	 * @param y - relative Y (in tile coordinates)
	 * @param angle - the rotation of the player (in degrees),
	 * 					measured clockwise from the positive Y axis
	 */
	public void updatePosition(long x, long y, double angle);
	
}
