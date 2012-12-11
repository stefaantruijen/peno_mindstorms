package bluebot.actions.impl;


import algorithms.AbstractBarcodeScanner;
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
	
	protected void execute() throws ActionException, DriverException, InterruptedException {
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
					return getDriver().readSensorLightBrightness();
				} catch (final CalibrationException e) {
					throw new RuntimeException(e);
				}
			}
			
			protected void print(String msg) {
				getDriver().sendDebug(msg);
			}
			
			protected boolean isMoving() {
				return getDriver().isMoving();
			}
			
			protected Tile getTile() {
				return new Tile(0, 0);
			}
			
			protected Orientation getOrientation() {
				return getDriver().getOrientation();
			}
		};
		scanner.start();
		getDriver().moveForward(400F, true);
		Thread.sleep(5000L);
		scanner.stop();
	}
	
	@SuppressWarnings("unused")
	private final float getPosition() {
		final Orientation pos = getDriver().getOrientation();
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