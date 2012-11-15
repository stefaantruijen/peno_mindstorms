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
 * 
 * @author  Incalza Dario
 */
public class BlueBot {
	
	public static void main(final String... args) {
		try {
			for (;;) {
				System.out.println("Standby mode");
				for (boolean enter = false; !enter;) {
					switch (Button.waitForAnyPress()) {
						case Button.ID_ENTER:
							enter = true;
							break;
						case Button.ID_ESCAPE:
							return;
					}
				}
				
				System.out.println("Connecting ...");
				final Connection connection = ServerConnection.create();
				System.out.println("Connected!");
				
				final Robot robot = new PhysicalRobot();
				
				final Driver driver = new DefaultDriver(robot, connection);
				
				final DriverHandler handler = new DriverHandler(driver);
				handler.start();
				
				final Communicator communicator =
						new Communicator(connection, handler);
				
				System.out.println("Communicating ...");
				communicator.run();
				
				System.out.println("Done!");
				handler.stop();
				driver.dispose();
			}
		} catch (final Throwable e) {
			e.printStackTrace();
		}
	}
	
	
}
