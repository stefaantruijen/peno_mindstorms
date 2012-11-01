package bluebot.ui;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import bluebot.core.Controller;



/**
 * 
 * @author Ruben Feyen
 */
public class GaugeComponent extends RenderingComponent {
	private static final long serialVersionUID = 1L;
	
	private static final double DIAL_OFFSET = (Math.PI * 0.38);
	private static final double DIAL_RANGE  = (Math.PI * 1.25);
//	private static final BufferedImage IMAGE_DIAL;
	private static final BufferedImage IMAGE_GAUGE;
	static {
//		IMAGE_DIAL = loadImage("dial");
		IMAGE_GAUGE = loadImage("gauge");
	}
	
	private Controller controller;
	private int value;
	
	
	public GaugeComponent(final Controller controller) {
		this.controller = controller;
		
		setFocusable(false);
		setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
		setOpaque(false);
		setPreferredSize(getPreferredSize());
		
		final MouseMonitor monitor = new MouseMonitor();
		addMouseListener(monitor);
		addMouseMotionListener(monitor);
	}
	
	
	
	public void addListener(final GaugeListener listener) {
		listenerList.add(GaugeListener.class, listener);
	}
	
	private static final int clamp(final int value) {
		return Math.min(Math.max(0, value), 100);
	}
	
	private final void fireValueChanged(final int value) {
		for (final GaugeListener listener : getListeners()) {
			listener.onValueChanged(value);
		}
	}
	
	protected GaugeListener[] getListeners() {
		return listenerList.getListeners(GaugeListener.class);
	}
	
	public final int getValue() {
		return value;
	}
	
	public final int getPreferredHeight() {
		return IMAGE_GAUGE.getHeight();
	}
	
	@Override
	public final Dimension getPreferredSize() {
		return new Dimension(getPreferredWidth(), getPreferredHeight());
	}
	
	public final int getPreferredWidth() {
		return IMAGE_GAUGE.getWidth();
	}
	
	private static final BufferedImage loadImage(final String name) {
		try {
			return ImageIO.read(GaugeComponent.class.getResource(name + ".png"));
		} catch (final IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void removeListener(final GaugeListener listener) {
		listenerList.remove(GaugeListener.class, listener);
	}
	
	protected void render(final Graphics2D gfx) {
		gfx.drawImage(IMAGE_GAUGE, 0, 0, this);
		
		gfx.setFont(getFont());
		final FontMetrics fm = gfx.getFontMetrics();
		
		final int h = (fm.getHeight() + 5);
		
		gfx.setColor(Color.BLACK);
		gfx.fillRect(60, (165 - h), 80, h);
		
		gfx.setColor(Color.WHITE);
		
		final String display = (Integer.toString(getValue()) + " % ");
		gfx.drawString(display,
				(140 - gfx.getFontMetrics().stringWidth(display)),
				160);
		
		final int value = getValue();
		
		gfx.translate((getWidth() / 2) + 1, (getHeight() / 2));
		
		/*
		final double theta;
		if (value == 50) {
			theta = 0D;
		} else {
			theta = ((DIAL_OFFSET + (DIAL_RANGE * value / 100D)) - Math.PI);
		}
		
		final int dx = (IMAGE_DIAL.getWidth() / 2);
		gfx.rotate(theta);
		gfx.translate(-dx, -72);
		
		gfx.drawImage(IMAGE_DIAL, 0, 0, this);
		*/
		
		final double theta;
		if (value == 50) {
			theta = Math.PI;
		} else {
			theta = (DIAL_OFFSET + (DIAL_RANGE * value / 100D));
		}
		
		gfx.rotate(theta);
		
		gfx.setColor(Color.WHITE);
		gfx.fillRect(-2, 0, 4, 70);
		
		gfx.setColor(Color.GRAY);
		gfx.fillOval(-10, -10, 20, 20);
	}
	
	public void setValue(int value) {
		value = clamp(value);
		if (value != this.value) {
			this.value = value;
			repaint(0L);
			fireValueChanged(value);
		}
	}
	
	
	
	
	
	
	
	
	
	
	private final class MouseMonitor extends MouseAdapter {
		
		private final int calculatePercentage(final int x, final int y) {
			final int cx = (getWidth() / 2);
			final int cy = (getHeight() / 2);
			final int dx = (x - cx);
			final int dy = (cy - y);
			
			if (dx == 0) {
				return ((dy < 0) ? 0 : 50);
			}
			
			double radians;
			if (dy == 0) {
				radians = (Math.PI / 2D);
				if (dx > 0) {
					radians *= 3D;
				}
			} else {
				final double tan = ((double)dx / (double)dy);
				
				radians = Math.atan(tan);
				
				if ((dx * dy) < 0) {
					radians += Math.PI;
				}
				if (dx > 0) {
					radians += Math.PI;
				}
			}
			
			if (radians <= DIAL_OFFSET) {
				return 0;
			}
			
			radians -= DIAL_OFFSET;
			if (radians >= DIAL_RANGE) {
				return 100;
			}
			
			return clamp((int)Math.round(100 * radians / DIAL_RANGE));
		}
		
		@Override
		public void mouseDragged(final MouseEvent event) {
			onEvent(event);
		}
		
		@Override
		public void mousePressed(final MouseEvent event) {
			onEvent(event);
		}
		
		private final void onEvent(final MouseEvent event) {
			final int percentage = calculatePercentage(event.getX(), event.getY());
			if (percentage != getValue()) {
				controller.setSpeed(percentage);
			}
		}
		
	}
	
}