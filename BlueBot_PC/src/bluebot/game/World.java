package bluebot.game;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import bluebot.graph.Tile;

import peno.htttp.SpectatorHandler;



/**
 * Represents the world
 * 
 * @author Ruben Feyen
 */
public class World {
	
	private Tile[] maze;
	private HashMap<String, Player> players;
	private HashSet<Integer> seesaws;
	private Tile[] starts;
	
	
	public World(final Tile[] maze) throws IllegalArgumentException {
		this.maze = maze.clone();
		this.players = new HashMap<String, Player>();
		this.seesaws = createSeesaws();
		this.starts = createStarts(maze);
	}
	
	
	
	private static final HashSet<Integer> createSeesaws() {
		final HashSet<Integer> seesaws = new HashSet<Integer>();
		seesaws.add(Integer.valueOf(13));
		seesaws.add(Integer.valueOf(17));
		seesaws.add(Integer.valueOf(21));
		return seesaws;
	}
	
	public SpectatorHandler createSpectatorHandler() {
		return new WorldHandler();
	}
	
	private static final Tile[] createStarts(final Tile[] maze)
			throws IllegalArgumentException {
		final Tile[] starts = new Tile[0];
		for (final Tile tile : maze) {
			if (tile.isStartPosition()) {
				starts[tile.getStartPlayerId() - 1] = tile;
			}
		}
		for (int i = 0; i < 4; i++) {
			if (starts[i] == null) {
				throw new IllegalArgumentException(
						"No start position for player # " + (i + 1));
			}
		}
		return starts;
	}
	
	public Tile[] getMaze() {
		return maze.clone();
	}
	
	public Player getPlayer(final String id) {
		return players.get(id);
	}
	
	public List<Player> getPlayers() {
		return new ArrayList<Player>(players.values());
	}
	
	public Tile getStart(final int playerNumber) throws IllegalArgumentException {
		if ((playerNumber < 1) || (playerNumber > 4)) {
			throw new IllegalArgumentException(
					"Invalid player number:  " + playerNumber);
		}
		return starts[playerNumber - 1];
	}
	
	public boolean isSeesawLocked(final int barcode) {
		return seesaws.contains(barcode);
	}
	
	
	
	
	
	
	
	
	
	
	private final class WorldHandler extends GameAdapter implements SpectatorHandler {
		
		public void lockedSeesaw(final String playerId,
				final int playerNumber, final int barcode) {
			//	ignored
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
			
			final Tile start = getStart(playerNumber);
			player.update(
					((start.getX() * Tile.SIZE) + (float)x),
					((start.getY() * Tile.SIZE) + (float)y),
					Protocol.angleExternalToInternal(angle),
					playerNumber,
					foundObject);
		}
		
		public void unlockedSeesaw(final String playerId,
				final int playerNumber, final int barcode) {
			final int end;
			switch (barcode) {
				case 11:
					end = 13;
					break;
				case 13:
					end = 11;
					break;
				case 15:
					end = 17;
					break;
				case 17:
					end = 15;
					break;
				case 19:
					end = 21;
					break;
				case 21:
					end = 19;
					break;
				default:
					//	ignored
					return;
			}
			seesaws.remove(Integer.valueOf(end));
			seesaws.add(Integer.valueOf(barcode));
		}
		
	}
	
}
