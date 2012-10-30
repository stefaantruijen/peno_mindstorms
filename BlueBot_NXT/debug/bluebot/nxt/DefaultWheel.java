package bluebot.nxt;


import lejos.robotics.RegulatedMotor;
import lejos.robotics.RegulatedMotorListener;



/**
 * 
 * @author Ruben Feyen
 */
public class DefaultWheel extends AbstractWheel {
	
	private RegulatedMotor motor;
	
	
	public DefaultWheel(final float diameter, final RegulatedMotor motor) {
		super(diameter);
		this.motor = motor;
		
		motor.addListener(new MotorMonitor());
	}
	
	
	
	public float getDelta() {
		return convertAngleToDistance(getTachoCount());
	}
	
	private final RegulatedMotor getMotor() {
		return motor;
	}
	
	private final int getTachoCount() {
		return getMotor().getTachoCount();
	}
	
	public void reset() {
		getMotor().resetTachoCount();
	}
	
	
	
	
	
	
	
	
	
	
	private final class MotorMonitor implements RegulatedMotorListener {
		
		public void rotationStarted(final RegulatedMotor motor,
				final int tachoCount,
				final boolean stalled,
				final long timeStamp) {
			fireStarted(timeStamp, convertAngleToDistance(tachoCount));
		}
		
		public void rotationStopped(final RegulatedMotor motor,
				final int tachoCount,
				final boolean stalled,
				final long timeStamp) {
			fireStopped(timeStamp, convertAngleToDistance(tachoCount));
		}
		
	}
	
}