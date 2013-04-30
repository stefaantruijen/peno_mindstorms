package bluebot.operations;


import bluebot.Operator;
import bluebot.sensors.Brightness;
import bluebot.sensors.CalibrationException;



/**
 * 
 * @author Ruben Feyen
 */
public abstract class Operation<T> {
	
	private Operator operator;
	
	
	
	protected final void checkAborted() throws InterruptedException {
		if (isAborted()) {
			throw new InterruptedException("Operation aborted");
		}
	}
	
	protected abstract T execute() throws CalibrationException,
			InterruptedException, OperationException;
	
	public synchronized final T execute(final Operator operator)
			throws CalibrationException, InterruptedException, OperationException {
		final int speed = operator.getSpeed();
		setOperator(operator);
		
		final T result;
		try {
			result = execute();
		} finally {
			setOperator(null);
			operator.setSpeed(speed);
		}
		return result;
	}
	
	protected Operator getOperator() {
		return operator;
	}
	
	public boolean isAborted() {
		return false;
	}
	
	private final void setOperator(final Operator operator) {
		this.operator = operator;
	}
	
	protected void waitForLightSensor(final Brightness color, final boolean detect)
			throws CalibrationException, InterruptedException {
		final Operator operator = getOperator();
		if (detect) {
			while (operator.isMoving()
					&& (operator.readSensorLightBrightness() != color)) {
				checkAborted();
			}
		} else {
			while (operator.isMoving()
					&& (operator.readSensorLightBrightness() == color)) {
				checkAborted();
			}
		}
	}
	
}
