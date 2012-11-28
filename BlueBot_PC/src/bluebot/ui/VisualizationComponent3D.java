package bluebot.ui;


import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import bluebot.graph.Border;
import bluebot.graph.Tile;
import bluebot.maze.Maze;
import bluebot.ui.rendering.AntiAlias;
import bluebot.ui.rendering.Texture;



/**
 * 
 * @author Ruben Feyen
 */
public class VisualizationComponent3D extends VisualizationComponent {
	private static final long serialVersionUID = 1L;
	
	private static final int ANTI_ALIAS = (1 << 2);
	private static final double EPSILON = (1D / (1 << 20));
	private static final double EYE_HEIGHT = 150D;
	private static final double PITCH_HORIZON = -(Math.PI / 36D); // 5° down
	private static final Texture TEXTURE_FLOOR;
	private static final Texture TEXTURE_WALL;
	private static final double VIEWPORT_ANGLE_HORIZONTAL = 90D;
	private static final double VIEWPORT_ANGLE_VERTICAL = 90D;
	private static final double WALL_HEIGHT = 200D;
	static {
		TEXTURE_FLOOR = Texture.load("hardboard.jpg");
		TEXTURE_WALL = Texture.load("wall.bmp");
	}
	
	private BufferedImage buffer;
	private Maze maze = null;
	private double pitch_delta, x, y, yaw, yaw_delta;
	
	
	public VisualizationComponent3D() {
		this(500, 500);
	}
	public VisualizationComponent3D(final int width, final int height) {
		setPreferredSize(new Dimension(width, height));
	}
	
	
	
	private static final int getColor(final Border border) {
		switch (border) {
			case CLOSED:
				return 0xFF000000;
			case OPEN:
				return 0xFFFFFFFF;
			case UNKNOWN:
				return 0xFF999999;
			default:
				return 0xFFFF0000;
		}
	}
	
	private final int getColor(final double yaw, final double pitch) {
		final double cos = Math.cos(yaw);
		final double sin = Math.sin(yaw);
		
		// Y = (X * sin / cos) + EYE_HEIGHT
		
		double dx = -1D, dy = -1D;
		
		final Maze maze = this.maze;
		if (maze != null) {
			int tx, ty;
			if (zero(cos)) {
				// Camera along the Y axis
				// TODO
			} else {
				final double slope = (sin / cos);
				
				if (cos > 0) {
					// Scan +horizontal
					tx = toTile(x);
					dx = ((Tile.SIZE * tx) + (Tile.SIZE / 2D));
//					for (int missing = 0; missing < 10; tx++) {
//						
//					}
				} else {
					// Scan -horizontal
				}
				if (sin > EPSILON) {
					// Scan +vertical
				} else if (sin < -EPSILON) {
					// Scan -vertical
				}
			}
		}
		
		if (pitch >= PITCH_HORIZON) {
			// We're looking at the sky
			return 0xFF99CCFF;
		}
		
		// We're looking at the floor
		final double d = (EYE_HEIGHT / Math.tan(-pitch));
		
		dx = ((x + (Tile.SIZE / 2D) + (cos * d)) / Tile.SIZE);
		dy = ((y + (Tile.SIZE / 2D) + (sin * d)) / Tile.SIZE);
		
		final int tx = (int)Math.floor(dx);
		final int ty = (int)Math.floor(dy);
		
		dx -= tx;
		dy -= ty;
		
		if (maze != null) {
			final Tile tile = maze.getTile(tx, ty);
			if (tile != null) {
				if (dy > 0.975D) {
					return getColor(tile.getBorderNorth());
				}
				if (dx > 0.975D) {
					return getColor(tile.getBorderEast());
				}
				if (dy < 0.025D) {
					return getColor(tile.getBorderSouth());
				}
				if (dx < 0.025D) {
					return getColor(tile.getBorderWest());
				}
			}
		}
		
		return TEXTURE_FLOOR.getColor(dx, dy);
	}
	
	private final int getColor(final double yaw, final double pitch,
			final AntiAlias aa) {
		final double dp = getPitchStep();
		final double dy = getYawStep();
		
		if (aa == AntiAlias.OFF) {
			return getColor((yaw + (0.5 * dy)), (pitch + (0.5 * dp)));
		}
		
		final int n = aa.getCount();
		
		int rgb;
		double r = 0D, g = 0D, b = 0D;
		for (int i = n; i > 0; i--) {
			rgb = getColor((yaw + (Math.random() * dy)), (pitch + (Math.random() * dp)));
			b += (rgb & 0xFF);
			rgb >>>= 8;
			g += (rgb & 0xFF);
			rgb >>>= 8;
			r += (rgb & 0xFF);
		}
		
		rgb = 0xFF00;
		rgb |= (int)Math.round(r / n);
		rgb <<= 8;
		rgb |= (int)Math.round(g / n);
		rgb <<= 8;
		rgb |= (int)Math.round(b / n);
		
		return rgb;
	}
	
