package bluebot;


import bluebot.core.PhysicalRobot;
import bluebot.nxt.Axle;
import bluebot.nxt.DefaultAxle;
import bluebot.nxt.DefaultWheel;

import lejos.nxt.Button;
import lejos.nxt.Motor;



/**
 * 
 * @author Ruben Feyen
 */
public class AxleBot {
	
	private static final float DEFAULT_ANGLE = 90F;
	private static final float DEFAULT_DISTANCE = 100F;
	
	private Axle axle;
	private Robot robot;
	
	
	private AxleBot() {
		this.axle = createAxle();
		this.robot = createRobot();
	}
	
	
	
	private static final Axle createAxle() {
		return new DefaultAxle(168.50F,
				new DefaultWheel(55.37F, Motor.A),
				new DefaultWheel(55.00F, Motor.B));
	}
	
	private static final Robot createRobot() {
		return new PhysicalRobot();
	}
	
	public static void main(final String... args) {
		new AxleBot().run();
	}
	
	private final void print() {
		System.out.println(axle.getX() + ", " + axle.getY());
		System.out.println(axle.getHeading());
	}
	
	public void run() {
		axle.reset();
		for (print(); true; print()) {
			switch (Button.waitForAnyPress()) {
				case Button.ID_ENTER:
					robot.moveForward(DEFAULT_DISTANCE, true);
					break;
				case Button.ID_ESCAPE:
					robot.moveBackward(DEFAULT_DISTANCE, true);
					break;
				case Button.ID_LEFT:
					robot.turnLeft(DEFAULT_ANGLE, true);
					break;
				case Button.ID_RIGHT:
					robot.turnRight(DEFAULT_ANGLE, true);
					break;
				default:
					return;
			}
		}
	}
	
}