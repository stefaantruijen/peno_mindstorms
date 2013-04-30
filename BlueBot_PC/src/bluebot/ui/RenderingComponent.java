package bluebot.ui;


import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;

import javax.swing.JComponent;



/**
 * 
 * @author Ruben Feyen
 */
public abstract class RenderingComponent extends JComponent {
	private static final long serialVersionUID = 1L;
	
	private static final long INTERVAL = 100;
	
	private RenderTask renderer;
	
	
	public RenderingComponent() {
		setFocusable(false);
		setLayout(null);
	}
	
	
	
	public boolean isAntiAliased() {
		return true;
	}
	
	@Override
	protected void paintComponent(final Graphics g) {
		super.paintComponent(g);
		
		final Graphics2D gfx = (Graphics2D)g;
		if (isAntiAliased()) {
			gfx.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
		}
		
		final AffineTransform transform = gfx.getTransform();
		render(gfx);
		if (transform != null) {
			gfx.setTransform(transform);
		}
	}
	
	protected abstract void render(Graphics2D gfx);
	
	public synchronized void startRendering() {
		if (renderer == null) {
			renderer = new RenderTask();
			final Thread host = new Thread(renderer);
			host.setDaemon(true);
			host.start();
		}
	}
	
	public synchronized void stopRendering() {
		if (renderer != null) {
			renderer.kill();
			renderer = null;
		}
	}
	
	
	
	
	
	
	
	
	
	
	private final class RenderTask implements Runnable {
		
		private boolean rendering = true;
		
		
		
		public void kill() {
			rendering = false;
		}
		
		public void run() {
			for (long diff, next = System.currentTimeMillis(); rendering; next += INTERVAL) {
				while ((diff = (next - System.currentTimeMillis())) > 0) {
					try {
						Thread.sleep(diff);
					} catch (final InterruptedException e) {
						kill();
						return;
					}
				}
				
				if (isVisible()) {
					repaint(0L);
				}
				
				next += INTERVAL;
			}
		}
		
	}
	
}