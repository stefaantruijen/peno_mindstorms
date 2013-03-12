package bluebot.actions.impl;


import bluebot.DriverException;
import bluebot.Robot;
import bluebot.actions.Action;
import bluebot.actions.ActionException;
import bluebot.graph.Orientation;
import bluebot.graph.Tile;
import bluebot.maze.BarcodeValidator;
import bluebot.sensors.Brightness;



/**
 * Precodition:
 * Robot is positioned in straight tile (with only walls left and right). 
 * Robot is positioned in the center of the tile.
 * 
 * This class checks whether on the current position there is a barcode.
 * If so a barcode reading algorithm is started. 
 * After the reading the barcode it can be retrieved.
 * @author Dieter, Ruben
 *
 */
public class ReadBarcodeAction extends Action {
	
	private int slow = 16;
	private int barcode =0;
	private Tile currentTile;
	
	
	public ReadBarcodeAction(Tile currentTile) {
		this.currentTile = currentTile;  
	}
	
	
	
	protected void execute() throws ActionException, DriverException, InterruptedException {
		getDriver().setSpeed(slow);
		
		if(!getDriver().readsBlack()){
			int maxDriveDistance = 120;
			//Drive backwards to the first black line.
			getDriver().moveBackward(maxDriveDistance, false);
			waitForLightSensor(Brightness.BLACK, true);
			if (getDriver().readsBlack()) {
				getDriver().stop();
			} else {
				//No barcode in this Tile.
				getDriver().moveForward(maxDriveDistance, true);
				return;
			}
		}
		//Compensate for case that we are in the barcode.
		while(!getDriver().readsGray()){
			getDriver().moveForward(20, true);
		}
		
		getDriver().moveBackward();
		waitForLightSensor(Brightness.BLACK, true);
		float difference = getPosition();
		getDriver().moveForward();
		waitForLightSensor(Brightness.BLACK, false);
		difference = Math.abs(difference - getPosition());
		//Position robot in middle of first black line
		getDriver().moveBackward(difference/2F+10, true);
		if(!getDriver().readsBlack()){
			//Positioning failed
			throw new ActionException("First black line moved. :0");
		}
		//We are in the middle of the first black line.
		for(int i=6; i>0; i--){ // Read the 6 significant lines.
			getDriver().moveBackward(20, true);
			if(getDriver().readsBlack()){
				appendBlackToBarcode();
			} else if(getDriver().readsWhite()){
				appendWhiteToBarcode();
			}
		}
		//Move robot to center of the ending black line
		getDriver().moveBackward(20, true);
		if(!getDriver().readsBlack()){
			//Positioning failed
			throw new ActionException("End black line moved. :0");
		}
		
		//Now validate the read barcode and update the tile.
		barcode = BarcodeValidator.validate(barcode);
		if(barcode != -1){
			currentTile.setBarCode(barcode);
			getDriver().sendTile(currentTile);
			getDriver().sendDebug("Barcode found:  " + barcode);
		}
		
//		// Return to the middle of the tile.
//		//	There's no need to adjust/correct our heading,
//		//      since this algorithm never turns the robot.
//		//      We could simply drive backwards
//		//      until we discover the white line,
//		//      and then move forward (200 + |sensor-center|) mm
//		getDriver().moveBackward();
//		waitForLightSensor(Brightness.WHITE, true);
//		getDriver().stop();
//		getDriver().moveForward(200 + Robot.OFFSET_SENSOR_LIGHT, true);
		
		// Return to the middle of the tile.
		//	There's no need to adjust/correct our heading,
		//      since this algorithm never turns the robot.
		//      We could simply drive backwards
		//      until we discover grey ground and then
		//		move forward equal to the barcode length/2
		//		+ distance between lightsensor and wheelaxis.
		//		= 8 + 4.2 cm = 12.2cm
		getDriver().moveBackward();
		waitForLightSensor(Brightness.GRAY, true);
		getDriver().stop();
		getDriver().moveForward(80 + Robot.OFFSET_SENSOR_LIGHT, true);
	}

	private float getPosition() {
		bluebot.util.Orientation pos = getDriver().getOrientation();
		Orientation orient = Orientation.forHeading(pos.getHeadingBody());
		switch (orient) {
		case NORTH:
		case SOUTH:
			return pos.getY();
		case EAST:
		case WEST:
			return pos.getX();
		default:
			getDriver().sendError("Wrong heading, orientation does not exist");
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
