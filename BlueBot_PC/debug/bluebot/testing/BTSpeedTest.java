package bluebot.testing;


import java.io.InputStream;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;



/**
 * 
 * @author Ruben Feyen
 */
public class BTSpeedTest {
	
	private static final NXTComm connect(final String name) throws NXTCommException {
		final NXTComm nxtc = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
		final NXTInfo[] devices = nxtc.search(name);
		if (devices.length != 1) {
			throw new NXTCommException("Unable to identify NXT brick");
		}
		nxtc.open(devices[0]);
		return nxtc;
	}
	
	public static void main(final String[] args) {
		try {
			final NXTComm device = connect("Lenny");
			
			final InputStream stream = device.getInputStream();
			
			final int length = (1 << 13);
			final byte[] buffer = new byte[length];
			final long start = System.currentTimeMillis();
			
			double diff;
			long last = 0, total = 0;
			for (int n; (n = stream.read(buffer, 0, length)) != -1;) {
				if (n > 0) {
					total += n;
					if ((System.currentTimeMillis() - last) >= 1000L) {
						diff = ((last = System.currentTimeMillis()) - start);
						System.out.printf("%s%n", (1000D * total / diff));
					}
				}
			}
		} catch (final Throwable e) {
			e.printStackTrace();
		}
	}
	
}