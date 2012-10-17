package bluebot;


import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.jtattoo.plaf.luna.LunaLookAndFeel;

import bluebot.ui.MainFrame;
import bluebot.ui.RepeatingKeyReleasedEventsFix;



/**
 * This class represents the entry point of the client-side application
 * 
 * @author Ruben Feyen
 */
public class Launcher implements Runnable {
	
	protected static final void execute(final Launcher launcher) {
		try {
			SwingUtilities.invokeAndWait(launcher);
		} catch (final Throwable e) {
			e.printStackTrace();
		}
	}
	
	protected void init() {
		RepeatingKeyReleasedEventsFix.install();
		setLookAndFeel();
	}
	
	public static void main(final String... args) {
		execute(new Launcher());
	}
	
	public void run() {
		init();
		showUserInterface();
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
	
	protected void showUserInterface() {
		new MainFrame().setVisible(true);
	}
	
}