package bluebot;


import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;

import bluebot.core.PhysicalRobot;
import bluebot.io.ServerLink;



/**
 * 
 * @author Ruben Feyen
 */
public class BlueBot2 {
	
	public static void main(final String[] args) {
		try {
			for (;;) {
				System.out.println("CONNECTING ...");
				final BTConnection btc = Bluetooth.waitForConnection();
				System.out.println("CONNECTED");
				
				final Operator operator = new LocalOperator(new PhysicalRobot());
				final OperatorHandler handler =
						new OperatorHandler(operator, new ServerLink(btc));
				
				try {
					handler.run();
				} finally {
					operator.dispose();
				}
			}
		} catch (final Throwable e) {
			e.printStackTrace();
		}
	}
	
}
