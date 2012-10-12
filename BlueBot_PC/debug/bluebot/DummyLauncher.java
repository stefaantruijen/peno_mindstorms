package bluebot;


import javax.swing.SwingUtilities;

import bluebot.core.DummyController;
import bluebot.ui.ControllerFrame;
import bluebot.ui.RepeatingKeyReleasedEventsFix;



/**
 * Substitute launcher for debugging purposes
 * 
 * @author Ruben Feyen
 */
public class DummyLauncher {
	
	public static void main(final String... args) {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					RepeatingKeyReleasedEventsFix.install();
					new ControllerFrame(DummyController.SINGLETON).setVisible(true);
				}
			});
		} catch (final Throwable e) {
			e.printStackTrace();
		}
	}
	
}