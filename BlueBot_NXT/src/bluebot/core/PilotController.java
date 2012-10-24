package bluebot.core;

import lejos.nxt.Motor;
import lejos.robotics.navigation.DifferentialPilot;


public class PilotController {
	
	private static DifferentialPilot pilot = new DifferentialPilot(55.37, 55,167.78, Motor.A, Motor.C, false);
	private final static double defaultTravelSpeed = 300;
	private final static int defaultAccelerationSpeed = 1000;
	private final static double defaultRotationSpeed = 100;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
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
		pilot.travel(Math.abs(distance), true);
	}
	
	/**
	 * Moves the pilot backward over the given distance.
	 * @param distance 
	 * 			the pilot moves backward. Given in mm.
	 */
	public void moveBackward(double distance){
		pilot.travel(- Math.abs(distance), true);
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
		pilot.rotate(Math.abs(angle), true);
	}
	
	/**
	 * Rotates the pilot clockwise over the given angle.
	 * @param angle
	 * 			The wanted angle of rotation in degrees.
	 */
	public void turnRight(double angle){
		pilot.rotate(- Math.abs(angle), true);
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
	
	/**
	 * Sets the travelspeed of the pilot to the given speed. (wielomtrek/s)
	 * @param speed
	 */
	public void setTravelSpeed(double speed){
		pilot.setTravelSpeed(speed);
	}
	
	/**
	 * returns true if the pilot is moving.
	 * @return	true if the pilot is moving.
	 */
	public boolean isMoving(){
		return pilot.isMoving();
	}

	
	

}
