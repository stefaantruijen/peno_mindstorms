package bluebot.operations.impl;


import bluebot.Robot;
import bluebot.graph.Orientation;
import bluebot.operations.Operation;
import bluebot.operations.OperationException;
import bluebot.sensors.Brightness;
import bluebot.sensors.CalibrationException;
import bluebot.util.BarcodeValidator;



/**
 * 
 * @author Ruben Feyen
 */
public class BarcodeOperation extends Operation<Integer> {
	
	private int slow = 16;
	private int barcode =0;
	
	
	
	protected Integer execute()
			throws CalibrationException, InterruptedException, OperationException {
		getOperator().setSpeed(slow);
		
		if (!getOperator().isSensorLightBrightnessBlack()) {
			int maxDriveDistance = 120;
			//Drive backwards to the first black line.
			getOperator().moveBackward(maxDriveDistance, false);
			waitForLightSensor(Brightness.BLACK, true);
			if (getOperator().isSensorLightBrightnessBlack()) {
				getOperator().stop();
			} else {
				//No barcode in this Tile.
				getOperator().moveForward(maxDriveDistance, true);
				return -1;
			}
		}
		
		//Compensate for case that we are in the barcode.
		while (!getOperator().isSensorLightBrightnessGray()) {
			getOperator().moveForward(20, true);
		}
		
		getOperator().moveBackward();
		waitForLightSensor(Brightness.BLACK, true);
		float difference = getPosition();
		getOperator().moveForward();
		waitForLightSensor(Brightness.BLACK, false);
		difference = Math.abs(difference - getPosition());
		//Position robot in middle of first black line
		getOperator().moveBackward(difference/2F+10, true);
		if (!getOperator().isSensorLightBrightnessBlack()) {
			//Positioning failed
			throw new OperationException("First black line moved. :0");
		}
		
		//We are in the middle of the first black line.
		for(int i=6; i>0; i--){ // Read the 6 significant lines.
			getOperator().moveBackward(20, true);
			switch (getOperator().readSensorLightBrightness()) {
				case BLACK:
					appendBlackToBarcode();
					break;
				case GRAY:
					//	ignored
					break;
				case WHITE:
					appendWhiteToBarcode();
					break;
			}
		}
		
		//Move robot to center of the ending black line
		getOperator().moveBackward(20, true);
		if (!getOperator().isSensorLightBrightnessBlack()) {
			//Positioning failed
			throw new OperationException("End black line moved. :0");
		}
		
		//Now validate the read barcode and update the tile.
		barcode = BarcodeValidator.validate(barcode);
		
//		// Return to the middle of the tile.
//		//	There's no need to adjust/correct our heading,
//		//      since this algorithm never turns the robot.
//		//      We could simply drive backwards
//		//      until we discover the white line,
//		//      and then move forward (200 + |sensor-center|) mm
//		getOperator().moveBackward();
//		waitForLightSensor(Brightness.WHITE, true);
//		getOperator().stop();
//		getOperator().moveForward(200 + Robot.OFFSET_SENSOR_LIGHT, true);
		
		// Return to the middle of the tile.
		//	There's no need to adjust/correct our heading,
		//      since this algorithm never turns the robot.
		//      We could simply drive backwards
		//      until we discover grey ground and then
		//		move forward equal to the barcode length/2
		//		+ distance between lightsensor and wheelaxis.
		//		= 8 + 4.2 cm = 12.2cm
		getOperator().moveBackward();
		waitForLightSensor(Brightness.GRAY, true);
		getOperator().stop();
		getOperator().moveForward(80 + Robot.OFFSET_SENSOR_LIGHT, true);
		return Integer.valueOf(barcode);
	}
	
	private float getPosition() {
		bluebot.util.Orientation pos = getOperator().getOrientation();
		Orientation orient = Orientation.forHeading(pos.getHeadingBody());
		switch (orient) {
			case NORTH:
			case SOUTH:
				return pos.getY();
			case EAST:
			case WEST:
				return pos.getX();
			default:
				throw new RuntimeException("Wrong orientation");
		}
	}
	
	/**
	 * This returns the value of the barcode if there is one. 
	 * @return
	 * 		If a barcode is found returns it's value (>0)
	 * 		If no barcode is found or an illegal one is found returns -1.
	 */
	public int getBarcode() {
		return barcode;
	}
	
	/**
	 * Appends a "0" (representing a black line) to the barcode.
	 * @return 
	 */
	private void appendBlackToBarcode(){
		barcode <<= 1;
	}
	
	/**
	 * Appends a "1" (representing a white line) to the barcode.
	 * @return 
	 */
	private void appendWhiteToBarcode(){
		barcode <<= 1;
		barcode |= 1;
	}
	
}
