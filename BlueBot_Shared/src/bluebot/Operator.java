package bluebot;


import bluebot.operations.OperationException;
import bluebot.sensors.Brightness;
import bluebot.sensors.Calibration;
import bluebot.sensors.CalibrationException;



/**
 * 
 * @author Ruben Feyen
 */
public interface Operator extends Mobile {
	
	public static final int OP_STOP					= 0x10;
	public static final int OP_MOVE_FORWARD			= 0x11;
	public static final int OP_MOVE_BACKWARD		= 0x12;
	public static final int OP_TURN_LEFT			= 0x13;
	public static final int OP_TURN_RIGHT			= 0x14;
	public static final int OP_TURN_HEAD_CW			= 0x15;
	public static final int OP_TURN_HEAD_CCW		= 0x16;
	public static final int OP_ORIENTATION_GET		= 0x21;
	public static final int OP_ORIENTATION_MODIFY	= 0x22;
	public static final int OP_ORIENTATION_RESET	= 0x23;
	public static final int OP_MOVING				= 0x30;
	public static final int OP_SPEED_GET			= 0x31;
	public static final int OP_SPEED_SET			= 0x32;
	public static final int OP_SENSOR				= 0x41;
	public static final int OP_DO_BARCODE			= 0x51;
	public static final int OP_DO_CALIBRATE			= 0x52;
	public static final int OP_DO_PICKUP			= 0x53;
	public static final int OP_DO_SEESAW			= 0x54;
	public static final int OP_DO_WHITELINE			= 0x55;
	
	
	
	public boolean detectInfrared();
	
	public void dispose();
	
	public void doCalibrate()
			throws InterruptedException, OperationException;
	
	public void doPickUp()
			throws CalibrationException, InterruptedException, OperationException;
	
	public void doSeesaw()
			throws CalibrationException, InterruptedException, OperationException;
	
	public void doWhiteLine()
			throws CalibrationException, InterruptedException, OperationException;
	
	public float getAngleIncrement();
	
	public float getArcLimit();
	
	public Calibration getCalibration();
	
	public boolean isSensorLightBrightnessBlack() throws CalibrationException;
	
	public boolean isSensorLightBrightnessGray() throws CalibrationException;
	
	public boolean isSensorLightBrightnessWhite() throws CalibrationException;
	
	public void modifyOrientation();
	
	public int readSensorInfrared();
	
	public int readSensorLight();
	
	public Brightness readSensorLightBrightness() throws CalibrationException;
	
	public boolean readSensorTouch();
	
	public int readSensorUltrasonic();
	
	public int scanBarcode()
			throws CalibrationException, InterruptedException, OperationException;
	
	public void setStartLocation(int x, int y, float angle);
	
}
