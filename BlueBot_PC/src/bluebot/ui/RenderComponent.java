package bluebot.ui;


import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;



/**
 * 
 * @author Ruben Feyen
 */
public class RenderComponent extends JComponent {
	private static final long serialVersionUID = 1L;
	
	private BufferedImage buffer;
	
	
	public RenderComponent() {
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(final ComponentEvent event) {
				onResize();
			}
		});
	}
	
	
	
	protected void onResize() {
		buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
	}
	
	@Override
	protected void paintComponent(final Graphics gfx) {
		gfx.clearRect(0, 0, getWidth(), getHeight());
		gfx.drawImage(buffer, 0, 0, this);
	}
	
	public void render(final Renderable renderable) {
		final Graphics gfx = buffer.createGraphics();
		renderable.render(gfx);
		gfx.dispose();
		repaint(0L);
	}
	
}