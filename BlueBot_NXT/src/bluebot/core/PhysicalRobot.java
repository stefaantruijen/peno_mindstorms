package bluebot.core;


import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;

import bluebot.Robot;



/**
 * The {@link Robot} implementation for the NXT brick
 * 
 * @author Ruben Feyen
 */
public class PhysicalRobot implements Robot {
	
	private PilotController pc;
	private LightSensor sensorLight;
	
	
	public PhysicalRobot() {
		this(SensorPort.S1);
	}
	public PhysicalRobot(final SensorPort light) {
		sensorLight = new LightSensor(light);
	}
	
	
	
	public void moveBackward() {
		pc.backward();
	}
	
	public void moveBackward(final float distance) {
		pc.moveBackward(distance);
	}
	
	public void moveForward() {
		pc.forward();
	}
	
	public void moveForward(final float distance) {
		pc.moveForward(distance);
	}
	
	public int readSensorLight() {
		return sensorLight.readValue();
	}
	
	public void stop() {
		pc.stop();
	}
	
	public void turnLeft() {
		pc.left();
	}
	
	public void turnLeft(final float angle) {
		pc.turnLeft(angle);
	}

	@Override
	public void turnRight() {
		pc.right();
	}
	
	public void turnRight(final float angle) {
		pc.turnRight(angle);
	}
	
}