	private final double getPitchStep() {
		return Math.toRadians(VIEWPORT_ANGLE_VERTICAL / getHeight());
	}
	
	private final double getYawStep() {
		return Math.toRadians(VIEWPORT_ANGLE_HORIZONTAL / getWidth());
	}
	
	@Override
	public void onMotion(final float x, final float y,
			float body, float head) {
		boolean repaint = false;
		
		if ((x != this.x) || (y != this.y)) {
			this.x = x;
			this.y = y;
			repaint = true;
		}
		
		double yaw = (body - 90D);
		if (yaw < 360D) {
			yaw += 360D;
		}
		yaw = Math.toRadians(360D - yaw);
		if (yaw != this.yaw) {
			this.yaw = yaw;
			repaint = true;
		}
		
		if (repaint) {
			repaint(0L);
		}
	}
	
	public void onTileUpdate(final Tile tile) {
		if (maze == null) {
			maze = new Maze();
		}
		maze.addTile(tile.getX(), tile.getY()).copyBorders(tile);
		repaint(0L);
	}
	
	protected void render(final Graphics2D g, final int w, final int h) {
//		if (maze == null) {
//			g.setColor(Color.RED);
//			g.fillRect(0, 0, w, h);
//			return;
//		}
		
		if (buffer == null) {
			System.out.println("Creating image:  " + w + " x " + h);
			buffer = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		}
		
		final BufferedImage img = buffer;
		
//		final Graphics2D gfx = img.createGraphics();
		final int[] pixels = new int[w * h];
		
		/*
		final double pitch_init = -Math.toRadians(VIEWPORT_ANGLE_VERTICAL / 2D);
		final double pitch_step = Math.toRadians(VIEWPORT_ANGLE_VERTICAL / h);
		final double yaw_init = (this.yaw + this.yaw_delta - Math.toRadians(VIEWPORT_ANGLE_HORIZONTAL / 2D));
		final double yaw_step = Math.toRadians(VIEWPORT_ANGLE_HORIZONTAL / w);
		
		double pitch = pitch_init;
		double yaw;
		for (int y = (h - 1); y >= 0; y--) {
			yaw = yaw_init;
			for (int x = (w - 1); x >= 0; x--) {
				pixels[(w * y) + x] = getColor(yaw, pitch, AntiAlias.OFF);
				yaw += yaw_step;
			}
			pitch += pitch_step;
		}
		*/
		
		// 60° FOV
		final double tan = Math.tan(Math.PI / 4D);
		
		double pitch, x, y, yaw;
		for (int v = (h - 1); v >= 0; v--) {
			y = (tan * ((2D * (h - 1 - v)) - h) / h);
			pitch = Math.atan(y);
			for (int u = 0; u < w; u++) {
				x = (tan * ((2D * u) - w) / w);
				yaw = (this.yaw + this.yaw_delta - Math.atan(x));
				pixels[(w * v) + u] = getColor(yaw, pitch, AntiAlias.OFF);
			}
		}
		
		
		
		img.setRGB(0, 0, w, h, pixels, 0, w);
		
//		gfx.dispose();
		g.drawImage(buffer, 0, 0, w, h, this);
	}
	
	public void reset() {
		// ignored
	}
	
	private static final int toTile(final double c) {
		return (int)Math.floor(0.5 + (c / Tile.SIZE));
	}
	
	private static final boolean zero(final double value) {
		return ((value > -EPSILON) && (value < EPSILON));
	}
	
	
	
	
	
	
	
	
	
	
	private static abstract class Ray {
		
		public double pitch, x, y, yaw;
		
		
		
		public static Ray create(final double pitch) {
			if (zero(pitch)) {
				return new Straight();
			}
			
			final double slope = Math.tan(pitch);
			return ((pitch > 0D) ? new Up(slope) : new Down(slope));
		}
		
		public abstract double limit();
		
		
		
		
		
		
		
		
		
		
		private static final class Down extends Sloped {
			
			protected Down(final double slope) {
				super(slope);
			}
			
			
			
			protected double getLimitZ() {
				return 0D;
			}
			
		}
		
		
		
		
		
		private static abstract class Sloped extends Ray {
			
			protected double slope;
			
			
			protected Sloped(final double slope) {
				this.slope = slope;
			}
			
			
			
			protected abstract double getLimitZ();
			
			public double limit() {
				return ((getLimitZ() - EYE_HEIGHT) / slope);
			}
			
		}
		
		
		
		
		
		private static final class Straight extends Ray {
			
			public double limit() {
				return Double.MAX_VALUE;
			}
			
		}
		
		
		
		
		
		private static final class Up extends Sloped {

			protected Up(final double slope) {
				super(slope);
			}
			
			
			
			protected double getLimitZ() {
				return EYE_HEIGHT;
			}
			
		}
		
	}
	
}