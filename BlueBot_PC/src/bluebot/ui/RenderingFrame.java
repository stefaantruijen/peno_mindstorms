package bluebot.ui;


import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;



/**
 * 
 * @author Ruben Feyen
 */
public abstract class RenderingFrame extends AbstractFrame {
	private static final long serialVersionUID = 1L;
	
	
	public RenderingFrame(final String title) {
		super(title);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(final WindowEvent event) {
				stopRendering();
			}
			
			@Override
			public void windowOpened(final WindowEvent event) {
				startRendering();
			}
		});	
	}
	
	
	
	protected abstract void startRendering();
	
	protected abstract void stopRendering();
	
	
}
