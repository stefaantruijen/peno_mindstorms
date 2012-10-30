package bluebot.core;


import lejos.pc.comm.NXTCommException;

import bluebot.DefaultDriver;
import bluebot.Driver;
import bluebot.DriverHandler;
import bluebot.Robot;
import bluebot.io.ClientConnection;
import bluebot.io.Communicator;
import bluebot.io.Connection;
import bluebot.io.VirtualConnection;
import bluebot.simulator.DummyRobot;
import bluebot.simulator.VirtualRobot;



/**
 * Factory class for {@link Controller} objects
 * 
 * @author Ruben Feyen
 */
public class ControllerFactory {
	
	private static ControllerFactory singleton;
	
	
	private ControllerFactory() {
		// hidden
	}
	
	
	
	/**
	 * Creates a controller for an NXT brick
	 * 
	 * @param name - the name of the NXT brick
	 * 
	 * @return a {@link Controller} object
	 * 
	 * @throws NXTCommException if the connection with the NXT brick fails
	 */
	public Controller connectToBrick(final String name) throws NXTCommException {
		return createController(ClientConnection.create(name));
	}
	
	/**
	 * Creates a controller for a simulator
	 * 
	 * @return a {@link Controller} object
	 */
	public Controller connectToSimulator() {
		return createController(new VirtualRobot());
	}
	
	// TODO: Remove this method after debugging
	public Controller connectToTestDummy() {
		return createController(new DummyRobot());
	}
	
	/**
	 * Creates a controller for the given connection
	 * 
	 * @param connection - a {@link Connection} object
	 * 
	 * @return a {@link Controller} object
	 */
	private final Controller createController(final Connection connection) {
		return new DefaultController(connection);
	}
	
	/**
	 * Creates a controller for the given robot
	 * over a (virtual) local connection
	 * 
	 * @param robot - a {@link Robot}
	 * 
	 * @return a {@link Controller} object
	 */
	private final Controller createController(final Robot robot) {
		final VirtualConnection connection = new VirtualConnection();
		
		final Connection server = connection.createServer();
		
		final Driver driver = new DefaultDriver(robot, server);
		
		final DriverHandler handler = new DriverHandler(driver);
		handler.start();
		
		final Communicator communicator = new Communicator(server, handler);
		communicator.start();
		
		final Controller controller =
				createController(connection.createClient());
		// TODO: Listen for disconnect to terminate threads
		return controller;
	}
	
	/**
	 * Returns the {@link ControllerFactory} instance
	 * 
	 * @return a {@link ControllerFactory} object
	 */
	public static ControllerFactory getControllerFactory() {
		if (singleton == null) {
			singleton = new ControllerFactory();
		}
		return singleton;
	}
	
}