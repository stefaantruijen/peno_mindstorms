package bluebot.ui;


import java.awt.Color;
import java.awt.Graphics2D;

import bluebot.game.Game;
import bluebot.graph.Tile;



/**
 * 
 * @author Ruben Feyen
 */
public class DefaultVisualizationComponent extends VisualizationComponent {
	private static final long serialVersionUID = 1L;
	
	private Game game;
	
	
	
	@Override
	public void onMotion(final float x, final float y,
			float body, float head) {
		super.onMotion(x, y, body, head);
		
		final Game game = this.game;
		if (game != null) {
			game.setPlayerHeadingHead(Math.toRadians(head));
		}
	}
	
	public void onTileUpdate(final Tile tile) {
		//	ignored
	}
	
	protected void render(final Graphics2D gfx, final int w, final int h) {
		gfx.setColor(Color.BLACK);
		gfx.fillRect(0, 0, w, h);
		
		final Game game = this.game;
		if (game != null) {
			gfx.translate((w / 2), (h / 2));
			game.render(gfx);
		}
	}
	
	public void reset() {
		setGame(null);
	}
	
	public void setGame(final Game game) {
		final Game old = this.game;
		if (old != null) {
			old.stop();
		}
		
		this.game = game;
		repaint(0L);
	}
	
}
