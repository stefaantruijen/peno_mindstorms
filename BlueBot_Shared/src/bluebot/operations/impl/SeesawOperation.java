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
		if (getOperator().detectInfrared()) {
			return null;
		}
		
		getOperator().setSpeed(50);
		getOperator().moveForward();
		while(getOperator().isMoving() && !getOperator().isSensorLightBrightnessGray());
		while(getOperator().isMoving() && !getOperator().isSensorLightBrightnessBlack());
		getOperator().setSpeed(12);
		while(getOperator().isMoving() && !getOperator().isSensorLightBrightnessGray());
		getOperator().stop();
		getOperator().doWhiteLine();
		getOperator().moveForward(20, true);
		return null;
	}
	
}
