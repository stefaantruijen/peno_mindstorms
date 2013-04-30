package bluebot;


import java.io.IOException;

import lejos.pc.comm.NXTCommException;

import bluebot.game.World;
import bluebot.simulator.VirtualRobot;



/**
 * 
 * @author Ruben Feyen
 */
public class OperatorFactory {
	
	private OperatorFactory() {
		//	disabled
	}
	
	
	
	public static final Operator connectToBrick(final String name)
			throws IOException, NXTCommException {
		//	TODO
		throw new UnsupportedOperationException();
	}
	
	public static final Operator connectToSimulator(final World world) {
		return new LocalOperator(new VirtualRobot(world));
	}
	
}
