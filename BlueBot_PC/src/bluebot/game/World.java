package bluebot.game;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import peno.htttp.SpectatorHandler;



/**
 * Represents the world
 * 
 * @author Ruben Feyen
 */
public class World {
	
	private HashMap<String, Player> players;
	
	
	public World() {
		this.players = new HashMap<String, Player>();
	}
	
	
	
	public SpectatorHandler createSpectatorHandler() {
		return new WorldHandler();
	}
	
	public Player getPlayer(final String id) {
		return players.get(id);
	}
	
	public List<Player> getPlayers() {
		return new ArrayList<Player>(players.values());
	}
	
	
	
	
	
	
	
	
	
	
	private final class WorldHandler extends GameAdapter implements SpectatorHandler {
		
		public void lockedSeesaw(final String playerId,
				final int playerNumber, final int barcode) {
			//	TODO
		}
		
		@Override
		public void playerJoined(final String playerId) {
			players.put(playerId, new Player(playerId));
		}
		
		public void playerUpdate(final String playerId,
				final int playerNumber,
				final double x, final double y, final double angle,
				final boolean foundObject) {
			final Player player = getPlayer(playerId);
			if (player == null) {
				return;
			}
			
			//	TODO:	X & Y conversion
			
			player.update((float)x, (float)y,
					Protocol.angleExternalToInternal(angle),
					playerNumber,
					foundObject);
		}
		
		public void unlockedSeesaw(final String playerId,
				final int playerNumber, final int barcode) {
			//	TODO
		}
		
	}
	
}
