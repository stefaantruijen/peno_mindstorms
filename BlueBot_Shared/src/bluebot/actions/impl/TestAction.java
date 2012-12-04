package bluebot.actions.impl;


import algorithms.AbstractBarcodeScanner;
import bluebot.Driver;
import bluebot.DriverException;
import bluebot.actions.Action;
import bluebot.actions.ActionException;
import bluebot.graph.Tile;
import bluebot.sensors.Brightness;
import bluebot.sensors.CalibrationException;
import bluebot.util.Orientation;



/**
 * 
 * @author Ruben Feyen
 */
public class TestAction extends Action {
	
	private Driver driver;
	
	
	
	public void execute(final Driver driver)
			throws ActionException, DriverException, InterruptedException {
		this.driver = driver;
		
		/*
		driver.moveForward(400F, false);
		waitForMoving(driver, true);
		while (driver.isMoving()) {
			Thread.sleep(10);
			driver.sendDebug(getPosition() + "\t" + driver.readSensorLightBrightness());
		}
		*/
		
		final AbstractBarcodeScanner scanner = new AbstractBarcodeScanner() {
			protected Brightness readSensor() {
				try {
					return driver.readSensorLightBrightness();
				} catch (final CalibrationException e) {
					throw new RuntimeException(e);
				}
			}
			
			protected void print(String msg) {
				driver.sendDebug(msg);
			}
			
			protected boolean isMoving() {
				return driver.isMoving();
			}
			
			protected Tile getTile() {
				return new Tile(0, 0);
			}
			
			protected Orientation getOrientation() {
				return driver.getOrientation();
			}
		};
		scanner.start();
		driver.moveForward(400F, true);
		Thread.sleep(5000L);
		scanner.stop();
	}
	
	@SuppressWarnings("unused")
	private final float getPosition() {
		final Orientation pos = driver.getOrientation();
		switch (bluebot.graph.Orientation.forHeading(pos.getHeadingBody())) {
			case NORTH:
			case SOUTH:
				return pos.getY();
			case EAST:
			case WEST:
				return pos.getX();
			default:
				throw new RuntimeException("The universe has collapsed!");
		}
	}
	
}