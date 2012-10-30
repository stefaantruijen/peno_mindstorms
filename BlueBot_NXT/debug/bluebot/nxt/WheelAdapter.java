package bluebot.nxt;



/**
 * 
 * @author Ruben Feyen
 */
public abstract class WheelAdapter implements WheelListener {
	
	public void onWheelStarted(final long time, final float delta) {
		// ignored
	}
	
	public void onWheelStopped(final long time, final float delta) {
		// ignored
	}

}