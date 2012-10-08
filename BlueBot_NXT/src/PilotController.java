import lejos.nxt.Motor;
import lejos.robotics.navigation.DifferentialPilot;


public class PilotController {
	
	DifferentialPilot pilot = new DifferentialPilot(53.2, 52.85,163, Motor.A, Motor.C, false);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	/**
	 * Moves the pilot forward over the given distance.
	 * @param distance 
	 * 			the pilot moves forward. Given in mm.
	 */
	public void moveForward(double distance){
		pilot.travel(Math.abs(distance));
	}
	
	/**
	 * Moves the pilot backward over the given distance.
	 * @param distance 
	 * 			the pilot moves backward. Given in mm.
	 */
	public void moveBackward(double distance){
		pilot.travel(- Math.abs(distance));
	}
	
	/**
	 * Stops the pilot 
	 */
	public void stop(){
		pilot.stop();
	}
	
	/**
	 * Rotates the pilot anti-clockwise over the given angle.
	 * @param angle
	 * 			The wanted angle of rotation in degrees.
	 */
	public void turnLeft(double angle){
		pilot.rotate(Math.abs(angle));
	}
	
	/**
	 * Rotates the pilot clockwise over the given angle.
	 * @param angle
	 * 			The wanted angle of rotation in degrees.
	 */
	public void turnRight(double angle){
		pilot.rotate(- Math.abs(angle));
	}
	
	

}
