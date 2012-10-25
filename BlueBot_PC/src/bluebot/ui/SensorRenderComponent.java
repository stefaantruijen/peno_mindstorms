package bluebot.ui;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedList;



/**
 * 
 * @author Ruben Feyen
 */
public class SensorRenderComponent extends RenderComponent
		implements Renderable {
	private static final long serialVersionUID = 1L;
	
	private LinkedList<Integer> data;
	private int max, min;
	
	
	public SensorRenderComponent() {
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(final MouseEvent event) {
				final Thread thread = new Thread(new Runnable() {
					public void run() {
//						final Random rng = new Random();
						for (double x = 0.00;; x += 0.05) {
							try {
//								addData(rng.nextInt(100));
								addData(50 + (int)Math.round(50 * Math.sin(x)));
								Thread.sleep(100);
							} catch (final InterruptedException e) {
								break;
							}
						}
					}
				});
				thread.setDaemon(true);
				thread.start();
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
		render(this);
	}
	
	private synchronized final ArrayList<Integer> getData() {
		return new ArrayList<Integer>(data);
	}
	
	@Override
	protected void onResize() {
		super.onResize();
		data = new LinkedList<Integer>();
	}
	
	public void render(final Graphics gfx) {
		final int height = getHeight();
		final int width = getWidth();
		
		gfx.setColor(Color.BLACK);
		gfx.fillRect(0, 0, width, height);
		
		gfx.setColor(Color.CYAN);
		
		final ArrayList<Integer> data = getData();
		for (int x = 0; ((x < width) && (x < data.size())); x++) {
			final int h = (data.get(x) * height / max);
			gfx.fillRect(x, (height - h), 1, h);
		}
	}
	
}