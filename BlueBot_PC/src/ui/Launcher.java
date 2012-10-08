package ui;


import javax.swing.SwingUtilities;



/**
 * 
 * @author Ruben Feyen
 */
public class Launcher {
	
	public static void main(final String... args) {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
//					setLookAndFeel();
					new MainFrame().setVisible(true);
				}
			});
		} catch (final Throwable e) {
			e.printStackTrace();
		}
	}
	
	/*
	private static final void setLookAndFeel() {
		final LookAndFeel laf;
		try {
			laf =
//				new AcrylLookAndFeel();
//				new AeroLookAndFeel();
//				new AluminiumLookAndFeel();
				new FastLookAndFeel();
//				new HiFiLookAndFeel();
//				new LunaLookAndFeel();
//				new McWinLookAndFeel();
//				new MintLookAndFeel();
//				new NoireLookAndFeel();
//				new SmartLookAndFeel();
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
	*/
	
}