package bluebot.ui.rendering;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;

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
	private static final Paint TEXTURE_TILE = loadTexture();
	
	
	
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
	
	private static final Paint loadTexture() {
		final Paint texture =
				Resources.loadTexture(RenderingUtils.class, "hardboard.jpg");
		return ((texture == null) ? Color.YELLOW : texture);
	}
	
	public static final void renderSeesaw(final Graphics2D gfx,
			final Orientation dir, final boolean locked, final int resolution) {
		final int half = (resolution >>> 1);
		final int quarter = (resolution >>> 2);
		final int off = (3 * (resolution >>> 3));
		
		//	TODO
		gfx.setColor(Color.GRAY);
		if (locked) {
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
		} else {
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
