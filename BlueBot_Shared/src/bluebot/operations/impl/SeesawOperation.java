package bluebot.operations.impl;


import bluebot.operations.Operation;
import bluebot.operations.OperationException;
import bluebot.sensors.CalibrationException;



/**
 * 
 * @author Ruben Feyen
 */
public class SeesawOperation extends Operation<Void> {
	
	protected Void execute()
			throws CalibrationException, InterruptedException, OperationException {
		int speed = getOperator().getSpeed();
		getOperator().setSpeed(50);
		getOperator().moveForward();
		while(getOperator().isMoving() && !getOperator().isSensorLightBrightnessGray());
		while(getOperator().isMoving() && !getOperator().isSensorLightBrightnessBlack());
		getOperator().stop();
		getOperator().setSpeed(12);
		getOperator().moveForward();
		while(getOperator().isMoving() && !getOperator().isSensorLightBrightnessGray());
		getOperator().stop();
		getOperator().doWhiteLine();
		getOperator().stop();
		getOperator().setSpeed(speed);
		getOperator().moveForward(200, true);
		return null;
	}
	
}
