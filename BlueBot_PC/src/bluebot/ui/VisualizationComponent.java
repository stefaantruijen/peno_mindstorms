package bluebot.ui;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import bluebot.MotionListener;
import bluebot.util.Utils;



/**
 * 
 * @author Ruben Feyen
 */
public class VisualizationComponent extends JComponent
		implements MotionListener {
	private static final long serialVersionUID = 1L;
	
	private static final BufferedImage IMAGE_ROBOT;
	static {
		BufferedImage image;
		try {
			image = ImageIO.read(VisualizationComponent.class.getResource("rc_car.png"));
		} catch (final IOException e) {
			image = null;
		}
		IMAGE_ROBOT = image;
	}
	
	private double heading = 0D;
	
	
	
	protected void drawMaze(final Graphics2D gfx, final int w, final int h) {
		// TODO
		gfx.setColor(Color.BLACK);
		
		for (int i = 0; i < 6; i++) {
			gfx.fillRect(0, ((128 * i) - 1), w, 2);
			gfx.fillRect(((128 * i) - 1), 0, 2, h);
		}
		
		gfx.setColor(Color.RED);
		gfx.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
		
		gfx.drawString("Klik op dit onderdeel en dat van de sensoren", 24, 50);
		gfx.drawString("voor een demo van de werking ervan.", 24, 75);
		
		gfx.drawString("Let op de nieuwe speed-slider:", 24, 150);
		
		gfx.drawString("- high speed", 50, 200);
		gfx.drawString("- medium speed", 50, 225);
		gfx.drawString("- low speed", 50, 250);
		gfx.drawString("- disabled", 50, 275);
	}
	
	protected void drawRobot(final Graphics2D gfx, final int w, final int h) {
		final int cx = (w / 2);
		final int cy = (h / 2);
		
		final AffineTransform transform = gfx.getTransform();
		
		gfx.translate(cx, cy);
		gfx.rotate(heading);
		
		/*
		gfx.setColor(Color.BLACK);
		
		final double r = ((Math.min(w, h) / 2D) - 10D);
		
		final int[] px = new int[4];
		final int[] py = new int[4];
		
		px[0] = 0;
		py[0] = (int)Math.round(-r);
		
		px[1] = (int)Math.round((r / 2));
		py[1] = (int)Math.round((r / 2));
		
		px[2] = (int)Math.round(-(r / 2));
		py[2] = py[1];
		
		gfx.fillPolygon(px, py, 3);
		*/
		
		final BufferedImage img = IMAGE_ROBOT;
		
		final int iw = img.getWidth();
		final int ih = img.getHeight();
		
		gfx.drawImage(img, -(iw / 2), -(ih / 2), iw, ih, this);
		
		if (transform != null) {
			gfx.setTransform(transform);
		}
	}
	
	public void onMotion(final float x, final float y, float heading) {
		heading = Utils.degrees2radians(heading);
		if (heading != this.heading) {
			this.heading = heading;
			repaint(0L);
		}
	}
	
	@Override
	protected void paintComponent(final Graphics g) {
		final Graphics2D gfx = (Graphics2D)g;
		
		final int w = getWidth();
		final int h = getHeight();
		
		gfx.setColor(Color.WHITE);
		gfx.fillRect(0, 0, w, h);
		
		drawMaze(gfx, w, h);
		drawRobot(gfx, w, h);
	}
	
}