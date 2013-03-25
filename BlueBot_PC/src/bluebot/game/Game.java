package bluebot.game;


import java.io.IOException;

import bluebot.io.rabbitmq.RabbitMQ;

import peno.htttp.PlayerClient;
import peno.htttp.PlayerHandler;
import peno.htttp.SpectatorClient;



/**
 * Represents a game
 * 
 * @author Ruben Feyen
 */
public class Game {
	
	private PlayerClient client;
	private SpectatorClient spectator;
	private World world;
	
	
	public Game(final String gameId, final String playerId,
			final PlayerHandler handler, final World world) throws IOException {
		this.client = createClient(gameId, playerId, handler);
		this.spectator = createSpectator(gameId, world);
		this.world = world;
	}
	
	
	
	protected PlayerClient createClient(final String gameId, final String playerId,
			final PlayerHandler handler) throws IOException {
		return new PlayerClient(RabbitMQ.connect(), handler, gameId, playerId);
	}
	
	protected SpectatorClient createSpectator(final String gameId, final World world)
			throws IOException {
		return new SpectatorClient(RabbitMQ.connect(),
				world.createSpectatorHandler(),
				gameId);
	}
	
	public PlayerClient getClient() {
		return client;
	}
	
	public World getWorld() {
		return world;
	}
	
	private final void leaveGame() {
		try {
			client.leave();
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}
	
	private final void stopSpectator() {
		spectator.stop();
	}
	
	public void terminate() {
		leaveGame();
		stopSpectator();
	}
	
}
