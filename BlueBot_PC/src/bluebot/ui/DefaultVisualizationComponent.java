package bluebot.ui;


import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import bluebot.Operator;
import bluebot.game.Game;
import bluebot.graph.Tile;
import bluebot.ui.rendering.RenderingUtils;
import bluebot.util.Orientation;



/**
 * 
 * @author Ruben Feyen
 */
public class DefaultVisualizationComponent extends VisualizationComponent {
	private static final long serialVersionUID = 1L;
	
	private int dx, dy;
	private Game game;
	private Operator operator;
	
	
	public DefaultVisualizationComponent() {
		final MouseMonitor monitor = new MouseMonitor();
		addMouseListener(monitor);
		addMouseMotionListener(monitor);
		addMouseWheelListener(monitor);
	}
	
	
	
	protected void render(final Graphics2D gfx, final int w, final int h) {
		gfx.setColor(getBackground());
		gfx.fillRect(0, 0, w, h);
		
		gfx.translate(((w / 2) + dx), ((h / 2) + dy));
		
		final Game game = this.game;
		final Operator operator = this.operator;
		if (game != null) {
			game.render(gfx, Tile.RESOLUTION);
		} else if (operator != null) {
			final Orientation pos = operator.getOrientation();
			RenderingUtils.renderPlayer(gfx,
					Math.toRadians(pos.getHeadingBody()),
					Math.toRadians(pos.getHeadingHead()));
		} else {
			final String msg = "<Insert Coin>";
			
			final Font font = new Font(Font.SANS_SERIF, Font.BOLD, 16);
			gfx.setColor(getForeground());
			gfx.setFont(font);
			
			final FontMetrics fm = gfx.getFontMetrics(font);
			
			gfx.drawString(msg,
					-(fm.stringWidth(msg) / 2),
					(fm.getHeight() / 2));
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
	
	public void setOperator(final Operator operator) {
		this.operator = operator;
		repaint(0L);
	}
	
	
	
	
	
	
	
	
	
	
	private final class MouseMonitor extends MouseAdapter {
		
		private int dx, dy, x0, y0;
		
		
		
		@Override
		public void mouseDragged(final MouseEvent event) {
			DefaultVisualizationComponent.this.dx = (dx + event.getX() - x0);
			DefaultVisualizationComponent.this.dy = (dy + event.getY() - y0);
			repaint(0L);
		}
		
		@Override
		public void mousePressed(final MouseEvent event) {
			switch (event.getButton()) {
				case MouseEvent.BUTTON1:
					dx = DefaultVisualizationComponent.this.dx;
					dy = DefaultVisualizationComponent.this.dy;
					x0 = event.getX();
					y0 = event.getY();
					break;
					
				case MouseEvent.BUTTON3:
					DefaultVisualizationComponent.this.dx = 0;
					DefaultVisualizationComponent.this.dy = 0;
					repaint(0L);
					break;
			}
		}
		
	}
	
}
