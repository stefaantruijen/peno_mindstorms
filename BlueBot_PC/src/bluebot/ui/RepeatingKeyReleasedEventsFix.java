package bluebot.ui;


import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.HashMap;

import javax.swing.Timer;



/**
 * 
 * @author <a href="http://endre.stolsvik.com/">Endre Stølsvik</a>
 * @author Ruben Feyen
 * 
 * @see <a href="http://tech.stolsvik.com/2010/05/linux-java-repeats-released-keyevents.html">Original publication</a>
 */
public class RepeatingKeyReleasedEventsFix implements AWTEventListener {
	
	private static final int DELAY = 5;
	
	private static RepeatingKeyReleasedEventsFix singleton;
	
	private final HashMap<Integer, ReleasedAction> map = new HashMap<Integer, ReleasedAction>();
	
	
	
	public void eventDispatched(final AWTEvent event) {
		if (!(event instanceof KeyEvent)) {
			return;
		}
		
		if (event instanceof Reposted) {
			return;
		}
		
		final KeyEvent key = (KeyEvent)event;
		if (key.isConsumed()) {
			return;
		}
		
		final ReleasedAction action;
		switch (event.getID()) {
			case KeyEvent.KEY_PRESSED:
				action = map.remove(Integer.valueOf(key.getKeyCode()));
				if (action != null) {
					action.cancel();
				}
				break;
				
			case KeyEvent.KEY_RELEASED:
				action = new ReleasedAction(key);
				map.put(Integer.valueOf(key.getKeyCode()), action);
				action.schedule();
				key.consume();
				break;
				
			case KeyEvent.KEY_TYPED:
				// ignored
				return;
				
			default:
				// All other AWT events are ignored
				return;
		}
	}
	
	public static synchronized void install() {
		if (singleton == null) {
			singleton = new RepeatingKeyReleasedEventsFix();
			Toolkit.getDefaultToolkit().addAWTEventListener(singleton,
					AWTEvent.KEY_EVENT_MASK);
		}
	}
	
	
	
	
	
	
	
	
	
	
	private final class ReleasedAction implements ActionListener {
		
		private KeyEvent event;
		private Timer timer;
		
		
		private ReleasedAction(final KeyEvent event) {
			this.event = event;
			this.timer = new Timer(DELAY, this);
		}
		
		
		
		public void actionPerformed(final ActionEvent event) {
			if (timer != null) {
				cancel();
				fire();
			}
		}
		
		private final void cancel() {
			timer.stop();
			timer = null;
			map.remove(Integer.valueOf(event.getKeyCode()));
		}
		
		private final void fire() {
			Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(
					new RepostedKeyEvent((Component)event.getSource(),
							event.getID(),
							event.getWhen(),
							event.getModifiers(),
							event.getKeyCode(),
							event.getKeyChar(),
							event.getKeyLocation()));
		}
		
		public void schedule() {
			timer.start();
		}
		
	}
	
	
	
	
	
	private static interface Reposted {}
	
	
	
	
	
	private static final class RepostedKeyEvent extends KeyEvent
			implements Reposted {
		private static final long serialVersionUID = 1L;
		
		
		public RepostedKeyEvent(final Component source,
				final int id, final long when, final int modifiers,
				final int keyCode, final char keyChar, final int keyLocation) {
			super(source, id, when, modifiers, keyCode, keyChar, keyLocation);
		}
	}
	
}