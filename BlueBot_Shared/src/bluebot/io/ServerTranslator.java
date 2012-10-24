package bluebot.io;



/**
 * This {@link Translator} implementation provides translation
 * for any server-to-client traffic
 * 
 * @author Ruben Feyen
 */
public class ServerTranslator extends Translator {
	
	public ServerTranslator(final Connection connection) {
		super(connection);
	}
	
	
	
	public void sendError(final String msg) {
		sendPacket(getPacketFactory().createError(msg));
	}
	
}