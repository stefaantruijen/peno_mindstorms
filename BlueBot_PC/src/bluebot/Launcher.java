package bluebot;


import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.jtattoo.plaf.luna.LunaLookAndFeel;

import bluebot.ui.MainFrame;
import bluebot.ui.RepeatingKeyReleasedEventsFix;



/**
 * 
 * @author Ruben Feyen
 */
public class Launcher {
	
	public static void main(final String... args) {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					RepeatingKeyReleasedEventsFix.install();
					setLookAndFeel();
					new MainFrame().setVisible(true);
				}
			});
		} catch (final Throwable e) {
			e.printStackTrace();
		}
	}
	
	private static final void setLookAndFeel() {
		final LookAndFeel laf;
		try {
			laf =
//				new AcrylLookAndFeel();
//				new AeroLookAndFeel();
//				new FastLookAndFeel();
//				new HiFiLookAndFeel();
				new LunaLookAndFeel();
//				new NoireLookAndFeel();
//				new TextureLookAndFeel();
		} catch (final Throwable e) {
			// L&F not available, perhaps the JTattoo library is missing
			e.printStackTrace();
			return;
		}
		
		try {
			UIManager.setLookAndFeel(laf);
		} catch (final UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}
	
}