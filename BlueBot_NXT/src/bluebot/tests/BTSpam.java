package bluebot.tests;


import java.io.DataOutputStream;

import lejos.nxt.Button;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTConnection;



/**
 * 
 * @author Ruben Feyen
 */
public class BTSpam {
	
	public static void main(final String[] args) {
		try {
			System.out.println("READY");
			Button.waitForAnyPress();
			
			System.out.println("CONNECTING ...");
			final NXTConnection nxtc = Bluetooth.waitForConnection();
			
			final byte[] packet = "PseudoRandomData".getBytes();
			
			System.out.println("SPAMMING ...");
			final DataOutputStream stream = nxtc.openDataOutputStream();
			while ((Button.readButtons() & Button.ID_ENTER) == 0) {
				stream.write(packet);
			}
			
			System.out.println("DONE");
			Button.waitForAnyPress();
		} catch (final Throwable e) {
			// ignored
		}
	}
	
}