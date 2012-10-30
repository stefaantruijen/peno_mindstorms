package bluebot.nxt;


import java.util.EventListener;



/**
 * 
 * @author Ruben Feyen
 */
public interface WheelListener extends EventListener {
	
	public void onWheelStarted(long time, float delta);
	
	public void onWheelStopped(long time, float delta);
	
}