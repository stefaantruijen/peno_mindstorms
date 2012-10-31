package bluebot.ui;


import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.LinkedList;



/**
 * 
 * @author Ruben Feyen
 */
public class GraphComponent extends RenderingComponent {
	private static final long serialVersionUID = 1L;
	
	private LinkedList<Integer> data;
	private int max, min = Integer.MAX_VALUE;
	
	
	public GraphComponent() {
//		setOpaque(true);
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(final ComponentEvent event) {
				data = new LinkedList<Integer>();
			}
		});
	}
	
	
	
	public synchronized void addData(final int value) {
		final LinkedList<Integer> data = this.data;
		if (data == null) {
			// The graph isn't visible yet,
			// ignore any undisplayed data
			return;
		}
		
		data.addLast(Integer.valueOf(value));
		for (int spill = (data.size() - getWidth()); spill > 0; spill--) {
			data.removeFirst();
		}
		
		if (value > max) {
			max = value;
		}
		if (value < min) {
			min = value;
		}
		
		repaint(0L);
	}
	
	private synchronized final ArrayList<Integer> getData() {
		return new ArrayList<Integer>(data);
	}
	
	@Override
	public boolean isAntiAliased() {
		return false;
	}
	
	protected void render(final Graphics2D gfx) {
		if (data == null) {
			// Wait for the component to be fully initialized
			return;
		}
		
		final int height = getHeight();
		final int width = getWidth();
		
//		gfx.setColor(getBackground());
//		gfx.fillRect(0, 0, width, height);
		
		gfx.setColor(getForeground());
		
		final ArrayList<Integer> data = getData();
		final int delta = Math.max(1, (max - min));
		for (int x = 0; ((x < width) && (x < data.size())); x++) {
			final int h = (height * (data.get(x) - min) / delta);
			gfx.fillRect(x, (height - h), 1, h);
		}
	}
	
}