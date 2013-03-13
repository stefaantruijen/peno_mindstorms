package bluebot.game;


import java.io.IOException;
import java.util.HashMap;

import bluebot.core.Controller;
import bluebot.graph.Tile;
import bluebot.maze.MazeListener;

import peno.htttp.Callback;
import peno.htttp.Handler;



/**
 * Represents a game (session)
 * 
 * @author Ruben Feyen
 */
public class Game {
	
	private GameClient client;
	private Controller controller;
	private HashMap<String, Player> players;
	private State state = State.INITIAL;
	
	
	public Game(final Controller controller,
			final String gameId, final String playerId) throws IOException {
		this.client = createClient(gameId, playerId);
		this.controller = controller;
		
		controller.addListener(new MazeMonitor());
	}
	
	
	
	private final Player addPlayer(final String id) throws GameException {
		final Player player = new Player(id);
		synchronized (players) {
			players.put(id, player);
		}
		return player;
	}
	
	/**
	 * Creates the game client
	 * 
	 * @param gameId - the ID of the game
	 * @param playerId - the ID of the (local) player
	 * 
	 * @return a {@link GameClient} instance
	 * 
	 * @throws IOException if an I/O error occurs
	 */
	protected GameClient createClient(final String gameId, final String playerId)
			throws IOException {
		final GameClient client = new GameClient(gameId, playerId);
		client.addHandler(new GameHandler());
		return client;
	}
	
	protected GameClient getClient() {
		return client;
	}
	
	protected Controller getController() {
		return controller;
	}
	
	/**
	 * Returns the (local) player
	 * 
	 * @return a {@link Player} instance
	 */
	protected Player getPlayer() {
		final String id = getClient().getPlayerID();
		
		Player player = getPlayer(id);
		if (player == null) {
			try {
				player = addPlayer(id);
			} catch (final GameException e) {
				throw new RuntimeException(e);
			}
		}
		return player;
	}
	
	/**
	 * Returns a player
	 * 
	 * @param id - the ID of the player
	 * 
	 * @return a {@link Player} instance
	 * 			or <code>NULL</code> if the player doesn't exist
	 */
	protected Player getPlayer(final String id) {
		return players.get(id);
	}
	
	/**
	 * Returns the state of the game
	 * 
	 * @return a {@link State} instance
	 */
	protected State getState() {
		return state;
	}
	
	public void init(final Callback<?> callback) throws IOException {
		getClient().join(new Callback<Void>() {
			public void onFailure(final Throwable error) {
				callback.onFailure(error);
			}
			
			public void onSuccess(final Void result) {
				try {
					getClient().setReady(true);
					callback.onSuccess(null);
					start();
				} catch (final IOException e) {
					e.printStackTrace();
					callback.onFailure(e);
				}
			}
		});
	}
	
	private final boolean isState(final State state) {
		return (getState() == state);
	}
	
	protected boolean isStateInitial() {
		return isState(State.INITIAL);
	}
	
	protected boolean isStatePaused() {
		return isState(State.PAUSED);
	}
	
	protected boolean isStatePlaying() {
		return isState(State.PLAYING);
	}
	
	protected boolean isStateStopped() {
		return isState(State.STOPPED);
	}
	
	private final void removePlayer(final String id) {
		synchronized (players) {
			players.remove(id);
		}
	}
	
	private final void setState(final State state) {
		if (state == null) {
			throw new NullPointerException("state == null");
		}
		this.state = state;
	}
	
	private final void setStatePaused() {
		setState(State.PAUSED);
	}
	
	private final void setStatePlaying() {
		setState(State.PLAYING);
	}
	
	private final void setStateStopped() {
		setState(State.STOPPED);
	}
	
	private final void start() {
		final Thread starter = new Thread(new Runnable() {
			public void run() {
				final GameClient client = getClient();
				for (;;) {
					//	Determine whether or not someone else
					//	has already started the game
					if (!isStateInitial()) {
						return;
					}
					
					//	Determine whether or not we can start the game
					if (client.canStart()) {
						try {
							client.start();
							return;
						} catch (final IllegalStateException e) {
							e.printStackTrace();
						} catch (final IOException e) {
							e.printStackTrace();
						}
					}
					
					//	Wait a little longer ...
					try {
						Thread.sleep(1000);
					} catch (final InterruptedException e) {
						return;
					}
				}
			}
		});
		starter.setDaemon(true);
		starter.start();
	}
	
	
	
	
	
	
	
	
	
	
	/**
	 * {@link Handler} implementation for the HTTTP protocol
	 * 
	 * @author Ruben Feyen
	 */
	private final class GameHandler implements Handler {
		
		public void gamePaused() {
			setStatePaused();
			//	TODO
		}
		
		public void gameRolled(final int playerNumber) {
			//	TODO
		}
		
		public void gameStarted() {
			setStatePlaying();
			//	TODO
		}
		
		public void gameStopped() {
			setStateStopped();
			//	TODO
		}
		
		public void playerFoundObject(final String playerId) {
			//	TODO
		}
		
		public void playerJoined(final String playerId) {
			try {
				addPlayer(playerId);
			} catch (final GameException e) {
				//	TODO(?):	Notify someone about this
				e.printStackTrace();
			}
		}
		
		public void playerLeft(final String playerId) {
			removePlayer(playerId);
		}
		
		public void playerPosition(final String playerId,
				final double x, final double y, final double angle) {
			
			//	TODO
		}
		
	}
	
	
	
	
	
	private final class MazeMonitor implements MazeListener {
		
		public void onMotion(final float x, final float y,
				final float body, final float head) {
			try {
				//	TODO:	Determine the convention for the angle
				final double angle = body;
				getClient().updatePosition(x, y, angle);
			} catch (final IllegalStateException e) {
				e.printStackTrace();
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
		
		public void onTileUpdate(final Tile tile) {
			//	TODO
		}
		
	}
	
	
	
	
	
	private static enum State {
		INITIAL,
		PLAYING,
		PAUSED,
		STOPPED
	}
	
}
