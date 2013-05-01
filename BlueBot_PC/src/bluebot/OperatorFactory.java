package bluebot;


import java.io.IOException;

import lejos.pc.comm.NXTCommException;

import bluebot.game.World;
import bluebot.io.Buffer;
import bluebot.io.Link;
import bluebot.io.PhysicalClientConnector;
import bluebot.io.VirtualClientLink;
import bluebot.simulator.VirtualRobot;



/**
 * 
 * @author Ruben Feyen
 */
public class OperatorFactory {
	
	private OperatorFactory() {
		//	disabled
	}
	
	
	
	private static final Operator connectTo(final Link link) {
		return new RemoteOperator(link);
	}
	
	public static final Operator connectToBrick(final String name)
			throws IOException, NXTCommException {
		return connectTo(new PhysicalClientConnector().connectTo(name));
	}
	
	public static final Operator connectToSimulator(final World world) {
		return new LocalOperator(new VirtualRobot(world));
	}
	
	public static final Operator connectToSimulatorRemotely(final World world) {
		final Operator local = connectToSimulator(world);
		
		final int capacity = 1024;
		final Buffer client2server = new Buffer(capacity);
		final Buffer server2client = new Buffer(capacity);
		
		final OperatorHandler handler = new OperatorHandler(local,
				new VirtualClientLink(client2server, server2client));
		
		final Thread thread = new Thread(handler);
		thread.setDaemon(true);
		thread.start();
		
		return connectTo(new VirtualClientLink(server2client, client2server));
	}
	
}
