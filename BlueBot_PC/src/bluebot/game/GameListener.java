package bluebot.game;


import java.util.EventListener;



/**
 * Represents the listener interface for the {@link Game} class
 * 
 * @author Ruben Feyen
 */
public interface GameListener extends EventListener {
	
	public void onStopped();
	
	public void onUpdate();
	
}
