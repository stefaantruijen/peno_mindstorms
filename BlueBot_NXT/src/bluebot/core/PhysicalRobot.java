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
		pc = new PilotController();
		sensorLight = new LightSensor(light);
	}
	
	
	
	@Override
	public void moveBackward() {
		pc.backward();
	}
	@Override
	public void moveBackward(final float distance) {
		pc.moveBackward(distance);
	}
	@Override
	public void moveForward() {
		pc.forward();
	}
	@Override
	public void moveForward(final float distance) {
		pc.moveForward(distance);
	}
	@Override
	public int readSensorLight() {
		return sensorLight.readValue();
	}
	@Override
	public void stop() {
		pc.stop();
	}
	@Override
	public void turnLeft() {
		pc.left();
	}
	@Override
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
	@Override
	public void setTravelSpeed(double speed) {
		pc.setTravelSpeed(speed);
		
	}
	@Override
	public boolean isMoving() {
		return pc.isMoving();
	}
	
}