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
	
	
	public RenderingComponent() {
		setFocusable(false);
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
	
}