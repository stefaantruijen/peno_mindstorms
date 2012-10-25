package bluebot.core;

import lejos.nxt.Motor;
import lejos.robotics.navigation.DifferentialPilot;


public class PilotController {
	
	private final DifferentialPilot pilot;
	private final static double defaultTravelSpeed = 200;
	private final static int defaultAccelerationSpeed = 500;
	private final static double defaultRotationSpeed = 75;
	
	public PilotController(){
		pilot = new DifferentialPilot(55.37F, 55F,167.78F, Motor.A, Motor.C, false);
		pilot.setAcceleration(defaultAccelerationSpeed);
		pilot.setRotateSpeed(defaultRotationSpeed);
		pilot.setTravelSpeed(defaultTravelSpeed);
	}
	
	/**
	 * @param args
	 */
	public void main(String[] args) {
		pilot.setAcceleration(defaultAccelerationSpeed);
		pilot.setRotateSpeed(defaultRotationSpeed);
		pilot.setTravelSpeed(defaultTravelSpeed);
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
