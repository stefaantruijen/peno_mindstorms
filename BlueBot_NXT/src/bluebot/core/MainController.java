package bluebot.core;

import lejos.nxt.Button;
import bluebot.io.Communicator;
import bluebot.io.Connection;
import bluebot.io.ServerConnection;
/**
 * Create the Main application and fire up all necessary tools.
 * @author  Incalza Dario
 *
 */
public class MainController {
	
	public static void main(final String... args) {
		try {
			System.out.println("Connecting ...");
			final Connection connection = ServerConnection.create();
			System.out.println("Connected!");
			
			final BlueBot bot = new BlueBot();
			bot.start();
			
			final Communicator communicator = new Communicator(connection, bot);
			communicator.start();
			
			System.out.println("Listening ...");
			
			Button.waitForAnyPress();
			
			communicator.stop();
			bot.stop();
			
			System.out.println("Done!");
		} catch (final Throwable e) {
			e.printStackTrace();
		}
	}
	
	
}
