package bluebot.actions.impl;

import bluebot.Driver;
import bluebot.actions.Action;
import bluebot.actions.ActionException;

public class ReadBarcodeAction extends Action {
	private Driver driver;
	private final boolean inFrontOfFirstBlack;
	private int blackThreshold;
	private int whiteThreshold;
	private int slow = 10;
	private String barcode ="";
	
	/**
	 * 
	 * @param inFrontOfBlack
	 * 			boolean representing whether the robot is already in front of a black line.
	 * 			
	 */
	public ReadBarcodeAction(boolean inFrontOfBlack) {
		this.inFrontOfFirstBlack = inFrontOfBlack;
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
		if(!inFrontOfFirstBlack){
			//Find a black line by moving forward until one is found.
			driver.setSpeed(50);
			driver.moveForward();
			waitForBlack(driver, true);
			driver.stop();
			if(driver.readSensorLight()>blackThreshold){
				throw new ActionException("The robot was not properly placed before executing the read barcode algorithm");
			}
			driver.setSpeed(slow);
			driver.moveBackward();
			waitForBlack(driver, false);
			driver.stop();
		}
		driver.setSpeed(slow);
		//Here the robot should be right in front of the first black line.
		//Move to the middle of the first black line
		driver.moveForward(10, true);
		for(int i=0; i<8; i++){			//Read the line
			if(driver.readSensorLight() <= blackThreshold){
				appendBlackToBarcode();
			} else if( driver.readSensorLight() >= whiteThreshold){
				appendWhiteToBarcode();
			}
			//Move to middle of next line
			driver.moveForward(20, true);
		}
		//Check if robot is not on a line anymore.
		if(driver.readSensorLight()<=blackThreshold || driver.readSensorLight() >= whiteThreshold){
			throw new ActionException("The robot is still on a line");
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

	public String getBarcode() {
		return barcode;
	}
	
	/**
	 * Appends a "0" (representing a black line to the barcode.
	 * @return 
	 */
	private void appendBlackToBarcode(){
		barcode += "0";
	}
	
	/**
	 * Appends a "1" (representing a black line to the barcode.
	 * @return 
	 */
	private void appendWhiteToBarcode(){
		barcode += "1";
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
	
	

}
