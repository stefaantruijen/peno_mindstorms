package bluebot.core;


import java.io.IOException;

import lejos.pc.comm.NXTCommException;

import bluebot.DefaultDriver;
import bluebot.Driver;
import bluebot.DriverHandler;
import bluebot.Robot;
import bluebot.game.World;
import bluebot.io.ClientConnector;
import bluebot.io.Communicator;
import bluebot.io.Connection;
import bluebot.io.VirtualConnection;
import bluebot.maze.Maze;
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
	 * @throws IOException
	 * @throws NXTCommException if the connection with the NXT brick fails
	 */
	public Controller connectToBrick(final World world, final String name)
			throws IOException, NXTCommException {
		return createController(world, new ClientConnector().connectTo("BlueBot"));
	}
	
	/**
	 * Creates a controller for a simulator
	 * 
	 * @param world - the {@link World} object representing the virtual world
	 * 
	 * @return a {@link Controller} object
	 * 
	 * @throws IOException 
	 */
	public Controller connectToSimulator(final World world) throws IOException {
//		Tile start = null;
//		for (final Tile tile : tiles) {
//			if ((tile.getX() | tile.getY()) == 0) {
//				start = tile;
//				break;
//			}
//		}
//		
//		if (start == null) {
//			start = tiles[0];
//		}
		
		return createController(world, new VirtualRobot(world));
	}
	
	/**
	 * Creates a controller for a test dummy
	 * 
	 * @param maze - a {@link Maze} object representing the virtual maze
	 * 
	 * @return a {@link Controller} object
	 
	public Controller connectToTestDummy(final Maze maze) {
		return createController(new DummyRobot(maze));
	}
	
	/**
	 * Creates a controller for the given connection
	 * 
	 * @param world - the {@link World} reference
	 * @param connection - a {@link Connection} object
	 * 
	 * @return a {@link Controller} object
	 * 
	 * @throws IOException
	 */
	private final Controller createController(final World world,
			final Connection connection) throws IOException {
		return new DefaultController(world, connection);
	}
	
	/**
	 * Creates a controller for the given robot
	 * over a (virtual) local connection
	 * 
	 * @param robot - a {@link Robot}
	 * 
	 * @return a {@link Controller} object
	 * 
	 * @throws IOException 
	 */
	private final Controller createController(final World world, final Robot robot)
			throws IOException {
		final VirtualConnection connection = new VirtualConnection();
		
		final Connection server = connection.createServer();
		
		final Driver driver = new DefaultDriver(robot, server);
		
		final DriverHandler handler = new DriverHandler(driver);
		handler.start();
		
		final Communicator communicator = new Communicator(server, handler);
		communicator.start();
		
		final Controller controller =
				createController(world, connection.createClient());
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