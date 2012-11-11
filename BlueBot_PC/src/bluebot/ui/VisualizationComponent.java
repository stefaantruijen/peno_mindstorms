package bluebot.ui;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import bluebot.graph.Border;
import bluebot.graph.Tile;
import bluebot.maze.MazeListener;
import bluebot.util.Utils;



/**
 * 
 * @author Ruben Feyen
 */
public class VisualizationComponent extends RenderingComponent
		implements MazeListener {
	private static final long serialVersionUID = 1L;
	
	private static final BufferedImage IMAGE_ROBOT;
	public static final int TILE_RESOLUTION = 50;
	static {
		IMAGE_ROBOT = loadImageRobot(0.5);
	}
	
	private float heading = 0F;
	private int maxX, maxY;
	private BufferedImage maze;
	private int minX, minY;
	private float x, y;
	
	
	public VisualizationComponent() {
		final int size = calculatePreferredSize();
		setPreferredSize(new Dimension(size, size));
	}
	
	
	
	private static final int calculatePreferredSize() {
		int size = (3 * TILE_RESOLUTION);
		for (final int step = (TILE_RESOLUTION << 1); size < 500; size += step);
		return size;
	}
	
	private static final BufferedImage createImage() {
		return new BufferedImage(TILE_RESOLUTION, TILE_RESOLUTION, BufferedImage.TYPE_INT_ARGB);
	}
	
	protected void drawMaze(final Graphics2D gfx, final int w, final int h) {
		gfx.setBackground(Color.BLACK);
		gfx.clearRect(0, 0, w, h);
		
		if (maze == null) {
			gfx.setColor(Color.WHITE);
			
			final int thickness = (TILE_RESOLUTION >> 5);
			
			int dx = Math.round(TILE_RESOLUTION * this.x / Tile.SIZE);
			for (; dx < 0; dx += TILE_RESOLUTION);
			dx %= TILE_RESOLUTION;
			
			int dy = Math.round(TILE_RESOLUTION * this.y / Tile.SIZE);
			for (; dy < 0; dy += TILE_RESOLUTION);
			dy %= TILE_RESOLUTION;
			
			for (int x = (-(thickness / 2) - dx); x < w; x += TILE_RESOLUTION) {
				gfx.fillRect(x, 0, thickness, h);
			}
			for (int y = (-(thickness / 2) + dy); y < h; y += TILE_RESOLUTION) {
				gfx.fillRect(0, y, w, thickness);
			}
		} else {
			int ax = (TILE_RESOLUTION / 2);
			if (minX < 0) {
				ax -= (TILE_RESOLUTION * minX);
			}
			
			int ay = (TILE_RESOLUTION / 2);
			if (maxY > 0) {
				ay += (TILE_RESOLUTION * maxY);
			}
			
			ax += Math.round(TILE_RESOLUTION * x / Tile.SIZE);
			ay -= Math.round(TILE_RESOLUTION * y / Tile.SIZE);
			
			gfx.drawImage(maze, ((w / 2) - ax), ((h / 2) - ay), this);
		}
	}
	
	protected void drawRobot(final Graphics2D gfx, final int w, final int h) {
		gfx.translate((w / 2), (h / 2));
		gfx.rotate(heading);
		
		final BufferedImage img = IMAGE_ROBOT;
		
		final int dx = img.getWidth();
		final int dy = img.getHeight();
		gfx.drawImage(img, -(dx / 2), -(dy / 2), dx, dy, this);
	}
	
	protected void drawTile(final Graphics2D gfx,
			final int x, final int y, final Tile tile) {
		gfx.setColor(Color.YELLOW);
		gfx.fillRect(x, y, TILE_RESOLUTION, TILE_RESOLUTION);
		
		final int thickness = (TILE_RESOLUTION >> 4);
		
		final Border[] borders = {
			tile.getBorderNorth(),
			tile.getBorderEast(),
			tile.getBorderSouth(),
			tile.getBorderWest()
		};
		final Border[] order = {
			Border.OPEN,
			Border.UNKNOWN,
			Border.CLOSED
		};
		final int[][] params = {
			{ x, y, TILE_RESOLUTION, thickness },
			{ (x + TILE_RESOLUTION - thickness), y, thickness, TILE_RESOLUTION },
			{ x, (y + TILE_RESOLUTION - thickness), TILE_RESOLUTION, thickness },
			{ x, y, thickness, TILE_RESOLUTION }
		};
		
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
	
	private static final Color getBorderColor(final Border border) {
		switch (border) {
			case CLOSED:
				return Color.RED;
			case OPEN:
				return Color.WHITE;
			case UNKNOWN:
				return Color.ORANGE;
			default:
				// Indicates an invalid value
				return Color.CYAN;
		}
	}
	
	private static final BufferedImage loadImageRobot(final double ratio) {
		final BufferedImage image;
		try {
			image = ImageIO.read(VisualizationComponent.class.getResource("rc_car.png"));
		} catch (final IOException e) {
			return null;
		}
		
		int dx = image.getWidth();
		int dy = image.getHeight();
		
		final int max = (int)Math.round(ratio * TILE_RESOLUTION);
		
		boolean resize = false;
		if (dx < dy) {
			if (dy != max) {
				dx = (int)Math.round((double)max * dx / dy);
				dy = max;
				resize = true;
			}
		} else {
			if (dx != max) {
				dy = (int)Math.round((double)max * dy / dx);
				dx = max;
				resize = true;
			}
		}
		if (!resize) {
			return image;
		}
		
		final BufferedImage resized =
				new BufferedImage(dx, dy, BufferedImage.TYPE_INT_ARGB);
		
		final Graphics2D gfx = resized.createGraphics();
		gfx.drawImage(image, 0, 0, dx, dy, null);
		gfx.dispose();
		
		return resized;
	}
	
	public void onMotion(final float x, final float y, float heading) {
		boolean repaint = false;
		
		if ((x != this.x) || (y != this.y)) {
			this.x = x;
			this.y = y;
			repaint = true;
		}
		
		heading = Utils.degrees2radians(heading);
		if (heading != this.heading) {
			this.heading = heading;
			repaint = true;
		}
		
		if (repaint) {
			repaint(0L);
		}
	}
	
	public void onTileUpdate(final Tile tile) {
		final int tx = tile.getX();
		final int ty = tile.getY();
		
		final int x, y;
		if (maze == null) {
			maze = createImage();
			
			x = y = 0;
			
			maxX = minX = tx;
			maxY = minY = ty;
		} else {
			if ((tx < minX) || (tx > maxX) || (ty < minY) || (ty > maxY)) {
				final int w = (1 + Math.max(maxX, tx) - Math.min(tx, minX));
				final int h = (1 + Math.max(maxY, ty) - Math.min(ty, minY));
				
				maze = resizeImage(maze,
						(TILE_RESOLUTION * (minX - Math.min(tx, minX))),
						(TILE_RESOLUTION * (Math.max(maxY, ty) - maxY)),
						(TILE_RESOLUTION * w),
						(TILE_RESOLUTION * h));
			}
			
			x = (TILE_RESOLUTION * (tx - Math.min(tx, minX)));
			y = (TILE_RESOLUTION * (Math.max(maxY, ty) - ty));
			
			maxX = Math.max(maxX, tx);
			maxY = Math.max(maxY, ty);
			minX = Math.min(tx, minX);
			minY = Math.min(ty, minY);
		}
		
		final Graphics2D gfx = maze.createGraphics();
		drawTile(gfx, x, y, tile);
		gfx.dispose();
		
		repaint(0L);
	}
	
	protected void render(final Graphics2D gfx) {
		final int w = getWidth();
		final int h = getHeight();
		
		drawMaze(gfx, w, h);
		drawRobot(gfx, w, h);
	}
	
	private static final BufferedImage resizeImage(final BufferedImage image,
			final int x, final int y, final int w, final int h) {
		final BufferedImage resized = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		
		final Graphics2D gfx = resized.createGraphics();
		
		gfx.drawImage(image, x, y, image.getWidth(), image.getHeight(), null);
		
		gfx.dispose();
		return resized;
	}
	
}