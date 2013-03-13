package bluebot.game;


import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import com.rabbitmq.client.Channel;

import bluebot.io.rabbitmq.RabbitMQ;

import peno.htttp.Client;
import peno.htttp.Handler;



/**
 * 
 * @author Ruben Feyen
 */
public class GameClient extends Client {
	
	public GameClient(final String gameId, final String playerId) throws IOException {
		super(RabbitMQ.connect(), new HandlerDispatcher(), gameId, playerId);
	}
	
	
	
	public void addHandler(final Handler handler) {
		getHandlerDispatcher().addHandler(handler);
	}
	
	protected Channel getChannel() {
		return getField("channel", Channel.class);
	}
	
	private final <T> T getField(final String name, final Class<T> type) {
		try {
			final Field field = Client.class.getDeclaredField(name);
			if (field.isAccessible()) {
				return type.cast(field.get(this));
			} else {
				field.setAccessible(true);
				try {
					return type.cast(field.get(this));
				} finally {
					field.setAccessible(false);
				}
			}
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	protected Handler getHandler() {
		return getField("handler", Handler.class);
	}
	
	protected HandlerDispatcher getHandlerDispatcher() {
		return (HandlerDispatcher)getHandler();
	}
	
	public void removeHandler(final Handler handler) {
		getHandlerDispatcher().removeHandler(handler);
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
