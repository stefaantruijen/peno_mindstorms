package bluebot.game;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import bluebot.graph.Tile;
import bluebot.simulator.VirtualInfraredBall;

import peno.htttp.SpectatorHandler;



/**
 * Represents the world
 * 
 * @author Ruben Feyen
 */
public class World {
	public static final int BARCODE_SEESAW_1A = 11;
	public static final int BARCODE_SEESAW_1B = 13;
	public static final int BARCODE_SEESAW_2A = 15;
	public static final int BARCOD_SEESAW_2B = 17;
	public static final int BARCODE_SEESAW_3A = 19;
	public static final int BARCODE_SEESAW_3B = 21;
	
	public static final HashSet<Integer> seesawBarcodes = new HashSet<Integer>() {{ 
		this.add(BARCODE_SEESAW_1A);
		this.add(BARCODE_SEESAW_1B);
		this.add(BARCODE_SEESAW_2A);
		this.add(BARCOD_SEESAW_2B);
		this.add(BARCODE_SEESAW_3A); 
		this.add(BARCODE_SEESAW_3B);
		}};
		
	public static final int[][] barcodeCouples = new int[][]{
			new int[]{BARCODE_SEESAW_1A, BARCODE_SEESAW_1B},  
			new int[]{BARCODE_SEESAW_2A, BARCOD_SEESAW_2B},  
			new int[]{BARCODE_SEESAW_3A, BARCODE_SEESAW_3B}};
	
	private int[] dim;
	private Tile[] maze;
	private HashMap<String, Player> players;
	private HashSet<Integer> seesaws;
	private HashMap<Integer, int[]> seesawLocations;
	private Tile[] starts;
	
	
	public World(final Tile[] maze) throws IllegalArgumentException {
		this.maze = maze.clone();
		this.players = new HashMap<String, Player>();
		this.seesaws = createSeesaws();
		this.starts = createStarts(maze);
		this.seesawLocations = createSeesawLocations(maze);
		placeIRBalls();
		
		int maxX = 0;
		int maxY = 0;
		for (final Tile tile : maze) {
			maxX = Math.max(maxX, tile.getX());
			maxY = Math.max(maxY, tile.getY());
		}
		this.dim = new int[] { (maxX + 1), (maxY + 1) };
	}
	
	private void placeIRBalls(){
		int TILE_SIZE_CM = (int) (Tile.SIZE/10);
		int x0,x1,y0,y1,ballX,ballY;
		for(int[] couple: barcodeCouples){
			try{
				x0 = seesawLocations.get(couple[0])[0];
				x1 = seesawLocations.get(couple[1])[0];
				y0 = seesawLocations.get(couple[0])[1];
				y1 = seesawLocations.get(couple[1])[1];
				if(x0 == x1){
					//Same x coordinate
					ballX = Math.round(x0*TILE_SIZE_CM + TILE_SIZE_CM/2);
					ballY = Math.min(y0,y1)*TILE_SIZE_CM + 2*TILE_SIZE_CM;
				} else {
					//Same x coordinate
					ballX = Math.min(x0,x1)*TILE_SIZE_CM + 2*TILE_SIZE_CM;
					ballY = Math.round(y0*TILE_SIZE_CM + TILE_SIZE_CM/2);
				}
				new VirtualInfraredBall(ballX, ballY);
			}catch(NullPointerException e){
				//No more seesaws if less than 3 are used in the maze.txt
				return;
			}
			
		}
	}
	
	private static final HashSet<Integer> createSeesaws() {
		final HashSet<Integer> seesaws = new HashSet<Integer>();
		seesaws.add(Integer.valueOf(BARCODE_SEESAW_1B));
		seesaws.add(Integer.valueOf(BARCOD_SEESAW_2B));
		seesaws.add(Integer.valueOf(BARCODE_SEESAW_3B));
		return seesaws;
	}
	
	public SpectatorHandler createSpectatorHandler() {
		return new WorldHandler();
	}
	
	private static final Tile[] createStarts(final Tile[] maze)
			throws IllegalArgumentException {
		final Tile[] starts = new Tile[4];
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
	
	
	private static final HashMap<Integer, int[]> createSeesawLocations(final Tile[] maze){
		HashMap<Integer, int[]> result = new HashMap<Integer, int[]>();
		int barcode;
		for (final Tile tile : maze) {
			barcode = tile.getBarCode();
			if(seesawBarcodes.contains(barcode)){
				result.put(barcode, new int[]{tile.getX(), tile.getY()});
			}
		}
		return result;
	}
	
	public int getHeight() {
		return dim[1];
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
	
	public int getWidth() {
		return dim[0];
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
				case BARCODE_SEESAW_1A:
					end = BARCODE_SEESAW_1B;
					break;
				case BARCODE_SEESAW_1B:
					end = BARCODE_SEESAW_1A;
					break;
				case BARCODE_SEESAW_2A:
					end = BARCOD_SEESAW_2B;
					break;
				case BARCOD_SEESAW_2B:
					end = BARCODE_SEESAW_2A;
					break;
				case BARCODE_SEESAW_3A:
					end = BARCODE_SEESAW_3B;
					break;
				case BARCODE_SEESAW_3B:
					end = BARCODE_SEESAW_3A;
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
