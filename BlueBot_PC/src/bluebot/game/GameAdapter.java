package bluebot.game;


import peno.htttp.DisconnectReason;
import peno.htttp.GameHandler;



/**
 * 
 * @author Ruben Feyen
 */
public abstract class GameAdapter implements GameHandler {
	
	public void gamePaused() {
		//	ignored
	}
	
	public void gameRolled(final int index) {
		//	ignored
	}
	
	public void gameStarted() {
		//	ignored
	}
	
	public void gameStopped() {
		//	ignored
	}
	
	public void playerDisconnected(final String playerId,
			final DisconnectReason reason) {
		//	ignored
	}
	
	public void playerFoundObject(final String playerId, final int index) {
		//	ignored
	}
	
	public void playerJoined(final String playerId) {
		//	ignored
	}
	
	public void playerJoining(final String playerId) {
		//	ignored
	}
	
	public void playerReady(final String playerId, final boolean ready) {
		//	ignored
	}
	
}
