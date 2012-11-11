package bluebot.core;


import lejos.pc.comm.NXTCommException;

import bluebot.DefaultDriver;
import bluebot.Driver;
import bluebot.DriverHandler;
import bluebot.Robot;
import bluebot.graph.Tile;
import bluebot.io.ClientConnector;
import bluebot.io.Communicator;
import bluebot.io.Connection;
import bluebot.io.VirtualConnection;
import bluebot.maze.Maze;
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
		return createController(new ClientConnector().connectTo("BlueBot"));
	}
	
	/**
	 * Creates a controller for a simulator
	 * 
	 * @param tiles - an array of {@link Tile} objects representing the virtual maze
	 * 
	 * @return a {@link Controller} object
	 */
	public Controller connectToSimulator(final Tile[] tiles) {
		Tile start = null;
		for (final Tile tile : tiles) {
			if ((tile.getX() | tile.getY()) == 0) {
				start = tile;
				break;
			}
		}
		
		if (start == null) {
			start = tiles[0];
		}
		
		return createController(new VirtualRobot(tiles, start));
	}
	
	/**
	 * Creates a controller for a test dummy
	 * 
	 * @param maze - a {@link Maze} object representing the virtual maze
	 * 
	 * @return a {@link Controller} object
	 */
	public Controller connectToTestDummy(final Maze maze) {
		return createController(new DummyRobot(maze));
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