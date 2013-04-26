package bluebot.actions.impl;


import static bluebot.io.protocol.impl.MovePacket.*;

import bluebot.actions.Action;



/**
 * Represents a movement {@link Action}
 * 
 * @author Ruben Feyen
 */
public class MovementAction extends Action {
	
	private int direction;
	private float quantity;
	private boolean wait;
	
	
	public MovementAction(final int direction, final float quantity, boolean b) {
		this.direction = direction;
		this.quantity = quantity;
		this.wait = b;
	}
	
	
	
	public static MovementAction createBackward(final float distance,boolean b) {
		return new MovementAction(MOVE_BACKWARD, distance,b);
	}
	
	public static MovementAction createForward(final float distance,boolean b) {
		return new MovementAction(MOVE_FORWARD, distance,b);
	}
	
	public static MovementAction createLeft(final float angle,boolean b) {
		return new MovementAction(TURN_LEFT, angle,b);
	}
	
	public static MovementAction createRight(final float angle, boolean b) {
		return new MovementAction(TURN_RIGHT, angle,b);
	}
	
	protected void execute() throws InterruptedException {
		switch (direction) {
			case MOVE_BACKWARD:
				getDriver().moveBackward(quantity, this.wait);
				break;
			case MOVE_FORWARD:
				getDriver().moveForward(quantity, this.wait);
				break;
			case TURN_LEFT:
				getDriver().turnLeft(quantity, this.wait);
				break;
			case TURN_RIGHT:
				getDriver().turnRight(quantity, this.wait);
				break;
		}
		while (!isAborted() && getDriver().isMoving()) {
			Thread.sleep(10);
		}
	}
	
}