package bluebot.io;


import java.io.DataInputStream;
import java.io.DataOutputStream;

import lejos.nxt.comm.NXTConnection;



/**
 * 
 * @author Ruben Feyen
 */
public class ServerLink extends Link {
	
	private NXTConnection btc;
	
	
	public ServerLink(final NXTConnection btc) {
		this.btc = btc;
	}
	
	
	
	protected DataInputStream createInput() {
		return btc.openDataInputStream();
	}
	
	protected DataOutputStream createOutput() {
		return btc.openDataOutputStream();
	}
	
}
