package bluebot.tests;


import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;



/**
 * 
 * @author Ruben Feyen
 */
public class BTSpeedTest {
	
	public static void main(final String[] args) {
		try {
			final NXTComm nxtc = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
			final NXTInfo[] devices = nxtc.search("NXT");
			for (final NXTInfo device : devices) {
				System.out.println(device.name);
			}
		} catch (final Throwable e) {
			e.printStackTrace();
		}
	}
	
}