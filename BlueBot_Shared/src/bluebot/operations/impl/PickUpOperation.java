package bluebot.operations.impl;


import bluebot.operations.Operation;
import bluebot.operations.OperationException;
import bluebot.sensors.CalibrationException;



/**
 * 
 * @author Ruben Feyen
 */
public class PickUpOperation extends Operation<Void> {
	
	protected Void execute()
			throws CalibrationException, InterruptedException, OperationException {
		getOperator().doWhiteLine();
		getOperator().modifyOrientation();
		
		final int speed = getOperator().getSpeed();
		getOperator().setSpeed(30);
		getOperator().moveForward();
		while (!getOperator().readSensorTouch()) {
			try {
				Thread.sleep(10);
			} catch (final InterruptedException e) {
				//	ignored
			}
		}
		
		getOperator().stop();
		//robot rijdt naar achter zodat hij vrij 180 graden kan draaien
		getOperator().moveBackward(100F,true);
		getOperator().turnLeft(180, true);
		getOperator().doWhiteLine();
		getOperator().setSpeed(speed);
		getOperator().moveForward(200F, true);
		return null;
	}

}
