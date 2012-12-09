package bluebot.ui;


import java.awt.Color;
import java.awt.Graphics2D;

import bluebot.maze.MazeListener;
import bluebot.util.Utils;



/**
 * 
 * @author Ruben Feyen
 */
public abstract class VisualizationComponent
		extends RenderingComponent
		implements MazeListener {
	private static final long serialVersionUID = 1L;
	
	protected static final Color COLOR_WALL;
	static {
		COLOR_WALL = new Color(0xFF593E1A, true);
	}
	
	protected float body, head;
	protected float x, y;
	
	
	
	public void onMotion(final float x, final float y,
			float body, float head) {
		boolean repaint = false;
		
		if ((x != this.x) || (y != this.y)) {
			this.x = x;
			this.y = y;
			repaint = true;
		}
		
		body = Utils.degrees2radians(body);
		if (body != this.body) {
			this.body = body;
			repaint = true;
		}
		
		head = Utils.degrees2radians(head);
		if (head != this.head) {
			this.head = head;
			repaint = true;
		}
		
		if (repaint) {
			repaint(0L);
		}
	}
	
	protected final void render(final Graphics2D gfx) {
		render(gfx, getWidth(), getHeight());
	}
	
	protected abstract void render(Graphics2D gfx, int w, int h);
	
	public abstract void reset();
	
}