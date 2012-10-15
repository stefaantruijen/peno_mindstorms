package simulator;

import bluebot.core.AbstractController;
import bluebot.core.ControllerListener;

public class SimulatorController extends AbstractController {
	private Robot simulatorRobot;
	private Thread simulatorThread;
	
	/*
	public static void main(String[] args) {
		SimulatorController simc = new SimulatorController();
		System.out.println("started moving at " + System.currentTimeMillis());
		simc.moveForward();
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		simc.stop();
		System.out.println("stopped moving at " + System.currentTimeMillis());
	}
	*/
	
	public SimulatorController() {
		simulatorRobot = new Robot();
		simulatorThread = new Thread(simulatorRobot);
		simulatorThread.start();
	}

	@Override
	public void addListener(final ControllerListener listener) {
		super.addListener(listener);
		simulatorRobot.addListener(listener);
	}
	
	@Override
	public void moveBackward() {
		fireMessageOutgoing("Move backward");
		simulatorRobot.backward();
	}

	@Override
	public void moveForward() {
		fireMessageOutgoing("Move forward");
		simulatorRobot.forward();
	}

	@Override
	public void stop() {
		fireMessageOutgoing("Stop");
		simulatorRobot.stop();
	}

	@Override
	public void turnLeft() {
		simulatorRobot.rotate(Double.MAX_VALUE);
	}

	@Override
	public void turnRight() {
		simulatorRobot.rotate(Double.MIN_VALUE);		
	}

	@Override
	protected void moveBackward(float distance) {
		simulatorRobot.travel(-distance);
	}

	@Override
	protected void moveForward(float distance) {
		simulatorRobot.travel(distance);
	}
	
	@Override
	public void removeListener(final ControllerListener listener) {
		super.removeListener(listener);
		simulatorRobot.removeListener(listener);
	}

	@Override
	protected void turnLeft(float angle) {
		simulatorRobot.rotate(angle);
	}

	@Override
	protected void turnRight(float angle) {
		simulatorRobot.rotate(-angle);
	}

}
