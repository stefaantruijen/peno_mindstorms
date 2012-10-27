package bluebot;



/**
 * Represents a robot
 * 
 * @author Ruben Feyen
 */
public interface Robot extends Mobile {
	
	public boolean isMoving();
	
	public int readSensorLight();
	
	public int readSensorUltraSonic();
	
	public void setTravelSpeed(double speed);
	
}