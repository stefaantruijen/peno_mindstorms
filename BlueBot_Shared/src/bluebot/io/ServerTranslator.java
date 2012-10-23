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
	
	
	
	/*
	 * @ mensen die niet graag lege klassen zien:
	 * 
	 * Dit is geen lege klasse.
	 * Hier komen methodes van zodra onze NXT/simulator
	 * messages terug stuurt naar de client
	 */
	
}