package bluebot.game;


import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

import bluebot.graph.Border;
import bluebot.graph.Tile;
import bluebot.io.rabbitmq.RabbitMQ;
import bluebot.ui.VisualizationComponent;
import bluebot.util.Resources;

import peno.htttp.Callback;
import peno.htttp.PlayerClient;
import peno.htttp.PlayerHandler;
import peno.htttp.SpectatorClient;



/**
 * Represents a game
 * 
 * @author Ruben Feyen
 */
public class Game {
	
	private static final Color COLOR_WALL = new Color(0xFF593E1A, true);
	private static final BufferedImage IMAGE_BRICK;
	private static final double IMAGE_BRICK_SCALE = 0.5D;
	private static final BufferedImage IMAGE_SENSOR;
	private static final double IMAGE_SENSOR_SCALE = 0.2D;
	private static final Paint TEXTURE_TILE = loadTexture();
	static {
		IMAGE_BRICK  = loadImage("nxt_brick.png");
		IMAGE_SENSOR = loadImage("nxt_sensor.png");
	}
	
	private PlayerClient client;
	private double head;
	private HashMap<Long, Tile> map;
	private SpectatorClient spectator;
	private World world;
	
	
	public Game(final String gameId, final String playerId,
			final PlayerHandler handler, final World world) throws IOException {
		this.client = createClient(gameId, playerId, handler);
		this.map = new HashMap<Long, Tile>();
		this.spectator = createSpectator(gameId, world);
		this.world = world;
	}
	
	
	
