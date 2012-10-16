import lejos.nxt.Motor;
import lejos.robotics.navigation.DifferentialPilot;


public class PilotController {
	
	private DifferentialPilot pilot = new DifferentialPilot(53.2, 52.85,163, Motor.A, Motor.C, false);

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
		pilot.travel(Math.abs(distance), false);
	}
	
	/**
	 * Moves the pilot backward over the given distance.
	 * @param distance 
	 * 			the pilot moves backward. Given in mm.
	 */
	public void moveBackward(double distance){
		pilot.travel(- Math.abs(distance), false);
	}
	
	/**
	 * Stops the pilot 
	 */
	public void stop(){
		System.out.println("STOPPING");
		pilot.stop();
		System.out.println("STOPPED");
	}
	
	/**
	 * Rotates the pilot anti-clockwise over the given angle.
	 * @param angle
	 * 			The wanted angle of rotation in degrees.
	 */
	public void turnLeft(double angle){
		pilot.rotate(Math.abs(angle), false);
	}
	
	/**
	 * Rotates the pilot clockwise over the given angle.
	 * @param angle
	 * 			The wanted angle of rotation in degrees.
	 */
	public void turnRight(double angle){
		pilot.rotate(- Math.abs(angle), false);
	}
	
	/**
	 * Starts the pilot moving forward.
	 */
	public void forward(){
		pilot.forward();
	}

	/**
	 * Starts the pilot moving backward.
	 */
	public void backward(){
		pilot.backward();
	}

	/**
	 * Starts the pilot turning anti-clockwise.
	 */
	public void left(){
		pilot.rotateLeft();
	}

	/**
	 * Starts the pilot turning clockwise.
	 */
	public void right(){
		pilot.rotateRight();
	}

	
	

}
