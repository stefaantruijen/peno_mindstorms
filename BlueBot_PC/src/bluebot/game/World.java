package bluebot.game;


import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import bluebot.graph.Orientation;
import bluebot.graph.Tile;
import bluebot.simulator.IRModel;
import bluebot.simulator.VirtualInfraredBall;
import bluebot.ui.rendering.RenderingUtils;

import peno.htttp.PlayerDetails;
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
	private static final Font FONT = new Font(Font.SANS_SERIF, Font.BOLD, 16);
	
	@SuppressWarnings("serial")
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
	private IRModel irModel;
	private Tile[] maze;
	private HashMap<String, Player> players;
	private BufferedImage renderBuffer;
	private int renderTileResolution = -1;
	private HashSet<Integer> seesaws;
	private HashMap<Integer, int[]> seesawLocations;
	private SeesawTile[] seesawTiles;
	private Tile[] starts;
	
	
	public World(final Tile[] maze) throws IllegalArgumentException {
		this.maze = maze.clone();
		this.players = new HashMap<String, Player>();
		this.seesaws = createSeesaws();
		this.starts = createStarts(maze);
		this.seesawLocations = createSeesawLocations(maze);
		this.seesawTiles = createSeesawTiles(maze);
		irModel = new IRModel();
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
				new VirtualInfraredBall(ballX, ballY, irModel);
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
	
	public IRModel getIRModel(){
		return irModel;
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
	
	private static final SeesawTile[] createSeesawTiles(final Tile[] maze) {
		final HashMap<Integer, Tile> tiles = new HashMap<Integer, Tile>();
		for (final Tile tile : maze) {
			final Integer barcode = Integer.valueOf(tile.getBarCode());
			if ((barcode > 0) && seesawBarcodes.contains(barcode)) {
				tiles.put(barcode, tile);
			}
		}
		
		final ArrayList<SeesawTile> list = new ArrayList<World.SeesawTile>();
		
		final int[] seesaws = {
				BARCODE_SEESAW_1A,
				BARCODE_SEESAW_2A,
				BARCODE_SEESAW_3A
		};
		for (final int seesaw : seesaws) {
			final Tile a = tiles.get(Integer.valueOf(seesaw));
			if (a == null) {
				continue;
			}
			
			final Tile b = tiles.get(Integer.valueOf(seesaw + 2));
			if (b == null) {
				continue;
			}
			
			final int ax = a.getX();
			final int ay = a.getY();
			final int bx = b.getX();
			final int by = b.getY();
			
			if (ay == by) {
				//	Horizontal
				if (ax < bx) {
					//	left-to-right
					list.add(new SeesawTile(getTile(maze, (ax + 1), ay),
							seesaw,
							Orientation.WEST));
					list.add(new SeesawTile(getTile(maze, (bx - 1), by),
							(seesaw + 2),
							Orientation.EAST));
				} else {
					//	right-to-left
					list.add(new SeesawTile(getTile(maze, (bx + 1), by),
							(seesaw + 2),
							Orientation.WEST));
					list.add(new SeesawTile(getTile(maze, (ax - 1), ay),
							seesaw,
							Orientation.EAST));
				}
			} else {
				//	Vertical
				if (ay > by) {
					//	top-to-bottom
					list.add(new SeesawTile(getTile(maze, ax, (ay - 1)),
							seesaw,
							Orientation.NORTH));
					list.add(new SeesawTile(getTile(maze, bx, (by + 1)),
							(seesaw + 2),
							Orientation.SOUTH));
				} else {
					//	bottom-to-top
					list.add(new SeesawTile(getTile(maze, bx, (by - 1)),
							(seesaw + 2),
							Orientation.NORTH));
					list.add(new SeesawTile(getTile(maze, ax, (ay + 1)),
							seesaw,
							Orientation.SOUTH));
				}
			}
		}
		
		if (list.isEmpty()) {
			return new SeesawTile[0];
		}
		
		final SeesawTile[] array = new SeesawTile[list.size()];
		list.toArray(array);
		return array;
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
	
	private static final Tile getTile(final Tile[] maze, final int x, final int y) {
		for (final Tile tile : maze) {
			if ((tile.getX() == x) && (tile.getY() == y)) {
				return tile;
			}
		}
		throw new RuntimeException("No such tile!");
	}
	
	public int getWidth() {
		return dim[0];
	}
	
	public boolean isSeesawLocked(final int barcode) {
		return seesaws.contains(barcode);
	}
	
	public void render(final Graphics2D gfx, final int tileResolution) {
		final int w = (tileResolution * getWidth());
		final int h = (tileResolution * getHeight());
		
		AffineTransform origin;
		
		BufferedImage buffer = renderBuffer;
		if ((buffer == null) || (tileResolution != renderTileResolution)) {
			buffer = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
			
			final Graphics2D g = buffer.createGraphics();
			
			origin = g.getTransform();
			for (final Tile tile : maze) {
				final int dx = (tile.getX() * tileResolution);
				final int dy = (h - tileResolution - (tile.getY() * tileResolution));
				g.translate(dx, dy);
				RenderingUtils.renderTile(g, tile, tileResolution);
				g.setTransform(origin);
			}
			
			g.dispose();
			this.renderBuffer = buffer;
			this.renderTileResolution = tileResolution;
		}
		gfx.drawImage(buffer, 0, 0, null);
		
		origin = gfx.getTransform();
		
		for (final SeesawTile seesaw : seesawTiles) {
			final Tile tile = seesaw.tile;
			final int dx = (tile.getX() * tileResolution);
			final int dy = (h - tileResolution - (tile.getY() * tileResolution));
			gfx.translate(dx, dy);
			RenderingUtils.renderSeesaw(gfx,
					seesaw.dir,
					isSeesawLocked(seesaw.barcode),
					tileResolution);
			gfx.setTransform(origin);
		}
		
		gfx.setFont(FONT);
		final FontMetrics fm = gfx.getFontMetrics(FONT);
		for (final Player player : getPlayers()) {
			final int dx = (((int)player.getX() * tileResolution) + (tileResolution / 2));
			final int dy = (h - (tileResolution / 2) - ((int)player.getY() * tileResolution));
			gfx.translate(dx, dy);
			
			RenderingUtils.renderPlayer(gfx,
					Math.toRadians(player.getAngle()),
					Color.YELLOW);
			
			/*
			final int off = -(tileResolution / 5);
			final int size = (tileResolution * 3 / 5);
			gfx.setColor(Color.YELLOW);
			gfx.fillOval(off, off, size, size);
			*/
			
			final int number = player.getNumber();
			final String id = ((number < 0) ? "?" : Integer.toString(number));
			gfx.setColor(Color.BLUE);
			gfx.drawString(id, -(fm.stringWidth(id) / 2), -(fm.getHeight() / 2));
			
			gfx.setTransform(origin);
		}
	}
	
	
	
	
	
	
	
	
	
	
	private static final class SeesawTile {
		
		private final int barcode;
		private final Orientation dir;
		private final Tile tile;
		
		
		public SeesawTile(final Tile tile, final int barcode, final Orientation dir) {
			this.barcode = barcode;
			this.dir = dir;
			this.tile = tile;
		}
		
	}
	
	
	
	
	
	private final class WorldHandler extends GameAdapter implements SpectatorHandler {
		
		public void lockedSeesaw(final String playerId,
				final int playerNumber, final int barcode) {
			//	ignored
		}
		
		@Override
		public void playerJoined(final String playerId) {
			System.out.printf("'%s' joined %s%n", playerId, this);
			players.put(playerId, new Player(playerId));
		}
		
		public void playerRolled(final PlayerDetails player, final int number) {
			//	TODO
		}
		
		public void playerUpdate(final PlayerDetails playerDetails,
				final int playerNumber,
				final long x, final long y, final double angle,
				final boolean foundObject) {
			final Player player = getPlayer(playerDetails.getPlayerID());
			if (player == null) {
				return;
			}
			
			final Tile start = getStart(playerNumber);
			player.update(
					(start.getX() + x),
					(start.getY() + y),
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
