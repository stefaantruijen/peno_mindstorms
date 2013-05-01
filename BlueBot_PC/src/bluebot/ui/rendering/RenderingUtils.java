package bluebot.ui.rendering;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import bluebot.graph.Border;
import bluebot.graph.Orientation;
import bluebot.graph.Tile;
import bluebot.util.Resources;



/**
 * 
 * @author Ruben Feyen
 */
public class RenderingUtils {
	
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
	
	
	
	private static final int[] calculateScaledSize(final BufferedImage img,
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
		
		return new int[] { dx, dy };
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
	
	private static final BufferedImage loadImage(final String name) {
		try {
			return ImageIO.read(RenderingUtils.class.getResource(name));
		} catch (final IOException e) {
			return null;
		}
	}
	
	private static final Paint loadTexture() {
		final Paint texture =
				Resources.loadTexture(RenderingUtils.class, "hardboard.jpg");
		return ((texture == null) ? Color.YELLOW : texture);
	}
	
	public static final void renderPlayer(final Graphics2D gfx,
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
	
	public static final void renderPlayer(final Graphics2D gfx,
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
	
	public static final void renderSeesaw(final Graphics2D gfx,
			final Orientation dir, final boolean locked, final int resolution) {
		final int half = (resolution >>> 1);
		final int quarter = (resolution >>> 2);
		final int off = (3 * (resolution >>> 3));
		
		//	TODO
		gfx.setColor(Color.GRAY);
		if (locked) {
			final int[] xx = new int[3];
			final int[] yy = new int[3];
			switch (dir) {
				case NORTH:
					gfx.fillRect(off, (3 * quarter), quarter, quarter);
					xx[0] = quarter;
					yy[0] = quarter * 3;
					xx[1] = quarter * 3;
					yy[1] = quarter * 3;
					xx[2] = half;
					yy[2] = quarter;
					break;
				case EAST:
					gfx.fillRect(0, off, quarter, quarter);
					xx[0] = quarter;
					yy[0] = quarter;
					xx[1] = quarter;
					yy[1] = quarter * 3;
					xx[2] = quarter * 3;
					yy[2] = half;
					break;
				case SOUTH:
					gfx.fillRect(off, 0, quarter, quarter);
					xx[0] = quarter;
					yy[0] = quarter;
					xx[1] = quarter * 3;
					yy[1] = quarter;
					xx[2] = half;
					yy[2] = quarter * 3;
					break;
				case WEST:
					gfx.fillRect((3 * quarter), off, quarter, quarter);
					xx[0] = quarter * 3;
					yy[0] = quarter * 3;
					xx[1] = quarter * 3;
					yy[1] = quarter;
					xx[2] = quarter;
					yy[2] = half;
					break;
			}
			gfx.fillPolygon(xx, yy, 3);
		} else {
			switch (dir) {
				case NORTH:
					gfx.fillRect(off, quarter, quarter, (half + quarter));
					break;
				case EAST:
					gfx.fillRect(0, off, (half + quarter), quarter);
					break;
				case SOUTH:
					gfx.fillRect(off, 0, quarter, (half + quarter));
					break;
				case WEST:
					gfx.fillRect(quarter, off, (half + quarter), quarter);
					break;
			}
		}
	}
	
	public static final void renderTile(final Graphics2D gfx,
			final Tile tile, final int resolution) {
		gfx.setPaint(TEXTURE_TILE);
		gfx.fillRect(0, 0, resolution, resolution);
		
		int thickness = (resolution >> 4);
		
		final Border[] borders = new Border[] {
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
			{ 0, 0, resolution, thickness },
			{ (resolution - thickness), 0, thickness, resolution },
			{ 0, (resolution - thickness), resolution, thickness },
			{ 0, 0, thickness, resolution }
		};
		
		final int barcode = tile.getBarCode();
		if (barcode >= 0) {
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
		
		if (!tile.isSeesaw()) {
			return;
		}
		
		//	TODO
		gfx.setColor(new Color(0x66FF9900, true));
		gfx.fillRect(0, 0, resolution, resolution);
	}
	
}
