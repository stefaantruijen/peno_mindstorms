package bluebot.ui;


import java.util.EventListener;



/**
 * 
 * @author Ruben Feyen
 */
public interface TerminalListener extends EventListener {
	
	public void onTerminalCommand(TerminalComponent component, String[] command);
	
}