package bluebot.game;


import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import bluebot.Operator;
import bluebot.actionsimpl.MazeActionV2;
import bluebot.graph.Direction;
import bluebot.graph.Tile;
import bluebot.io.rabbitmq.RabbitMQ;
import bluebot.maze.MazeCallback;
import bluebot.maze.MazeMerger;
import bluebot.maze.TileBuilder;
import bluebot.ui.rendering.RenderingUtils;
import bluebot.util.Orientation;
import bluebot.util.Utils;

import peno.htttp.Callback;
import peno.htttp.PlayerClient;
import peno.htttp.PlayerDetails;
import peno.htttp.PlayerHandler;
import peno.htttp.PlayerType;



/**
 * Represents a game
 * 
 * @author Ruben Feyen
 */
public class Game {
	
	private PlayerClient client;
	private MazeActionV2 explorer;
	private Operator operator;
	
	
	public Game(final Operator operator,
			final String gameId, final String playerId,
			final GameCallback callback) throws IOException {
		this.operator = operator;
		this.client = createClient(gameId, playerId, callback);
	}
	
	
	
	protected PlayerClient createClient(final String gameId, final String playerId,
			final GameCallback callback) throws IOException {
		return new PlayerClient(RabbitMQ.connect(),
				new GameHandler(callback),
				gameId,
				createPlayerDetails(playerId));
	}
	
	public MazeCallback createMazeCallback() {
		return new MazeCallbackImpl();
	}
	
	private final PlayerDetails createPlayerDetails(final String playerId) {
		//	TODO:	Collect useful data from ... somewhere
		return new PlayerDetails(playerId,
				PlayerType.valueOf(operator.getPlayerType()),
				(Tile.SIZE / 2D),
				(Tile.SIZE / 2D));
	}
	
	public PlayerClient getClient() {
		return client;
	}
	
	private final MazeActionV2 getExplorer() {
		return explorer;
	}
	
	public Operator getOperator() {
		return operator;
	}
	
	public void init(final Callback<Void> callback) throws IOException {
		final PlayerClient client = getClient();
		if (client.isConnected()) {
			return;
		}
		
		try {
			client.join(callback);
		} catch (final IllegalStateException e) {
			e.printStackTrace();
		}
	}
	
	private final void leaveGame() {
		try {
			client.leave();
		} catch (final IOException e) {
			e.printStackTrace();
		} catch (final NullPointerException e) {
//			e.printStackTrace();
		}
	}
	
	public void render(final Graphics2D gfx, final int tileResolution) {
		final MazeActionV2 explorer = getExplorer();
		if (explorer == null) {
			return;
		}
		
		final AffineTransform transform = gfx.getTransform();
		
		final Orientation pos = operator.getOrientation();
		
		final int dx = (Math.round(pos.getX() * tileResolution / Tile.SIZE)
				+ (tileResolution / 2));
		final int dy = (Math.round(pos.getY() * tileResolution / Tile.SIZE)
				- (tileResolution / 2));
		gfx.translate(-dx, dy);
		final AffineTransform origin = gfx.getTransform();
		
		final MazeMerger merger = explorer.getMazeMerger();
		
		ArrayList<Tile> tiles;
		
		gfx.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.50F));
		tiles = new ArrayList<Tile>(merger.getTilesFromTeammateForGUI());
		for (final Tile tile : tiles) {
			gfx.translate((tileResolution * tile.getX()), -(tileResolution * tile.getY()));
			RenderingUtils.renderTile(gfx, tile, tileResolution);
			gfx.setTransform(origin);
		}
		
		gfx.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.00F));
		tiles = new ArrayList<Tile>(merger.getTilesFromSelfForGUI());
		for (final Tile tile : tiles) {
			gfx.translate((tileResolution * tile.getX()), -(tileResolution * tile.getY()));
			RenderingUtils.renderTile(gfx, tile, tileResolution);
			gfx.setTransform(origin);
		}
		
		gfx.setTransform(transform);
