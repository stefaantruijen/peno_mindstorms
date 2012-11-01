package bluebot.io;


import java.io.IOException;
import java.util.Properties;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;



/**
 * This class is used to establish a connection from the PC to the NXT brick
 * 
 * @author Ruben Feyen
 */
public class ClientConnector {
	
	private NXTComm nxtc;
	
	
	public ClientConnector() throws NXTCommException {
		this.nxtc = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
	}
	
	
	
	/**
	 * Connects to an NXT brick (over bluetooth)
	 * 
	 * @param name - the name of the NXT brick
	 * 
	 * @return a {@link ClientConnection} object
	 * 
	 * @throws NXTCommException if connecting to the NXT brick fails for any reason
	 */
	public ClientConnection connectTo(final String name) throws NXTCommException {
		final NXTInfo nxt = search(name);
		if (nxt == null) {
			throw new NXTCommException("Could not find the NXT brick");
		}
		if (!nxtc.open(nxt)) {
			throw new NXTCommException("Failed to connect with the NXT brick");
		}
		return new ClientConnection(nxtc);
	}
	
	/**
	 * Loads the NXJ cache
	 * 
	 * @return a {@link Properties} object or <code>NULL</code> if the cache can't be loaded
	 */
	private static final Properties loadCache() {
		try {
			return NXTCommFactory.getNXJCache();
		} catch (final NXTCommException e) {
			return null;
		}
	}
	
	/**
	 * Saves the NXJ cache
	 * 
	 * @param cache - the {@link Properties} object holding the cache entries
	 */
	private static final void saveCache(final Properties cache) {
		try {
			NXTCommFactory.saveNXJCache(cache, "BlueBot is the best!");
		} catch (final IOException e) {
			// Failed to write the cache file
			e.printStackTrace();
		}
	}
	
	/**
	 * Searches for a specific NXT brick
	 * 
	 * @param name - the name of the NXT brick
	 * 
	 * @return an {@link NXTInfo} object or <code>NULL</code> if no match was found
	 * 
	 * @throws NXTCommException if an error occurs while searching
	 */
	private final NXTInfo search(final String name) throws NXTCommException {
		// Browse the NXJ cache
		Properties cache = loadCache();
		if (cache != null) {
			String address;
			for (final Object key : cache.keySet()) {
				address = (String)key;
				if (address.startsWith("NXT_00")
						&& name.equals(cache.getProperty(address))) {
					return new NXTInfo(NXTCommFactory.BLUETOOTH,
							name,
							address.substring(4));
				}
			}
		}
		
		// Perform a bluetooth inquiry
		final NXTInfo[] nxts = nxtc.search(name);
		if (nxts.length == 0) {
			return null;
		}
		
		// Update the NXJ cache
		if (cache == null) {
			cache = new Properties();
		}
		for (final NXTInfo nxt : nxts) {
			cache.setProperty(("NXT_" + nxt.deviceAddress), nxt.name);
		}
		saveCache(cache);
		
		// Return the first NXT that was found
		return nxts[0];
	}
	
}