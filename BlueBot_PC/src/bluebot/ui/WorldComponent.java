package bluebot.ui;


import java.awt.Graphics2D;

import bluebot.game.World;



/**
 * 
 * @author Ruben Feyen
 */
public class WorldComponent extends RenderingComponent {
	private static final long serialVersionUID = 1L;
	
	private World world;
	
	
	public WorldComponent(final World world) {
		this.world = world;
	}
	
	
	
	protected void render(final Graphics2D gfx) {
		final int w = getWidth();
		final int h = getHeight();
		
		gfx.setColor(getBackground());
		gfx.fillRect(0, 0, w, h);
		
		final int dx = (w / world.getWidth());
		final int dy = (h / world.getHeight());
		
		final int tileResolution = Math.min(dx, dy);
		gfx.translate(
				((w - (tileResolution * world.getWidth())) / 2),
				((h - (tileResolution * world.getHeight())) / 2));
		world.render(gfx, tileResolution);
	}
	
}
