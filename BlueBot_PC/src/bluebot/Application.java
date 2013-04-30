package bluebot;


import bluebot.game.World;



/**
 * 
 * @author Ruben Feyen
 */
public class Application {
	
	private String gameId;
	private World world;
	
	
	public Application(final World world, final String gameId) {
		this.gameId = gameId;
		this.world = world;
	}
	
	
	
	public String getGameId() {
		return gameId;
	}
	
	public World getWorld() {
		return world;
	}
	
}
