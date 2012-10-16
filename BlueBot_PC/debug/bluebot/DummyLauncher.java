package bluebot;


import javax.swing.SwingUtilities;

import bluebot.core.Controller;
import bluebot.core.RemoteController;
import bluebot.io.DummyConnection;
import bluebot.ui.ControllerFrame;
import bluebot.ui.RepeatingKeyReleasedEventsFix;



/**
 * Substitute launcher for debugging purposes
 * 
 * @author Ruben Feyen
 */
public class DummyLauncher {
	
	private static final Controller createController() {
//		return DummyController.SINGLETON;
		return new RemoteController(new DummyConnection());
	}
	
	public static void main(final String... args) {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					RepeatingKeyReleasedEventsFix.install();
					new ControllerFrame(createController()).setVisible(true);
				}
			});
		} catch (final Throwable e) {
			e.printStackTrace();
		}
	}
	
}