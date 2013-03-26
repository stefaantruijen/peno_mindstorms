package bluebot.ui;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.TexturePaint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import bluebot.graph.Border;
import bluebot.graph.Tile;
import bluebot.maze.MazeListener;
import bluebot.util.Utils;



/**
 * 
 * @author Ruben Feyen
 */
public class VisualizationComponent2D extends VisualizationComponent
		implements ActionListener, MazeListener {
	private static final long serialVersionUID = 1L;
	
	private static final BufferedImage IMAGE_BRICK;
	private static final double IMAGE_BRICK_SCALE = 0.5D;
	private static final BufferedImage IMAGE_SENSOR;
	private static final double IMAGE_SENSOR_SCALE = 0.2D;
	private static final HashMap<String, BufferedImage> IMAGES;
	private static final Paint TEXTURE_TILE;
	public static final int TILE_RESOLUTION = 50;
	private static final double ZOOM_STEP = 0.25D;
	static {
		IMAGE_BRICK  = loadImage("nxt_brick.png");
		IMAGE_SENSOR = loadImage("nxt_sensor.png");
		IMAGES = new HashMap<String, BufferedImage>();
		
		TEXTURE_TILE = loadTexture("hardboard.jpg");
	}
	
	private final Object lock = new Object();
	
	private int dx, dy;
	private int maxX, maxY;
	private BufferedImage maze;
	private int minX, minY;
	private int zoom;
	
	
	public VisualizationComponent2D() {
		final int size = calculatePreferredSize();
//		setComponentPopupMenu(createContextMenu());
		setPreferredSize(new Dimension(size, size));
		
		final MouseMonitor monitor = new MouseMonitor();
		addMouseListener(monitor);
		addMouseMotionListener(monitor);
		addMouseWheelListener(monitor);
	}
	
	
	
	public void actionPerformed(final ActionEvent event) {
		final String command = event.getActionCommand();
		if ((command == null) || command.isEmpty()) {
			return;
		} else if (command.equals("RESET")) {
			synchronized (lock) {
				maze = null;
			}
		} else if (command.startsWith("ZOOM_")) {
			final int zoom = Integer.parseInt(command.substring(5));
			this.zoom = ((zoom - 100) / 25);
		}
//		repaint(0L);
	}
	
	private static final int calculatePreferredSize() {
		int size = (3 * TILE_RESOLUTION);
		for (final int step = (TILE_RESOLUTION << 1); size < 500; size += step);
		return size;
	}
	
	private final int[] calculateScaledSize(final BufferedImage img,
			final double scale) {
		int dx = img.getWidth();
		int dy = img.getHeight();
		
		final int max = (int)Math.round(scale * TILE_RESOLUTION);
		
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
		
		if (zoom != 0) {
			final double factor = getZoomFactor();
			dx = (int)Math.round(factor * dx);
			dy = (int)Math.round(factor * dy);
		}
		
		return new int[] { dx, dy };
	}
	
	@SuppressWarnings("unused")
	private final JPopupMenu createContextMenu() {
		final JPopupMenu menu = new JPopupMenu();
		
		menu.add(createContextMenuZoom());
		
		menu.addSeparator();
		
		final JMenuItem itemReset = new JMenuItem("Reset");
		itemReset.setActionCommand("RESET");
		itemReset.addActionListener(this);
		menu.add(itemReset);
		
		return menu;
	}
	
	private final JMenu createContextMenuZoom() {
		final JMenu menu = new JMenu("Zoom");
		
		final JMenuItem item300 = new JMenuItem("300 %");
		item300.setActionCommand("ZOOM_300");
		item300.setHorizontalAlignment(JMenuItem.RIGHT);
		item300.addActionListener(this);
		menu.add(item300);
		
		menu.addSeparator();
		
		final JMenuItem item100 = new JMenuItem("100 %");
		item100.setActionCommand("ZOOM_100");
		item100.setHorizontalAlignment(JMenuItem.RIGHT);
		item100.addActionListener(this);
		menu.add(item100);
		
		menu.addSeparator();
		
		final JMenuItem item50 = new JMenuItem("50 %");
		item50.setActionCommand("ZOOM_50");
		item50.setHorizontalAlignment(JMenuItem.RIGHT);
		item50.addActionListener(this);
		menu.add(item50);
		
		return menu;
	}
	
	private static final BufferedImage createImage() {
		return new BufferedImage(TILE_RESOLUTION, TILE_RESOLUTION, BufferedImage.TYPE_INT_ARGB);
	}
	
	protected void drawGhost(final Graphics2D gfx, final int w, final int h,
			final float x, final float y, final float body) {
//		System.out.println("Ghost at (" + x + ", " + y + ") # " + body);
		final AffineTransform transform = gfx.getTransform();
		
		final int resolution = getTileResolution();
		int dx = Math.round(resolution * (x - this.x) / Tile.SIZE);
		int dy = Math.round(resolution * (y - this.y) / Tile.SIZE);
		gfx.translate(dx, -dy);
		
		BufferedImage img;
		int[] size;
		
		//	BODY
		gfx.rotate(Utils.degrees2radians(body));
		
		img = IMAGE_BRICK;
		size = calculateScaledSize(img, IMAGE_BRICK_SCALE);
		dx = size[0];
		dy = size[1];
		
		gfx.drawImage(img, -(dx / 2), -(dy / 2), dx, dy, this);
		
		gfx.setColor(new Color(0x33FF0000, true));
		gfx.fillRect((1 - (dx / 2)), (1 - (dy / 2)), (dx - 2), (dy - 2));
		
		gfx.setTransform(transform);
	}
	
	protected void drawMaze(final Graphics2D gfx, final int w, final int h) {
		gfx.setBackground(Color.DARK_GRAY);
		gfx.clearRect(0, 0, w, h);
		
		final int resolution = getTileResolution();
		if (maze == null) {
			gfx.setColor(Color.WHITE);
			
			final int thickness = Math.max(1, (resolution >> 6));
			
			int dx = (Math.round(resolution * this.x / Tile.SIZE) - this.dx);
			for (; dx < 0; dx += resolution);
			dx %= resolution;
			
			int dy = ((resolution - Math.round(resolution * this.y / Tile.SIZE)) - this.dy);
			for (; dy < 0; dy += resolution);
			dy %= resolution;
			
			dx = ((w / 2) - dx);
			dy = ((h / 2) - dy);
			
			for (int x = (dx - (resolution >>> 1) - thickness); x >= 0; x -= resolution) {
				gfx.fillRect(x, 0, (thickness << 1), h);
			}
			for (int x = (dx + (resolution >>> 1) - thickness); x < w; x += resolution) {
				gfx.fillRect(x, 0, (thickness << 1), h);
			}
			
			for (int y = (dy - (resolution >>> 1) - thickness); y >= 0; y -= resolution) {
				gfx.fillRect(0, y, w, (thickness << 1));
			}
			for (int y = (dy + (resolution >>> 1) - thickness); y < h; y += resolution) {
				gfx.fillRect(0, y, w, (thickness << 1));
			}
		} else {
			int ax = (resolution / 2);
			if (minX < 0) {
				ax -= (resolution * minX);
			}
			
			int ay = (resolution / 2);
			if (maxY > 0) {
				ay += (resolution * maxY);
			}
			
			ax += Math.round(resolution * x / Tile.SIZE);
			ay -= Math.round(resolution * y / Tile.SIZE);
			
			final BufferedImage img = maze;
			
			int dx = img.getWidth();
			int dy = img.getHeight();
			if (zoom != 0) {
				final double factor = getZoomFactor();
				dx = (int)Math.round(factor * dx);
				dy = (int)Math.round(factor * dy);
			}
			
			gfx.drawImage(img,
					((w / 2) - ax + this.dx),
					((h / 2) - ay + this.dy),
					dx, dy, this);
		}
		
		gfx.translate(((w / 2) + dx), ((h / 2) + dy));
	}
	
	protected void drawRobot(final Graphics2D gfx, final int w, final int h) {
//		System.out.println("Player at (" + x + ", " + y + ") # " + body);
		final AffineTransform transform = gfx.getTransform();
		
		int dx, dy;
		BufferedImage img;
		int[] size;
		
		//	BODY
		gfx.rotate(body);
		
		img = IMAGE_BRICK;
		size = calculateScaledSize(img, IMAGE_BRICK_SCALE);
		dx = size[0];
		dy = size[1];
		
		gfx.drawImage(img, -(dx / 2), -(dy / 2), dx, dy, this);
		
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
		
		gfx.drawImage(img, -(dx / 2), -(offset + dy), dx, dy, this);
		
		gfx.setTransform(transform);
	}
	
	protected void drawTile(final Graphics2D gfx,
			final int x, final int y, final Tile tile) {
//		gfx.setColor(Color.YELLOW);
		gfx.setPaint(TEXTURE_TILE);
		gfx.fillRect(x, y, TILE_RESOLUTION, TILE_RESOLUTION);
		
//		Color overlay = null;
//		switch (tile.getBarCode()) {
//			case 13:
//				// Waypoint
//				overlay = new Color(0x66FF0000, true);
//				break;
//			case 55:
//				// Finish
//				overlay = new Color(0x6600FF00, true);
//				break;
//		}
//		if (overlay != null) {
//			gfx.setColor(overlay);
//			gfx.fillRect(x, y, TILE_RESOLUTION, TILE_RESOLUTION);
//		}
		
		int thickness = (TILE_RESOLUTION >> 4);
		
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
		
		final int barcode = tile.getBarCode();
		if (barcode > 0) {
			final BufferedImage icon = getBarcodeImage(barcode);
			if (icon == null) {
				thickness = Math.max(1, (TILE_RESOLUTION / 20));
				
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
					int xx = x + ((TILE_RESOLUTION >>> 1) - (thickness << 2));
					for (int i = 0; i < 8; i++) {
						gfx.setColor(colors[i]);
						gfx.fillRect(xx, y, thickness, TILE_RESOLUTION);
						xx += thickness;
					}
				} else {
					// Vertical
					int yy = y + ((TILE_RESOLUTION >>> 1) - (thickness << 2));
					for (int i = 0; i < 8; i++) {
						gfx.setColor(colors[i]);
						gfx.fillRect(x, yy, TILE_RESOLUTION, thickness);
						yy += thickness;
					}
				}
			} else {
				final int delta = (TILE_RESOLUTION / 8);
				gfx.drawImage(icon,
						(x + delta), (y + delta),
						(6 * delta), (6 * delta),
						this);
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
	
	private final BufferedImage getBarcodeImage(final int barcode) {
		/*
		switch (barcode) {
			case 5:
				return loadImageBarcode("turn_left");
			case 9:
				return loadImageBarcode("turn_right");
			case 13:
				return loadImageBarcode("waypoint");
			case 15:
				return loadImageBarcode("music");
			case 19:
				return loadImageBarcode("wait");
			case 25:
				return loadImageBarcode("slow");
			case 37:
				return loadImageBarcode("fast");
			case 55:
				return loadImageBarcode("finish");
			default:
				return null;
		}
		*/
		return null;
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
	
	private final int getTileResolution() {
		if (zoom == 0) {
			return TILE_RESOLUTION;
		}
		return (int)Math.round(getZoomFactor() * TILE_RESOLUTION);
	}
	
	private final double getZoomFactor() {
		return (1D + (zoom * ZOOM_STEP));
	}
	
	private static final BufferedImage loadImage(final String name) {
		try {
			return ImageIO.read(VisualizationComponent.class.getResource(name));
		} catch (final IOException e) {
			return null;
		}
	}
	
	@SuppressWarnings("unused")
	private static final BufferedImage loadImageBarcode(final String name) {
		BufferedImage image = IMAGES.get(name);
		if (image != null) {
			return image;
		}
		
		image = loadImage("barcode_" + name + ".png");
		if (image == null) {
			return null;
		}
		
		IMAGES.put(name, image);
		return image;
	}
	
	@SuppressWarnings("unused")
	private static final BufferedImage loadImageRobot() {
		final BufferedImage image;
		try {
			image = ImageIO.read(VisualizationComponent.class.getResource("rc_car.png"));
			return image;
		} catch (final IOException e) {
			return null;
		}
		
		/*
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
		*/
	}
	
	private static final Paint loadTexture(final String name) {
		final BufferedImage img = loadImage("rendering/" + name);
		if (img == null) {
			return Color.YELLOW;
		}
		return new TexturePaint(img,
				new Rectangle2D.Double(0, 0, img.getWidth(), img.getHeight()));
	}
	
	public void onTileUpdate(final Tile tile) {
		final int tx = tile.getX();
		final int ty = tile.getY();
		
		final BufferedImage img;
		final int x, y;
		synchronized (lock) {
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
			img = maze;
		}
		
		final Graphics2D gfx = img.createGraphics();
		drawTile(gfx, x, y, tile);
		gfx.dispose();
		
//		repaint(0L);
	}
	
	protected void render(final Graphics2D gfx, final int w, final int h) {
		drawMaze(gfx, w, h);
		drawRobot(gfx, w, h);
	}
	
	public void reset() {
		synchronized (lock) {
			maze = null;
		}
	}
	
	private static final BufferedImage resizeImage(final BufferedImage image,
			final int x, final int y, final int w, final int h) {
		final BufferedImage resized = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		
		final Graphics2D gfx = resized.createGraphics();
		
		gfx.drawImage(image, x, y, image.getWidth(), image.getHeight(), null);
		
		gfx.dispose();
		return resized;
	}
	
	
	
	
	
	
	
	
	
	
	private final class MouseMonitor extends MouseAdapter {
		
		private int dx, dy, x0, y0;
		
		
		
		@Override
		public void mouseDragged(final MouseEvent event) {
			VisualizationComponent2D.this.dx = (dx + event.getX() - x0);
			VisualizationComponent2D.this.dy = (dy + event.getY() - y0);
			repaint(0L);
		}
		
		@Override
		public void mousePressed(final MouseEvent event) {
			switch (event.getButton()) {
				case MouseEvent.BUTTON1:
					dx = VisualizationComponent2D.this.dx;
					dy = VisualizationComponent2D.this.dy;
					x0 = event.getX();
					y0 = event.getY();
					break;
					
				case MouseEvent.BUTTON3:
					VisualizationComponent2D.this.dx = 0;
					VisualizationComponent2D.this.dy = 0;
					repaint(0L);
					break;
			}
		}
		
		@Override
		public void mouseWheelMoved(final MouseWheelEvent event) {
			int scroll = -event.getWheelRotation();
			if (scroll == 0) {
				return;
			}
			
			switch (event.getScrollType()) {
				case MouseWheelEvent.WHEEL_BLOCK_SCROLL:
					if ((scroll < 4) && (scroll > -4)) {
						scroll = ((scroll > 0) ? 4 : -4);
					}
					break;
				case MouseWheelEvent.WHEEL_UNIT_SCROLL:
					if (scroll > 1) {
						scroll = 1;
					} else if (scroll < -1) {
						scroll = -1;
					}
					break;
			}
			
			zoom += scroll;
			if (zoom > 8) {
				zoom = 8;
			} else if (zoom < -2) {
				zoom = -2;
			}
			
			repaint(0L);
		}
		
	}
	
}