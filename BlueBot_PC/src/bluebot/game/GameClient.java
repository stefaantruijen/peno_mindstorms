package bluebot.game;


import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import bluebot.io.rabbitmq.RabbitConfig;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import peno.htttp.Client;
import peno.htttp.Handler;



/**
 * 
 * @author Ruben Feyen
 */
public class GameClient extends Client {
	
	public GameClient(final String gameId, final String playerId) throws IOException {
		super(connect(), new HandlerDispatcher(), gameId, playerId);
	}
	
	
	
	public void addHandler(final Handler handler) {
		getHandler().addHandler(handler);
	}
	
	private static final Connection connect() throws IOException {
		final ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(RabbitConfig.HOST_NAME);
		factory.setPassword(RabbitConfig.PASSWORD);
		factory.setPort(RabbitConfig.PORT);
		factory.setRequestedHeartbeat(0);
		factory.setUsername(RabbitConfig.USER_NAME);
		factory.setVirtualHost(RabbitConfig.VIRTUAL_HOST);
		return factory.newConnection();
	}
	
	@Override
	protected HandlerDispatcher getHandler() {
		return (HandlerDispatcher)super.getHandler();
	}
	
	public void removeHandler(final Handler handler) {
		getHandler().removeHandler(handler);
	}
	
	
	
	
	
	
	
	
	
	
	private static class HandlerDispatcher implements Handler {
		
		private Handler[] handlers;
		
		
		public HandlerDispatcher() {
			setHandlers(null);
		}
		
		
		
		public void addHandler(final Handler handler) {
			final HashSet<Handler> set = toSet();
			if (set.add(handler)) {
				setHandlers(set);
			}
		}
		
		public void gamePaused() {
			for (final Handler handler : getHandlers()) {
				handler.gamePaused();
			}
		}
		
		public void gameRolled(final int playerNumber) {
			for (final Handler handler : getHandlers()) {
				handler.gameRolled(playerNumber);
			}
		}
		
		public void gameStarted() {
			for (final Handler handler : getHandlers()) {
				handler.gameStarted();
			}
		}
		
		public void gameStopped() {
			for (final Handler handler : getHandlers()) {
				handler.gameStopped();
			}
		}
		
		private final Handler[] getHandlers() {
			return handlers;
		}
		
		public void playerFoundObject(final String playerId) {
			for (final Handler handler : getHandlers()) {
				handler.playerFoundObject(playerId);
			}
		}
		
		public void playerJoined(final String playerId) {
			for (final Handler handler : getHandlers()) {
				handler.playerJoined(playerId);
			}
		}
		
		public void playerLeft(final String playerId) {
			for (final Handler handler : getHandlers()) {
				handler.playerLeft(playerId);
			}
		}
		
		public void playerPosition(final String playerId,
				final double x, final double y, final double angle) {
			for (final Handler handler : getHandlers()) {
				handler.playerPosition(playerId, x, y, angle);
			}
		}
		
		public void removeHandler(final Handler handler) {
			final HashSet<Handler> set = toSet();
			if (set.remove(handler)) {
				setHandlers(set);
			}
		}
		
		private final void setHandlers(final Collection<Handler> handlers) {
			final int n = ((handlers == null) ? 0 : handlers.size());
			this.handlers = new Handler[n];
			if (n > 0) {
				handlers.toArray(this.handlers);
			}
		}
		
		private final HashSet<Handler> toSet() {
			return new HashSet<Handler>(Arrays.asList(handlers));
		}
		
	}
	
}