	private final int[] calculateScaledSize(final BufferedImage img,
			final double scale) {
		int dx = img.getWidth();
		int dy = img.getHeight();
		
		final int max = (int)Math.round(scale * Tile.RESOLUTION);
		
		if (dx < dy) {
			if (dy != max) {
				dx = (int)Math.round((double)max * dx / dy);
				dy = max;
			}
		} else {
			if (dx != max) {
				dy = (int)Math.round((double)max * dy / dx);
				dx = max;
			}
		}
		
		/*
		if (zoom != 0) {
			final double factor = getZoomFactor();
			dx = (int)Math.round(factor * dx);
			dy = (int)Math.round(factor * dy);
		}
		*/
		
		return new int[] { dx, dy };
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
	
	protected void drawPlayer(final Graphics2D gfx,
			final double body, final Color color) {
		int dx, dy;
		BufferedImage img;
		int[] size;
		
		//	BODY
		gfx.rotate(body);
		
		img = IMAGE_BRICK;
		size = calculateScaledSize(img, IMAGE_BRICK_SCALE);
		dx = size[0];
		dy = size[1];
		
		gfx.drawImage(img, -(dx / 2), -(dy / 2), dx, dy, null);
		
		gfx.setColor(color);
		gfx.fillRect((1 - (dx / 2)), (1 - (dy / 2)), (dx - 2), (dy - 2));
	}
	
	protected void drawPlayer(final Graphics2D gfx,
			final double body, final double head) {
		int dx, dy;
		BufferedImage img;
		int[] size;
		
		//	BODY
		gfx.rotate(body);
		
		img = IMAGE_BRICK;
		size = calculateScaledSize(img, IMAGE_BRICK_SCALE);
		dx = size[0];
		dy = size[1];
		
		gfx.drawImage(img, -(dx / 2), -(dy / 2), dx, dy, null);
		
		gfx.setColor(new Color(0x330000FF, true));
		gfx.fillRect((1 - (dx / 2)), (1 - (dy / 2)), (dx - 2), (dy - 2));
		
		//	HEAD
		gfx.rotate(head);
		
		final double cos = Math.cos(head);
		final double sin = Math.sin(head);
		
		final int offset = (int)Math.round(0.55D
				* ((sin * sin * dx / 2D) + (cos * cos * dy / 2D)));
		
		img = IMAGE_SENSOR;
		size = calculateScaledSize(img, IMAGE_SENSOR_SCALE);
		dx = size[0];
		dy = size[1];
		
		gfx.drawImage(img, -(dx / 2), -(offset + dy), dx, dy, null);
	}
	
	protected void drawPlayer(final Graphics2D gfx, final Player player) {
		final double body = Math.toRadians(player.getAngle());
		final String id = player.getId();
		if (id.equals(getClient().getPlayerID())) {
			drawPlayer(gfx, body, head);
		} else {
			drawPlayer(gfx, body, new Color(0x33FF0000, true));
		}
	}
	
	protected void drawTile(final Graphics2D gfx,
			final Border[] borders, final int barcode) {
		final int resolution = Tile.RESOLUTION;
		
		gfx.setPaint(TEXTURE_TILE);
		gfx.fillRect(0, 0, resolution, resolution);
		
		int thickness = (resolution >> 4);
		
		final Border[] order = {
			Border.OPEN,
			Border.UNKNOWN,
			Border.CLOSED
		};
		final int[][] params = {
			{ 0, 0, resolution, thickness },
			{ (resolution - thickness), 0, thickness, resolution },
			{ 0, (resolution - thickness), resolution, thickness },
			{ 0, 0, thickness, resolution }
		};
		
		if (barcode > 0) {
			thickness = Math.max(1, (resolution / 20));
			
			final Color[] colors = {
				Color.BLACK,
				(((barcode & 0x20) == 0) ? Color.BLACK : Color.WHITE),
				(((barcode & 0x10) == 0) ? Color.BLACK : Color.WHITE),
				(((barcode & 0x08) == 0) ? Color.BLACK : Color.WHITE),
				(((barcode & 0x04) == 0) ? Color.BLACK : Color.WHITE),
				(((barcode & 0x02) == 0) ? Color.BLACK : Color.WHITE),
				(((barcode & 0x01) == 0) ? Color.BLACK : Color.WHITE),
				Color.BLACK
			};
			if (borders[0] == Border.CLOSED) {
				// Horizontal
				int xx = ((resolution >>> 1) - (thickness << 2));
				for (int i = 0; i < 8; i++) {
					gfx.setColor(colors[i]);
					gfx.fillRect(xx, 0, thickness, resolution);
					xx += thickness;
				}
			} else {
				// Vertical
				int yy = ((resolution >>> 1) - (thickness << 2));
				for (int i = 0; i < 8; i++) {
					gfx.setColor(colors[i]);
					gfx.fillRect(0, yy, resolution, thickness);
					yy += thickness;
				}
			}
		}
		
		int[] bounds;
		for (final Border border : order) {
			gfx.setColor(getBorderColor(border));
			for (int i = 0; i < 4; i++) {
				if (borders[i] == border) {
					bounds = params[i];
					gfx.fillRect(bounds[0], bounds[1], bounds[2], bounds[3]);
				}
			}
		}
	}
	
	private final void drawTile(final Graphics2D gfx, final Tile tile) {
		drawTile(gfx, new Border[] {
				tile.getBorderNorth(),
				tile.getBorderEast(),
				tile.getBorderSouth(),
				tile.getBorderWest()
		}, tile.getBarCode());
		if (!tile.isSeesaw()) {
			return;
		}
		
		//	TODO
		gfx.setColor(new Color(0x66FF9900, true));
		gfx.fillRect(0, 0, Tile.RESOLUTION, Tile.RESOLUTION);
	}
	
	private static final Color getBorderColor(final Border border) {
		switch (border) {
			case CLOSED:
				return COLOR_WALL;
			case OPEN:
				return Color.WHITE;
			case UNKNOWN:
				return Color.ORANGE;
			default:
				// Indicates an invalid value
				return Color.CYAN;
		}
	}
	
	public PlayerClient getClient() {
		return client;
	}
	
	private final Tile getTile(final int x, final int y) {
		return map.get(key(x, y));
	}
	
	public World getWorld() {
		return world;
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
	
	private static final Long key(final int x, final int y) {
		return Long.valueOf((y << 32L) | x);
	}
	
	private final void leaveGame() {
		try {
			client.leave();
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}
	
	private static final BufferedImage loadImage(final String name) {
		try {
			return ImageIO.read(VisualizationComponent.class.getResource(name));
		} catch (final IOException e) {
			return null;
		}
	}
	
	private static final Paint loadTexture() {
		final Paint texture = Resources.loadTexture(VisualizationComponent.class,
				"rendering/hardboard.jpg");
		return ((texture == null) ? Color.YELLOW : texture);
	}
	
	public void render(final Graphics2D gfx) {
		final AffineTransform transform = gfx.getTransform();
		
		final Player player = getWorld().getPlayer(getClient().getPlayerID());
		
		final float px = player.getX();
		final float py = player.getY();
		
		final double ratio = Tile.ratioPixelsPerDistance();
		
		int dx = (int)Math.round(ratio * px);
		int dy = (int)Math.round(ratio * py);
		
		dx += (Tile.RESOLUTION / 2);
		dy += (Tile.RESOLUTION / 2);
		
		gfx.translate(-dx, -dy);
		final AffineTransform origin = gfx.getTransform();
		
		for (Tile tile : getWorld().getMaze()) {
			final float alpha;
			
			final int x = tile.getX();
			final int y = tile.getY();
			
			final Tile own = getTile(x, y);
			if (own == null) {
				alpha = 0.33F;
			} else {
				alpha = 1F;
				tile = own;
			}
			
			gfx.translate((x * Tile.RESOLUTION), (y * Tile.RESOLUTION));
			gfx.setComposite(AlphaComposite.getInstance(
					AlphaComposite.SRC_OVER, alpha));
			drawTile(gfx, tile);
			gfx.setTransform(origin);
		}
		
		gfx.setTransform(transform);
		
		for (final Player p : getWorld().getPlayers()) {
			dx = (int)Math.round(ratio * (p.getX() - px));
			dy = (int)Math.round(ratio * (py - p.getY()));
			gfx.translate(dx, dy);
			
			gfx.setTransform(transform);
		}
	}
	
	public void setPlayerHeadingHead(final double radians) {
		this.head = radians;
	}
	
	public void start() {
		startSpectator();
	}
	
	private final void startSpectator() {
		try {
			spectator.start();
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}
	
	public void stop() {
		leaveGame();
		stopSpectator();
	}
	
	private final void stopSpectator() {
		spectator.stop();
	}
	
	public void updateTile(final Tile tile) {
		updateTile(tile, getClient().getPlayerNumber());
	}
	
	public void updateTile(final Tile tile, final int playerNumber) {
		final Tile start = getWorld().getStart(playerNumber);
		updateTile(tile,
				(start.getX() + tile.getX()),
				(start.getY() + tile.getY()));
	}
	
	private final void updateTile(final Tile tile, final int x, final int y) {
		map.put(key(x, y), tile);
	}
	
}
