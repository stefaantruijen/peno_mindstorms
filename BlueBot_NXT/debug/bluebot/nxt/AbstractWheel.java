package bluebot.nxt;

import bluebot.util.AbstractEventDispatcher;



/**
 * 
 * @author Ruben Feyen
 */
public abstract class AbstractWheel
		extends AbstractEventDispatcher<WheelListener>
		implements Wheel {
	
	private static final double RATIO = (Math.PI / 360D);
	
	private float diameter;
	
	
	public AbstractWheel(final float diameter) {
		setDiameter(diameter);
	}
	
	
	
	protected float convertAngleToDistance(final float angle) {
		return (float)(RATIO * angle * getDiameter());
	}
	
	protected void fireStarted(final long time, final float delta) {
		for (final WheelListener listener : getListeners()) {
			listener.onWheelStarted(time, delta);
		}
	}
	
	protected void fireStopped(final long time, final float delta) {
		for (final WheelListener listener : getListeners()) {
			listener.onWheelStopped(time, delta);
		}
	}
	
	public float getDiameter() {
		return diameter;
	}
	
	private final void setDiameter(final float diameter) {
		this.diameter = diameter;
	}
	
}