package bluebot.io;


import java.io.InputStream;
import java.io.OutputStream;

import lejos.pc.comm.NXTComm;



/**
 * 
 * @author Ruben Feyen
 */
public class PhysicalClientLink extends ClientLink {
	
	private NXTComm nxtc;
	
	
	public PhysicalClientLink(final NXTComm nxtc) {
		this.nxtc = nxtc;
	}
	
	
	
	protected InputStream createInputRaw() {
		return nxtc.getInputStream();
	}
	
	protected OutputStream createOutputRaw() {
		return nxtc.getOutputStream();
	}
	
}
