package bluebot.game;


import java.util.List;

import peno.htttp.PlayerHandler;
import peno.htttp.Tile;



/**
 * 
 * @author Ruben Feyen
 */
public abstract class PlayerAdapter extends GameAdapter implements PlayerHandler {
	
	public void gameRolled(final int playerNumber, final int objectNumber) {
		//	ignored
	}
	
	public void teamConnected(final String partnerId) {
		//	ignored
	}
	
	public void teamPosition(final double x, final double y, final double angle) {
		//	ignored
	}
	
	public void teamTilesReceived(final List<Tile> tiles) {
		//	ignored
	}
	
}
