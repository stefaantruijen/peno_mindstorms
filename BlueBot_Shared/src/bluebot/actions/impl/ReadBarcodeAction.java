package bluebot.actions.impl;

import bluebot.Driver;
import bluebot.actions.Action;
import bluebot.actions.ActionException;
import bluebot.graph.Orientation;
import bluebot.graph.Tile;
import bluebot.maze.BarcodeValidator;

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
	private Driver driver;
	private int blackThreshold;
	private int whiteThreshold;
	private int slow = 16;
	private int barcode =0;
	private boolean hasBarcode = false;
	private Tile currentTile;
	
	/**
	 * 
	 * 
	 * 
	 */
	public ReadBarcodeAction(Tile currentTile) {
		this.currentTile = currentTile;  
	}

	@Override
	public void execute(Driver driver) throws ActionException,
			InterruptedException {
		this.driver = driver;
		if(!driver.getCalibration().isCalibrated()){
			throw new ActionException("Calibration of the light sensor is required to run the barcode reading algorithm.");
		} 	
		this.blackThreshold = driver.getCalibration().getLightThresholdBlack();
		this.whiteThreshold = driver.getCalibration().getLightThresholdWhite();
		driver.setSpeed(slow);
		
		if(!readBlack()){
			driver.moveBackward(30, false);
			waitForBlack(driver, true);
			if (readBlack()) {
				driver.stop();
			} else {
				//No barcode in this Tile.
				driver.moveForward(30, true);
				return;
			}
		}
		driver.moveForward();
		waitForBlack(driver,false);
		driver.moveBackward();
		waitForBlack(driver, true);
		float difference = getPosition(driver);
		driver.moveForward();
		waitForBlack(driver,false);
		difference = Math.abs(difference - getPosition(driver));
		//Position robot in middle of first black line
		driver.moveBackward(difference/2F+10, true);
		if(!readBlack()){
			//Positioning failed
			throw new ActionException("First black line moved. :0");
		}
		//We are in the middle of the first black line.
		for(int i=6; i>0; i--){ // Read the 6 significant lines.
			driver.moveBackward(20, true);
			if(readBlack()){
				appendBlackToBarcode();
			} else if(readWhite()){
				appendWhiteToBarcode();
			}
		}
		driver.moveBackward(20, true);
		if(!readBlack()){
			//Positioning failed
			throw new ActionException("Second black line moved. :0");
		}
		
		//Return to the middle of the tile.
		driver.moveForward(200, true);
		executeWhiteLine(driver);
		driver.moveBackward(200, true);
		
		//Now validate the readed barcode and update the tile.
		int validatedBarcode = BarcodeValidator.validate(barcode);
		if(validatedBarcode != -1){
			currentTile.setBarCode(validatedBarcode);
		}
	}

	private float getPosition(Driver driver) {
		bluebot.util.Orientation pos = driver.getOrientation();
		Orientation orient = Orientation.forHeading(pos.getHeadingBody());
		switch (orient) {
		case NORTH:
		case SOUTH:
			return pos.getY();
		case EAST:
		case WEST:
			return pos.getX();
		default:
			driver.sendError("Wrong heading, orientation does not exist");
			throw new RuntimeException("Wrong orientation");
		}
	}
	
	
	/**
	 * Waits till a 'black' lightvalue is found. 
	 * @param driver
	 * @param flag
	 */
	//TODO: make this an action to prevent code duplication.
	private final void waitForBlack(final Driver driver, final boolean flag) {
		if (flag) {
			while (!isAborted()
					&& driver.isMoving()
					&& (driver.readSensorLightValue() > blackThreshold));
		} else {
			while (!isAborted()
					&& driver.isMoving()
					&& (driver.readSensorLightValue() <= blackThreshold));
		}
	}

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
	
	/**
	 * Returns whether the given string is a valid barcode.
	 * @return
	 */
	//TODO: see how this works in combination with the executing of the barcodes.
	private boolean isValidBarcode(String s){
		return true;
	}
	
	/**
	 * Returns the reverse of the given string
	 * @return
	 */
	private String reverse(String s){
		String result = "";
		for(int i = s.length()-1; i>=0; i--){
			result += s.charAt(i);
		}
		return result;
	}
	
	private boolean readBlack(){
		return driver.readSensorLightValue() <= blackThreshold;
	}

	
	private boolean readWhite(){
		return driver.readSensorLightValue() >= whiteThreshold;
	}
	
	public boolean hasBarCode(){
		return hasBarcode;
	}
}
