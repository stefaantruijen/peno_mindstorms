package bluebot.ui;


import java.util.EventListener;



/**
 * Event listener interface for the {@link GaugeComponent} class
 * 
 * @author Ruben Feyen
 */
public interface GaugeListener extends EventListener {
	
	public void onValueChanged(int value);
	
	public void onValueRequested(int value);
	
}