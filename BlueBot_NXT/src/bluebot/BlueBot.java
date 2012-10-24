package bluebot;

import lejos.nxt.Button;
import bluebot.Driver;
import bluebot.DriverHandler;
import bluebot.Robot;
import bluebot.core.PhysicalRobot;
import bluebot.io.Communicator;
import bluebot.io.Connection;
import bluebot.io.ServerConnection;
/**
 * Create the Main application and fire up all necessary tools.
 * @author  Incalza Dario
 *
 */
public class BlueBot {
	
	public static void main(final String... args) {
		try {
			System.out.println("Connecting ...");
			final Connection connection = ServerConnection.create();
			System.out.println("Connected!");
			
			final Robot robot = new PhysicalRobot();
			
			final Driver driver = new Driver(robot, connection);
			
			final DriverHandler handler = new DriverHandler(driver);
			handler.start();
			
			final Communicator communicator = new Communicator(connection, handler);
			communicator.start();
			
			System.out.println("Listening ...");
			
			Button.waitForAnyPress();
			
			communicator.stop();
			handler.stop();
			
			System.out.println("Done!");
		} catch (final Throwable e) {
			e.printStackTrace();
		}
	}
	
	
}
