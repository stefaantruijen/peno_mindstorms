package bluebot.ui;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import javax.swing.JInternalFrame;
import javax.swing.JLabel;

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
	private String[] msg;
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
	
	@Override
	protected void processMouseEvent(final MouseEvent event) {
		super.processMouseEvent(event);
		if (event.getID() == MouseEvent.MOUSE_PRESSED) {
			setMessage("TEST", "Test");
		}
	}
	
	protected final void render(final Graphics2D gfx) {
		render(gfx, getWidth(), getHeight());
		
		final String[] msg = this.msg;
		if (msg != null) {
			gfx.setColor(Color.RED);
			gfx.setFont(new Font(Font.MONOSPACED, Font.BOLD, 16));
			
			final int h = gfx.getFontMetrics().getHeight();
			
			int y = 25;
			for (final String line : msg) {
				y += h;
				gfx.drawString(line, 25, y);
			}
		}
	}
	
	protected abstract void render(Graphics2D gfx, int w, int h);
	
	public abstract void reset();
	
	public void setMessage(final String msg, final String title) {
		if (msg == null) {
//			this.msg = null;
			removeAll();
		} else {
//			this.msg = msg.split("\n");
			
			final JLabel label = new JLabel(msg);
			label.setBackground(Color.CYAN);
			label.setOpaque(true);
			
			final JInternalFrame frame = new JInternalFrame(title, true, true);
			frame.add(label);
			frame.pack();
			frame.setBounds(10, 10, 480, 240);
			
			add(frame);
			
			System.out.println(frame.getSize());
		}
		repaint(0L);
	}
	
}