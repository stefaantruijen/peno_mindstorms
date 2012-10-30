package bluebot.nxt;



/**
 * 
 * @author Ruben Feyen
 */
public abstract class AbstractAxle implements Axle {
	
	private static final double RATIO = (360D / Math.PI);
	private float span;
	
	
	public AbstractAxle(final float span) {
		setSpan(span);
	}
	
	
	
	protected float convertDistanceToAngle(final float distance) {
		return (float)(RATIO * distance / getSpan());
	}
	
	public float getHeading() {
		return getOrientation().getHeading();
	}
	
	public float getSpan() {
		return span;
	}
	
	public float getX() {
		return getOrientation().getX();
	}
	
	public float getY() {
		return getOrientation().getY();
	}
	
	private final void setSpan(final float span) {
		this.span = span;
	}
	
}