package bluebot.ui;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.JComponent;



/**
 * 
 * @author Ruben Feyen
 */
public class GraphComponent extends JComponent {
	private static final long serialVersionUID = 1L;
	
	private LinkedList<Integer> data;
	private int max, min;
	
	
	public GraphComponent() {
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(final ComponentEvent event) {
				data = new LinkedList<Integer>();
			}
		});
	}
	
	
	
	public synchronized void addData(final int value) {
		final LinkedList<Integer> data = this.data;
		
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
	protected void paintComponent(final Graphics gfx) {
		if (data == null) {
			// Wait for the component to be fully initialized
			return;
		}
		
		final int height = getHeight();
		final int width = getWidth();
		
		gfx.setColor(Color.WHITE);
		gfx.fillRect(0, 0, width, height);
		
		gfx.setColor(Color.BLACK);
		
		final ArrayList<Integer> data = getData();
		for (int x = 0; ((x < width) && (x < data.size())); x++) {
			final int h = (data.get(x) * height / max);
			gfx.fillRect(x, (height - h), 1, h);
		}
	}
	
}