//		System.out.println("body = " + pos.getBody());
//		System.out.println("head = " + pos.getHead());
		RenderingUtils.renderPlayer(gfx,
				Math.toRadians(pos.getHeadingBody()),
				Math.toRadians(pos.getHeadingHead()));
		gfx.setTransform(transform);
	}
	
	public void start() {
		//	ignored
	}
	
	public void stop() {
		leaveGame();
	}
	
	/*
	private static final double toAngle(final Orientation dir) {
		switch (dir) {
			case NORTH:
				return 0D;
			case EAST:
				return (Math.PI / 2D);
			case SOUTH:
				return Math.PI;
			case WEST:
				return (Math.PI * 3D / 2D);
			default:
				throw new IllegalArgumentException("Invalid direction:  " + dir);
		}
	}*/
	@Deprecated
	public void updatePosition(final long x, final long y, final double angle) {
		try {
			getClient().updatePosition(x, y, angle);
		} catch (final IllegalStateException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	
	
	
	
	private final class GameHandler extends GameAdapter implements PlayerHandler {
		
		private GameCallback callback;
		private int objectNumber;
		private int playerNumber;
		
		
		private GameHandler(final GameCallback callback) {
			this.callback = callback;
		}
		
		
		
		public void gameRolled(final int playerNumber, final int objectNumber) {
			this.objectNumber = objectNumber;
			this.playerNumber = playerNumber;
			
			final Thread thread = new Thread(new Runnable() {
				public void run() {
					final boolean ready = callback.prepareForGameStart(
							playerNumber, objectNumber);
					
					try {
						getClient().setReady(ready);
					} catch (final IOException e) {
						e.printStackTrace();
					}
				}
			});
			thread.setDaemon(true);
			thread.start();
		}
		
		@Override
		public void gameStarted() {
			if (explorer != null) {
				gameStopped();
			}
			
			explorer = new MazeActionV2(playerNumber, objectNumber, createMazeCallback());
			
			final Thread thread = new Thread(new Runnable() {
				public void run() {
					try {
						explorer.execute(operator);
					} catch (final Exception e) {
						e.printStackTrace();
					}
				}
			});
			thread.setDaemon(true);
			thread.start();
		}
		
		@Override
		public void gameStopped() {
			final MazeActionV2 explorer = getExplorer();
			if (explorer != null) {
				explorer.abort();
				operator.stop();
				Game.this.explorer = null;
			}
		}
		
		public void teamConnected(final String playerId) {
			final MazeActionV2 explorer = getExplorer();
			if (explorer == null) {
				return;
			}
			
			final ArrayList<Tile> tiles = explorer.getMazeMerger().getTilesFromSelf();
			final int n = tiles.size();
			final peno.htttp.Tile[] exported = new peno.htttp.Tile[n];
			for (int i = 0; i < n; i++) {
				exported[i] = tiles.get(i).export();
			}
			
			try {
				getClient().sendTiles(exported);
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
		
		public void teamDisconnected(final String playerId) {
			//	ignored
		}
		
		public void teamPosition(final long x, final long y, final double angle) {
//			/**
//			 * Chunk of code to transform x and y coördinates from the 90° rotated system.
//			 */
			System.out.println(getClient().getPlayerID()+" RECIEVED X Y = "+x+" "+y + "       angle:"+ angle );

			Tile tempT = new Tile(Math.round(x),Math.round(y));
			Vector<Integer> tempV = new Vector<Integer>();
			tempV.add(0);
			tempV.add(0);
			tempT.transform(Direction.LEFT, tempV);
			long modX = tempT.getX();
			long modY = tempT.getY();
//			/**
//			 * Modify angle from weird system
//			 */
//			double modAngle = -angle+360;
//			System.out.println("("+modX+","+modY+") " +modAngle );
			final MazeActionV2 explorer = getExplorer();
			if (explorer != null) {
				float modAngle = Protocol.angleExternalToInternal(angle);
				System.out.println(getClient().getPlayerID()+" ORIGINAL X Y = "+modX+" "+modY + "       angle:"+ modAngle );
				explorer.setTeammatePosition(modX, modY, modAngle);
			}
		}
		
		public void teamTilesReceived(final List<peno.htttp.Tile> tiles) {
			final MazeActionV2 explorer = getExplorer();
			if (explorer == null) {
				return;
			}
			
			final MazeMerger merger = explorer.getMazeMerger();
			for (final peno.htttp.Tile tile : tiles) {
				Tile t = TileBuilder.getTile(tile.getToken(),(int)tile.getX(), (int)tile.getY());
				t.setPriority(Tile.PRIORITY_TEAMMATE);
//				Vector<Integer> vector = new Vector<Integer>();
//				vector.add(0);
//				vector.add(0);
//				t.transform(Direction.LEFT, vector);
				merger.addTileFromTeammate(t);
			}
			if(merger.hasMerged()){
				explorer.updateTilesFromTeammate();
			}
		}
		
	}
	
	
	
	
	
	private final class MazeCallbackImpl implements MazeCallback {
		
		public void joinTeam(final int teamNumber) {
			try {
				getClient().joinTeam(teamNumber);
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
		
		public void lockSeesaw(final int barcode) {
			try {
				getClient().lockSeesaw(barcode);
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
		
		public void notifyGameOver() {
			try {
				getClient().win();
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
		
		public void notifyObjectFound() {
			try {
				getClient().foundObject();
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
		
		public void sendTile(final Tile tile) {
			try {
				if (getClient().hasTeamPartner()) {
					getClient().sendTiles(tile.export());
				}
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
		
		public void unlockSeesaw() {
			try {
				getClient().unlockSeesaw();
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
		
		public void updatePosition(final long x, final long y, final double angle) {
			try {
				System.out.println(getClient().getPlayerID()+" X Y = "+x+" "+y + "       angle:"+ angle );
//				System.out.println(getClient().getPlayerID()+" updating pos: ("+x+","+y+") "+angle);
//				/**
//				 * Chunk of code to transform x and y coördinates to 90° rotated system.
//				 */
				Tile tempT = new Tile(Math.round(x),Math.round(y));
				Vector<Integer> tempV = new Vector<Integer>();
				tempV.add(0);
				tempV.add(0);
				tempT.transform(Direction.RIGHT, tempV);
				long modX = tempT.getX();
				long modY = tempT.getY();
				
//				/**
//				 * Also transform the Angle to the weird convention
//				 * Is this in degrees?
//				 */
//				float angleF = (float) angle;
//				double modAngle = Utils.clampAngleDegrees(-angleF);
//				System.out.print("    into     ");
				Double modAngle = Protocol.angleInternalToExternal((float) angle);
				System.out.println(getClient().getPlayerID()+" pos: ("+modX+","+modY+") "+modAngle);
				getClient().updatePosition(modX, modY, modAngle);
				
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
}
