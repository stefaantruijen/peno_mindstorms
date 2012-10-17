package bluebot;


import bluebot.core.Controller;
import bluebot.core.RemoteController;
import bluebot.io.DummyConnection;
import bluebot.ui.ControllerFrame;



/**
 * Substitute launcher for debugging purposes
 * 
 * @author Ruben Feyen
 */
public class DummyLauncher extends Launcher {
	
	private static final Controller createController() {
//		return DummyController.SINGLETON;
		return new RemoteController(new DummyConnection());
	}
	
	public static void main(final String... args) {
		execute(new DummyLauncher());
	}
	
	@Override
	protected void showUserInterface() {
		new ControllerFrame(createController()).setVisible(true);
	}
	
}